package cn.iocoder.yudao.module.radar.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by l09655 on 2022/8/4.
 * 自定义WebSocketServerHandshaker，调用自定义解码器
 */
public class CustomWebSocketServerHandshaker extends WebSocketServerHandshaker {

    public static final String WEBSOCKET_13_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private final boolean allowExtensions;
    private final boolean allowMaskMismatch;

    public CustomWebSocketServerHandshaker(
            String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength) {
        this(webSocketURL, subprotocols, allowExtensions, maxFramePayloadLength, false);
    }

    public CustomWebSocketServerHandshaker(
            String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength,
            boolean allowMaskMismatch) {
        super(WebSocketVersion.V13, webSocketURL, subprotocols, maxFramePayloadLength);
        this.allowExtensions = allowExtensions;
        this.allowMaskMismatch = allowMaskMismatch;
    }

    @Override
    protected FullHttpResponse newHandshakeResponse(FullHttpRequest req, HttpHeaders responseHeaders) {
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
        if (responseHeaders != null) {
            res.headers().add(responseHeaders);
        }

        CharSequence key = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY);
        if (key == null) {
            throw new WebSocketHandshakeException("not a WebSocket request: missing key");
        }
        String acceptSeed = key + WEBSOCKET_13_ACCEPT_GUID;
        byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
        String accept = WebSocketUtil.base64(sha1);

        if (logger.isDebugEnabled()) {
            logger.debug("WebSocket version 13 server handshake key: {}, response: {}", key, accept);
        }

        res.headers().add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
        res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
        res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT, accept);
        JSONObject jsonObject = new JSONObject();
        /**
         * 添加自定义返回body
         */
        JSONObject response = new JSONObject();
        response.put("ResponseURL", "/LAPI/V1.0/System/UpServer/Register");
        response.put("ResponseString", "Switching Protocols");
        response.put("StatusCode", "101");
        JSONObject data = new JSONObject();
        //请求参数
        Map<String, String> params = HttpUtil.getRequestParams(req);
        data.put("Cnonce", params.get("Cnonce"));
        data.put("Resign", WebSocketUtil.generateResign(params.get("Algorithm")));
        response.put("Data", data);
        jsonObject.put("Response", response);
        //如果参数不为空，写入
        if (!jsonObject.isEmpty()) {
            ByteBuf jsonBuf = Unpooled.copiedBuffer(JSON.toJSONString(jsonObject), CharsetUtil.UTF_8);
            res.content().writeBytes(jsonBuf);
            jsonBuf.release();
        }

        String subprotocols = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
        if (subprotocols != null) {
            String selectedSubprotocol = selectSubprotocol(subprotocols);
            if (selectedSubprotocol == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
                }
            } else {
                res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
            }
        }
        return res;
    }

    /**
     * @return io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder
     * @description: TODO 自定义解码器，用于去除掩码校验
     * @author l09655 2022年08月05日
     */
    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new CustomWebSocketFrameDecoder(true, allowExtensions, maxFramePayloadLength(), allowMaskMismatch);
    }

    /**
     * @return io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder
     * @description: TODO 自定义编码器，用于给请求添加掩码，响应不加掩码
     * @author l09655 2022年08月09日
     */
    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {

        //return new WebSocket13FrameEncoder(false);
        return new CustomWebSocketFrameEncoder(true);
    }
}
