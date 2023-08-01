package cn.iocoder.yudao.module.radar.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.*;

import java.nio.ByteOrder;
import java.util.List;

import static io.netty.buffer.ByteBufUtil.readBytes;

/**
 * Created by l09655 on 2022/8/3.
 * 自定义WebSocket解码器
 * 由于需要同时满足请求带掩码，以及解析不带掩码的响应，在allowMaskMismatch设置为false的时候允许接收不带掩码的请求
 */
public class CustomWebSocketFrameDecoder extends ByteToMessageDecoder implements WebSocketFrameDecoder {

    enum State {
        READING_FIRST,
        READING_SECOND,
        READING_SIZE,
        MASKING_KEY,
        PAYLOAD,
        CORRUPT
    }

    private static final byte OPCODE_CONT = 0x0;
    private static final byte OPCODE_TEXT = 0x1;
    private static final byte OPCODE_BINARY = 0x2;
    private static final byte OPCODE_CLOSE = 0x8;
    private static final byte OPCODE_PING = 0x9;
    private static final byte OPCODE_PONG = 0xA;

    private final long maxFramePayloadLength;
    private final boolean allowExtensions;
    private final boolean expectMaskedFrames;

    private int fragmentedFramesCount;
    private boolean frameFinalFlag;
    private boolean frameMasked;
    private int frameRsv;
    private int frameOpcode;
    private long framePayloadLength;
    private byte[] maskingKey;
    private int framePayloadLen1;
    private boolean receivedClosingHandshake;
    private State state = State.READING_FIRST;

    public CustomWebSocketFrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength) {
        this(expectMaskedFrames, allowExtensions, maxFramePayloadLength, false);
    }

    public CustomWebSocketFrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength,
                                       boolean allowMaskMismatch) {
        this.expectMaskedFrames = expectMaskedFrames;
        this.allowExtensions = allowExtensions;
        this.maxFramePayloadLength = maxFramePayloadLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Discard all data received if closing handshake was received before.
        if (receivedClosingHandshake) {
            in.skipBytes(actualReadableBytes());
            return;
        }
        switch (state) {
            case READING_FIRST:
                if (!in.isReadable()) {
                    return;
                }

                framePayloadLength = 0;

                // FIN, RSV, OPCODE
                byte b = in.readByte();
//                System.out.println("读取数据为：" + b);
                frameFinalFlag = (b & 0x80) != 0;
                frameRsv = (b & 0x70) >> 4;
                frameOpcode = b & 0x0F;


                state = State.READING_SECOND;
            case READING_SECOND:
                if (!in.isReadable()) {
                    return;
                }
                // MASK, PAYLOAD LEN 1
                b = in.readByte();
                frameMasked = (b & 0x80) != 0;
                framePayloadLen1 = b & 0x7F;

                if (frameRsv != 0 && !allowExtensions) {
                    System.out.println("frameRsv===" + frameRsv);
                    System.out.println("allowExtensions===" + allowExtensions);
                    protocolViolation(ctx, "RSV != 0 and no extension negotiated, RSV:" + frameRsv);
                    return;
                }

                //去掉掩码校验
                /*if (!allowMaskMismatch && expectMaskedFrames != frameMasked) {
                    System.out.println("allowMaskMismatch===" + allowMaskMismatch);
                    System.out.println("expectMaskedFrames===" + expectMaskedFrames);
                    System.out.println("frameMasked===" + frameMasked);
                    //protocolViolation(ctx, "received a frame that is not masked as expected");
                    //return;
                }*/

                if (frameOpcode > 7) { // control frame (have MSB in opcode set)

                    // control frames MUST NOT be fragmented
                    if (!frameFinalFlag) {
                        protocolViolation(ctx, "fragmented control frame");
                        return;
                    }

                    // control frames MUST have payload 125 octets or less
                    if (framePayloadLen1 > 125) {
                        protocolViolation(ctx, "control frame with payload length > 125 octets");
                        return;
                    }

                    // check for reserved control frame opcodes
                    if (!(frameOpcode == OPCODE_CLOSE || frameOpcode == OPCODE_PING
                            || frameOpcode == OPCODE_PONG)) {
                        protocolViolation(ctx, "control frame using reserved opcode " + frameOpcode);
                        return;
                    }

                    // close frame : if there is a body, the first two bytes of the
                    // body MUST be a 2-byte unsigned integer (in network byte
                    // order) representing a getStatus code
                    if (frameOpcode == 8 && framePayloadLen1 == 1) {
                        protocolViolation(ctx, "received close control frame with payload len 1");
                        return;
                    }
                } else { // data frame
                    // check for reserved data frame opcodes
                    if (!(frameOpcode == OPCODE_CONT || frameOpcode == OPCODE_TEXT
                            || frameOpcode == OPCODE_BINARY)) {
                        protocolViolation(ctx, "data frame using reserved opcode " + frameOpcode);
                    }

                    // check opcode vs message fragmentation state 1/2
                    if (fragmentedFramesCount == 0 && frameOpcode == OPCODE_CONT) {
                        protocolViolation(ctx, "received continuation data frame outside fragmented message");
                        return;
                    }

                    // check opcode vs message fragmentation state 2/2
                    if (fragmentedFramesCount != 0 && frameOpcode != OPCODE_CONT && frameOpcode != OPCODE_PING) {
                        protocolViolation(ctx,
                                "received non-continuation data frame while inside fragmented message");
                        return;
                    }
                }

                state = State.READING_SIZE;
            case READING_SIZE:

                // Read frame payload length
                if (framePayloadLen1 == 126) {
                    if (in.readableBytes() < 2) {
                        return;
                    }
                    framePayloadLength = in.readUnsignedShort();
                    if (framePayloadLength < 126) {
                        protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
                        return;
                    }
                } else if (framePayloadLen1 == 127) {
                    if (in.readableBytes() < 8) {
                        return;
                    }
                    framePayloadLength = in.readLong();
                    // TODO: check if it's bigger than 0x7FFFFFFFFFFFFFFF, Maybe
                    // just check if it's negative?

                    if (framePayloadLength < 65536) {
                        protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
                        return;
                    }
                } else {
                    framePayloadLength = framePayloadLen1;
                }

                if (framePayloadLength > maxFramePayloadLength) {
                    protocolViolation(ctx, "Max frame length of " + maxFramePayloadLength + " has been exceeded.");
                    return;
                }

                state = State.MASKING_KEY;
            case MASKING_KEY:
                if (frameMasked) {
                    if (in.readableBytes() < 4) {
                        return;
                    }
                    if (maskingKey == null) {
                        maskingKey = new byte[4];
                    }
                    in.readBytes(maskingKey);
                }
                state = State.PAYLOAD;
            case PAYLOAD:
                if (in.readableBytes() < framePayloadLength) {
                    return;
                }

                ByteBuf payloadBuffer = null;
                try {
                    payloadBuffer = readBytes(ctx.alloc(), in, toFrameLength(framePayloadLength));

                    // Now we have all the data, the next checkpoint must be the next
                    // frame
                    state = State.READING_FIRST;

                    // Unmask data if needed
                    if (frameMasked) {
                        unmask(payloadBuffer);
                    }

                    // Processing ping/pong/close frames because they cannot be
                    // fragmented
                    if (frameOpcode == OPCODE_PING) {
                        out.add(new PingWebSocketFrame(frameFinalFlag, frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        return;
                    }
                    if (frameOpcode == OPCODE_PONG) {
                        out.add(new PongWebSocketFrame(frameFinalFlag, frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        return;
                    }
                    if (frameOpcode == OPCODE_CLOSE) {
                        receivedClosingHandshake = true;
                        checkCloseFrameBody(ctx, payloadBuffer);
                        out.add(new CloseWebSocketFrame(frameFinalFlag, frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        return;
                    }

                    // Processing for possible fragmented messages for text and binary
                    // frames
                    if (frameFinalFlag) {
                        // Final frame of the sequence. Apparently ping frames are
                        // allowed in the middle of a fragmented message
                        if (frameOpcode != OPCODE_PING) {
                            fragmentedFramesCount = 0;
                        }
                    } else {
                        // Increment counter
                        fragmentedFramesCount++;
                    }

                    // Return the frame
                    if (frameOpcode == OPCODE_TEXT) {
                        out.add(new TextWebSocketFrame(frameFinalFlag, frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        return;
                    } else if (frameOpcode == OPCODE_BINARY) {
                        out.add(new BinaryWebSocketFrame(frameFinalFlag, frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        return;
                    } else if (frameOpcode == OPCODE_CONT) {
                        out.add(new ContinuationWebSocketFrame(frameFinalFlag, frameRsv,
                                payloadBuffer));
                        payloadBuffer = null;
                        return;
                    } else {
                        throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: "
                                + frameOpcode);
                    }
                } finally {
                    if (payloadBuffer != null) {
                        payloadBuffer.release();
                    }
                }
            case CORRUPT:
                if (in.isReadable()) {
                    // If we don't keep reading Netty will throw an exception saying
                    // we can't return null if no bytes read and state not changed.
                    in.readByte();
                }
                return;
            default:
                throw new Error("Shouldn't reach here.");
        }
    }

    private void unmask(ByteBuf frame) {
        int i = frame.readerIndex();
        int end = frame.writerIndex();

        ByteOrder order = frame.order();

        // Remark: & 0xFF is necessary because Java will do signed expansion from
        // byte to int which we don't want.
        int intMask = ((maskingKey[0] & 0xFF) << 24)
                | ((maskingKey[1] & 0xFF) << 16)
                | ((maskingKey[2] & 0xFF) << 8)
                | (maskingKey[3] & 0xFF);

        // If the byte order of our buffers it little endian we have to bring our mask
        // into the same format, because getInt() and writeInt() will use a reversed byte order
        if (order == ByteOrder.LITTLE_ENDIAN) {
            intMask = Integer.reverseBytes(intMask);
        }

        for (; i + 3 < end; i += 4) {
            int unmasked = frame.getInt(i) ^ intMask;
            frame.setInt(i, unmasked);
        }
        for (; i < end; i++) {
            frame.setByte(i, frame.getByte(i) ^ maskingKey[i % 4]);
        }
    }

    private void protocolViolation(ChannelHandlerContext ctx, String reason) {
        protocolViolation(ctx, new CorruptedFrameException(reason));
    }

    private void protocolViolation(ChannelHandlerContext ctx, CorruptedFrameException ex) {
        state = State.CORRUPT;
        if (ctx.channel().isActive()) {
            Object closeMessage;
            if (receivedClosingHandshake) {
                closeMessage = Unpooled.EMPTY_BUFFER;
            } else {
                closeMessage = new CloseWebSocketFrame(1002, null);
            }
            ctx.writeAndFlush(closeMessage).addListener(ChannelFutureListener.CLOSE);
        }
        throw ex;
    }

    private static int toFrameLength(long l) {
        if (l > Integer.MAX_VALUE) {
            throw new TooLongFrameException("Length:" + l);
        } else {
            return (int) l;
        }
    }

    /**
     *
     */
    protected void checkCloseFrameBody(
            ChannelHandlerContext ctx, ByteBuf buffer) {
        if (buffer == null || !buffer.isReadable()) {
            return;
        }
        if (buffer.readableBytes() == 1) {
            protocolViolation(ctx, "Invalid close frame body");
        }

        // Save reader index
        int idx = buffer.readerIndex();
        buffer.readerIndex(0);

        // Must have 2 byte integer within the valid range
        int statusCode = buffer.readShort();
        if (statusCode >= 0 && statusCode <= 999 || statusCode >= 1004 && statusCode <= 1006
                || statusCode >= 1012 && statusCode <= 2999) {
            protocolViolation(ctx, "Invalid close frame getStatus code: " + statusCode);
        }

        // May have UTF-8 message
        if (buffer.isReadable()) {
            try {
                new Utf8Validator().check(buffer);
            } catch (CorruptedFrameException ex) {
                protocolViolation(ctx, ex);
            }
        }

        // Restore reader index
        buffer.readerIndex(idx);
    }
}
