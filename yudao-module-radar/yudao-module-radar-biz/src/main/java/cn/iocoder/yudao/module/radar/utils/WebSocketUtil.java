package cn.iocoder.yudao.module.radar.utils;

import cn.iocoder.yudao.module.radar.bean.request.WebsocketRequest;
import cn.iocoder.yudao.module.radar.service.ChannelSupervise;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.FastThreadLocal;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by l09655 on 2022/8/2.
 * WebSocket相关工具类
 */
public class WebSocketUtil {

    private static final FastThreadLocal<MessageDigest> MD5 = new FastThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() throws Exception {
            try {
                //Try to get a MessageDigest that uses MD5
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                //This shouldn't happen! How old is the computer?
                throw new InternalError("MD5 not supported on this platform - Outdated?");
            }
        }
    };

    private static final FastThreadLocal<MessageDigest> SHA1 = new FastThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() throws Exception {
            try {
                //Try to get a MessageDigest that uses SHA1
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                //This shouldn't happen! How old is the computer?
                throw new InternalError("SHA-1 not supported on this platform - Outdated?");
            }
        }
    };

    /**
     * @return java.lang.String
     * @description: TODO 随机生成Nonce字段，用于注册流程返回值，demo暂时用不到
     * @author l09655 2022年07月19日
     */
    public static String generateNonce() {
        String nonce = RandomStringUtils.randomAlphanumeric(16);
        return nonce;
    }

    /**
     * @param algorithm
     * @return java.lang.String
     * @description: TODO 根据算法计算签名，暂不考虑，返回随机字符串
     * @author l09655 2022年07月19日
     */
    public static String generateResign(String algorithm) {
        String resign = RandomStringUtils.randomAlphanumeric(16);
        return resign;
    }


    /**
     * Performs a MD5 hash on the specified data
     *
     * @param data The data to hash
     * @return The hashed data
     */
    static byte[] md5(byte[] data) {
        // TODO(normanmaurer): Create md5 method that not need MessageDigest.
        return digest(MD5, data);
    }

    /**
     * Performs a SHA-1 hash on the specified data
     *
     * @param data The data to hash
     * @return The hashed data
     */
    static byte[] sha1(byte[] data) {
        // TODO(normanmaurer): Create sha1 method that not need MessageDigest.
        return digest(SHA1, data);
    }

    private static byte[] digest(FastThreadLocal<MessageDigest> digestFastThreadLocal, byte[] data) {
        MessageDigest digest = digestFastThreadLocal.get();
        digest.reset();
        return digest.digest(data);
    }

    /**
     * Performs base64 encoding on the specified data
     *
     * @param data The data to encode
     * @return An encoded string containing the data
     */
    static String base64(byte[] data) {
        ByteBuf encodedData = Unpooled.wrappedBuffer(data);
        ByteBuf encoded = Base64.encode(encodedData);
        String encodedString = encoded.toString(CharsetUtil.UTF_8);
        encoded.release();
        return encodedString;
    }

    /**
     * Creates an arbitrary number of random bytes
     *
     * @param size the number of random bytes to create
     * @return An array of random bytes
     */
    static byte[] randomBytes(int size) {
        byte[] bytes = new byte[size];

        for (int index = 0; index < size; index++) {
            bytes[index] = (byte) randomNumber(0, 255);
        }

        return bytes;
    }

    /**
     * Generates a pseudo-random number
     *
     * @param minimum The minimum allowable value
     * @param maximum The maximum allowable value
     * @return A pseudo-random number
     */
    static int randomNumber(int minimum, int maximum) {
        return (int) (Math.random() * maximum + minimum);
    }

    /**
     * A private constructor to ensure that instances of this class cannot be made
     */
    private WebSocketUtil() {
        // Unused
    }

    /**
     * @param url
     * @param method
     * @param data
     * @param channelId
     * @description: TODO 初始化并发送请求
     * @author l09655 2022年08月02日
     */
    public static String sendWebSocketRequest(String url, String method, JSONObject data, ChannelId channelId) {
        WebsocketRequest request = new WebsocketRequest();
        request.setRequestURL(url);
        request.setMethod(method);
        request.setCseq(ChannelSupervise.Cseq++);
        if(data != null) {
            request.setData(data);
        }
        String requestStr = JSONObject.toJSONString(request);
        try {
            ChannelHandlerContext ctx = ChannelSupervise.ChannelMap.get(channelId);
            TextWebSocketFrame tws = new TextWebSocketFrame(requestStr);
            //WebSocketUtil.encode(ctx, tws, true);
            ctx.channel().writeAndFlush(tws);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestStr;
    }

    /**
     * @param req
     * @return java.lang.String
     * @description: TODO 获取WebSocket地址
     * @author l09655 2022年07月20日
     */
    public static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaderNames.HOST) + req.uri();
        //System.out.println(location);
        return "ws://" + location;
    }

    public static void closeSession(ChannelId channelId) {
        System.out.println("Close session " + channelId.asLongText());
        final ChannelHandlerContext ctx = ChannelSupervise.ChannelMap.get(channelId);

        // 1000 1000
        //构造一个关闭session的请求
        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(new byte[] {(byte)0x88}));
    }
}
