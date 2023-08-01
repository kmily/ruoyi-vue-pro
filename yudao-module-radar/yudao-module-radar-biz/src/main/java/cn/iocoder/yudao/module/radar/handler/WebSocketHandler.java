package cn.iocoder.yudao.module.radar.handler;


import cn.iocoder.yudao.module.radar.bean.entity.Device;
import cn.iocoder.yudao.module.radar.bean.enums.ResponseCodeEnum;
import cn.iocoder.yudao.module.radar.bean.enums.WebSocketApiEnum;
import cn.iocoder.yudao.module.radar.bean.request.SubscriptionRequest;
import cn.iocoder.yudao.module.radar.bean.request.WebsocketRequest;
import cn.iocoder.yudao.module.radar.bean.response.SubscriptionResponse;
import cn.iocoder.yudao.module.radar.bean.response.WebsocketResponse;
import cn.iocoder.yudao.module.radar.service.ApiSubThread;
import cn.iocoder.yudao.module.radar.service.ChannelSupervise;
import cn.iocoder.yudao.module.radar.utils.CustomWebSocketServerHandshaker;
import cn.iocoder.yudao.module.radar.utils.CustomWebSocketServerHandshakerFactory;
import cn.iocoder.yudao.module.radar.utils.WebSocketUtil;
import cn.iocoder.yudao.module.radar.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger LOGGER = LogManager.getLogger(WebSocketHandler.class.getName());

    private CustomWebSocketServerHandshaker handshaker;


    private int registerType;


    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public WebSocketHandler( int registerType) {
        this.registerType = registerType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //后续需要重定向操作，demo暂不考虑
        try {
            if (msg instanceof FullHttpRequest) {
                //处理http消息
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            } else if (msg instanceof WebSocketFrame) {
                //处理websocket消息
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        } catch (Exception e) {
           log.error("[NETTY] 请求出现异常 ", e);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.info("客户端加入连接：{}", ctx.channel());
        ChannelSupervise.addChannel(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.info("客户端断开连接：{}", ctx.channel());
        ChannelSupervise.channelSubTimeMap.remove(ctx.channel().id());
        ChannelSupervise.removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * @param ctx
     * @param frame
     * @description: TODO WebSocket请求处理
     * @author l09655 2022年07月19日
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        Channel channel = ctx.channel();
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(channel, (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            channel.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        //请求和响应
        String requestStr = ((TextWebSocketFrame) frame).text();

        if(log.isDebugEnabled()){
            log.debug("[NETTY] 请求数据为 {}", requestStr);
        }

        if (requestStr.contains("RequestURL")) {
            //初始化响应
            WebsocketResponse response = initHandledApiResponse(requestStr, channel.id());
            String resStr = "";
            if (StringUtils.isNotEmpty(response.getResponseURL())) {
                resStr = JSONObject.toJSONString(response);
                TextWebSocketFrame tws = new TextWebSocketFrame(resStr);
                // 群发
                //ChannelSupervise.send2All(tws);
                // 返回
                channel.writeAndFlush(tws);
            }
        } else if (requestStr.contains("ResponseURL")) {
            //如果请求字符串中包含"ResponseURL"，说明是响应返回
            WebsocketResponse response = JSON.parseObject(requestStr.trim(), WebsocketResponse.class);
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));

            if (response.getResponseURL().contains(WebSocketApiEnum.SUBSCRIPTION.url)) {
                // 订阅响应接口
                if(null != data.get("ID")) {
                    log.info(ctx.channel().id() + "-" + df.format(new Date()) + "订阅数据接口返回" + requestStr + "\n");
                    SubscriptionResponse subscriptionResponse = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), SubscriptionResponse.class);
                    long ID = subscriptionResponse.getId();
                    ChannelSupervise.channelDeviceMap.get(channel.id()).setId(ID);
                    ChannelSupervise.channelSubTimeMap.put(channel.id(), System.currentTimeMillis());
                } else {
                    if(null != data.get("CurrentTime")){
                        log.info(ctx.channel().id() + "-" + df.format(new Date()) + "刷新订阅接口返回" + requestStr + "\n");
                        ChannelSupervise.channelSubTimeMap.put(channel.id(), System.currentTimeMillis());
                    } else {
                        log.info(ctx.channel().id() + "-" + df.format(new Date()) + "停止订阅接口返回" + requestStr + "\n");
                        ChannelSupervise.channelSubTimeMap.put(channel.id(), 0L);
                    }

                }
            }
        }

    }

    /**
     * @param requestStr
     * @return bean.response.WebsocketResponse
     * @description: TODO 初始化已处理的请求响应
     * @author l09655 2022年08月08日
     */
    public WebsocketResponse initHandledApiResponse(String requestStr, ChannelId channelId) {
        WebsocketRequest request = JSON.parseObject(requestStr.trim(), WebsocketRequest.class);
        WebsocketResponse response = new WebsocketResponse();
        WebSocketApiEnum webSocketApiEnum = WebSocketApiEnum.getApiByUrl(request.getRequestURL());

        log.info("请求路径：{}", request.getRequestURL());

        log.info("请求数据为{}", requestStr);

        try {

            if (WebSocketApiEnum.KEEPALIVE.url.equals(request.getRequestURL())) {
                log.info(channelId + "-" + df.format(new Date()) + "收到保活请求" + request + "\n");
                //返回消息体
                JSONObject data = new JSONObject();
                //协议要求，精确到秒
                data.put("Timestamp", System.currentTimeMillis()/1000);
                data.put("Timeout", 60);
                response = initResponse(request, response, ResponseCodeEnum.SUCCEED, data);
            } else if (WebSocketApiEnum.REAL_TIME_INFO.url.equals(request.getRequestURL())) {

                LOGGER.info("RealTimeInfo 获取数据为：{}", requestStr);

                //暂不支持，防止日志打印过多
                //response = initResponse(request, response, ResponseCodeEnum.NOT_SUPPORTED, ResponseCodeEnum.NOT_SUPPORTED_MSG);
            } else if (WebSocketApiEnum.ALARM.url.equals(request.getRequestURL())) {
                //暂不支持，防止日志打印过多
                LOGGER.info("Alarm 获取数据为：{}", requestStr);
            }else {
                if (null != webSocketApiEnum) {
                    String text = channelId + "-" + df.format(new Date()) + webSocketApiEnum.apiName + "收到数据" + requestStr;

                    //如果是雷达上报事件或结构化数据，界面日志打印替换BASE64流
                    //实际使用时应将json字符串转换为实体类或JSONObject后替换字段属性
                    if(WebSocketApiEnum.RADAR_DETECTION.url.equals(request.getRequestURL()) || WebSocketApiEnum.STRUCTURE_DATA.url.equals(request.getRequestURL())) {
                        //从第二个Data字段开始遍历，第一个Data字段一定在最外层
                        int indexTemp = StringUtils.ordinalIndexOf(requestStr, "\"Data\":", 2);
                        int dataNum = 2;
                        while(indexTemp != -1) {
                            //截取"Data":后的第一个"
                            int indexStart = StringUtils.indexOf(requestStr, "\"", indexTemp + 6);
                            //截取"Data":后的第二个"
                            int indexEnd = StringUtils.indexOf(requestStr, "\"", indexStart+1);
                            String base64 = requestStr.substring(indexStart+1, indexEnd);
                            //日志打印时将base64流转换为"base64"字符串
                            if(StringUtils.isNotEmpty(base64)) {
                                requestStr = requestStr.replace(base64, "BASE64");
                            }
                            //替换完毕后取下一个Data字段
                            dataNum++;
                            indexTemp = StringUtils.ordinalIndexOf(requestStr, "\"Data\":", dataNum);
                        }
                    }
                    String textNoBase64 = channelId + "-" + df.format(new Date()) + webSocketApiEnum.apiName + "收到数据" + requestStr;

                    //String getMethodName = webSocketApiEnum.textAreaGetMethodName;
                    //获取要执行的get方法名
                   // Method method = textAreasLog.getClass().getMethod(getMethodName);
                    //过车替换为中文
                   // textNoBase64 = replaceENG2CHN(textNoBase64, getMethodName);
                    //反射执行get方法，界面日志不打印base64流
                    //TextAreaLogUtil.printLog(textNoBase64, (JTextArea) method.invoke(textAreasLog));

                    //结构化数据接口，需要response
                    if(WebSocketApiEnum.STRUCTURE_DATA.url.equals(request.getRequestURL())) {
                        JSONObject data = new JSONObject();
                        //替换占位符，否则解析报错
                        JSONObject requestJsonObject = JSONObject.parseObject(requestStr.trim());
                        JSONObject requestDataJsonObject = requestJsonObject.getJSONObject("Data");

                        data.put("NotificationType", requestDataJsonObject.get("NotificationType"));
                        data.put("RelatedID", requestDataJsonObject.get("RelatedID"));
                        response = initResponse(request, response, ResponseCodeEnum.SUCCEED, data);
                    }

                    //人体康养数据业务接口，需要response
                    if(WebSocketApiEnum.HEALTH_DATA.url.equals(request.getRequestURL())) {
                        Object requestData = request.getData();


                        JSONObject data = new JSONObject();
                        response = initResponse(request, response, ResponseCodeEnum.SUCCEED, data);
                    }
                    //response = initResponse(request, response, ResponseCodeEnum.SUCCEED, ResponseCodeEnum.SUCCEED_MSG);
                    //log4j日志打印base64流
                    LOGGER.log(webSocketApiEnum.logLevel, text);
                } else {
                    //response = initResponse(request, response, ResponseCodeEnum.NOT_SUPPORTED, ResponseCodeEnum.NOT_SUPPORTED_MSG);
                    log.info(channelId + "请求未做处理:" + request + "\n");
                }
            }

        } catch (Exception e) {
            log.info("调用方法异常：" + e);
        }
        return response;
    }


    /**
     * @param text 要替换的文本
     * @param getMethodName get方法名称
     * @return java.lang.String
     * @description: TODO 通过get方法名称判断当前打印的日志类型，并将英文字段替换为中文
     * @author l09655 2022年11月28日
     */
    public String replaceENG2CHN(String text, String getMethodName) {
        switch (getMethodName) {
            case "getTextAreaLogPassData":
                /*text = text.replaceAll("DeviceID","设备编码");
                text = text.replaceAll("CurrentTime","当前时间");
                text = text.replaceAll("Seq","消息序号");
                text = text.replaceAll("SourceID","告警源ID");
                text = text.replaceAll("LaneID","车道编号");
                text = text.replaceAll("CoilID","线圈编号");
                text = text.replaceAll("Speed","目标离开线圈时的速度");
                text = text.replaceAll("VehicleLength","车长");
                text = text.replaceAll("VehicleType","车辆类型");
                text = text.replaceAll("DriveIntoTime","目标进入线圈时间");
                text = text.replaceAll("PresenceTime","压占时间");*/
            default:
        }
        return text;
    }

    /**
     * @param ctx
     * @param req
     * @description: TODO HTTP请求处理，用于创建websocket连接
     * @author l09655 2022年07月19日
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //过滤普通的http请求，以及处理异常的请求
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST), new JSONObject());
            return;
        }
        if (req.uri().contains(WebSocketApiEnum.REGISTER.url)) {
            /**
             * 判断注册类型，1有鉴权方式，2无鉴权方式
             */
            Device device = null;
            Map<String, String> params = cn.iocoder.yudao.module.radar.utils.HttpUtil.getRequestParams(req);
            if(log.isDebugEnabled()){
                log.debug("注册参数：{}", params);
            }
            if (1 == registerType) {
                //第一种注册方式，先判断是否第一次连接，第一次连接时不携带验证信息，返回401
                device = checkIfFirst(params);
                if (device == null) {
                    sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED), new JSONObject());
                    return;
                } else {
                    //如果不是第一次，建立websocket连接
                    handshaker(ctx, req, device);
                }
            } else if (2 == registerType) {
                device = new Device();
                //第二种注册方式，不做验证，直接建立连接
                handshaker(ctx, req, device);
            }
        }
    }


    /**
     * 进行握手操作
     * @param ctx
     * @param req
     */
    private void handshaker(ChannelHandlerContext ctx, FullHttpRequest req, Device device){
        CustomWebSocketServerHandshakerFactory wsFactory = new CustomWebSocketServerHandshakerFactory( WebSocketUtil.getWebSocketLocation(req), null, true, 65536 * 160);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
            //可能注册成功的设备存储IP
            String address = ctx.channel().remoteAddress().toString();
            device.setIp(address.substring(address.indexOf("/") + 1, address.indexOf(":")));
            ChannelSupervise.channelDeviceMap.put(ctx.channel().id(), device);

            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

            /**
             * 如果订阅状态为开启，直接发起订阅，否则只存储
             */
            if (1 == ApiSubThread.apiSubStatus) {
                String requestStr = "";
                //如果最近发送订阅时间为0，代表没有发过，立即发送订阅数据请求
                SubscriptionRequest subscriptionRequest = ApiSubThread.initSubscriptionRequest(ctx.channel().id());
                JSONObject data = JSON.parseObject(JSON.toJSONString(subscriptionRequest));
                ChannelSupervise.channelDeviceMap.get(ctx.channel().id()).setSubDuration(subscriptionRequest.getDuration());
                //发送订阅请求
                requestStr = cn.iocoder.yudao.module.radar.utils.WebSocketUtil.sendWebSocketRequest(WebSocketApiEnum.SUBSCRIPTION.url, "POST", data, ctx.channel().id());
                log.info(ctx.channel().id() + "发送订阅请求" + requestStr + "\n");
                ChannelSupervise.channelSubTimeMap.put(ctx.channel().id(), System.currentTimeMillis());
                log.info("---------------------------\n");
            } else {
                ChannelSupervise.channelSubTimeMap.put(ctx.channel().id(), 0L);
            }
        }
    }


    /**
     * @param ctx
     * @param req
     * @param res
     * @param jsonObject
     * @description: TODO 拒绝不合法的请求返回错误信息，判断合法请求返回数据
     * @author l09655 2022年07月19日
     */
    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest req, DefaultFullHttpResponse res, JSONObject jsonObject) {
        Map<String, String> params = HttpUtil.getRequestParams(req);
        HttpHeaders headers = res.headers();

        // 返回应答给客户端
        switch (res.status().code()) {
            case 400:
                //参数不合法处理
                break;
            case 401:
                //验证失败处理
                //后续可能需要添加的请求头
                headers.set("Content-Type", "text/html;Charset=utf-8");
                headers.set("Status", "401");
                headers.set("Connection", "close");
                headers.set("X-Frame-Options", "SAMEORGINr");
                headers.set("X-Content-Type-Options", "nosniff");
                headers.set("X-XSS-Protection", "1; mode=block");
                JSONObject response = new JSONObject();
                response.put("ResponseURL", "/LAPI/V1.0/System/UpServer/Register");
                response.put("ResponseString", "Not Authorized");
                response.put("StatusCode", "401");
                JSONObject data = new JSONObject();
                data.put("Nonce", WebSocketUtil.generateNonce());
                response.put("Data", data);
                jsonObject.put("Response", response);
                break;
            case 302:
                //重定向处理，demo暂不需要
                //请求头设置新的地址
                headers.set(HttpHeaderNames.LOCATION, "");
                if (registerType == 1) {
                    //jsonObject.put("Cnonce", params.get("Cnonce"));
                    String algorithm = params.get("Algorithm");
                    jsonObject.put("Resign", WebSocketUtil.generateResign(algorithm));
                }
                break;
            case 200:
                //请求成功处理，手动拼接response，无法建立websocket连接，目前用不到
                /*if (registerType == 1) {
                    String algorithm = params.get("Algorithm");
                    jsonObject.put("Resign", generateResign(algorithm));
                    res.setStatus(HttpResponseStatus.SWITCHING_PROTOCOLS);

                    headers.set("upgrade", "websocket");
                    headers.set("connection", "upgrade");
                    headers.set("sec-websocket-accept", keyToAccept(req.headers().get("Sec-WebSocket-Key")));
                    headers.set("x-frame-options", "SAMEORIGIN");
                } else if (registerType == 2) {
                    jsonObject.put("Nonce", params.get("Nonce"));
                    res.setStatus(HttpResponseStatus.SWITCHING_PROTOCOLS);
                }*/
                break;
        }

        //如果参数不为空，写入
        if (!jsonObject.isEmpty()) {
            ByteBuf jsonBuf = Unpooled.copiedBuffer(JSON.toJSONString(jsonObject), CharsetUtil.UTF_8);
            res.content().writeBytes(jsonBuf);
            jsonBuf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);

        // 如果是非Keep-Alive，关闭连接
        /*if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }*/
    }


    /**
     * @param params
     * @return boolean
     * @description: TODO 判断是否第一次发送注册请求，并存储可能成功的当前设备信息
     * @author l09655 2022年07月19日
     */
    private Device checkIfFirst(Map<String, String> params) {
        Device device = new Device();
        if (StringUtils.isEmpty(params.get("Vendor"))) {
            return null;
        } else {
            device.setVendor(params.get("Vendor"));
        }
        if (StringUtils.isEmpty(params.get("DeviceType"))) {
            return null;
        } else {
            device.setDeviceType(params.get("DeviceType"));
        }
        if (StringUtils.isEmpty(params.get("DeviceCode"))) {
            return null;
        } else {
            device.setDeviceCode(params.get("DeviceCode"));
        }
        if (StringUtils.isEmpty(params.get("Sign"))) {
            return null;
        } else {
            device.setSign(params.get("Sign"));
        }
        return device;
    }


    /**
     * @param key
     * @return java.lang.String
     * @description: TODO 计算sec-websocket-accept，用于手动拼接response的header
     * @author l09655 2022年07月20日
     */
    private static String keyToAccept(String key) {
        try {
            String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            key = key + guid;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(key.getBytes(StandardCharsets.ISO_8859_1), 0, key.length());
            byte[] sha1Hash = digest.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(sha1Hash);
        } catch (Exception e) {
            log.error("计算 [sec-websocket-accept] 出现异常 {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param ctx
     * @param cause
     * @description: TODO 连接异常处理
     * @author l09655 2022年07月20日
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[NETTY] {} 连接出现异常", ctx.channel().id(), cause);
    }


    /**
     * @param request 请求
     * @param response 响应
     * @param responseCodeEnum 状态
     * @return bean.response.WebsocketResponse
     * @description: TODO 初始化响应消息体(不带data参数）
     * @author l09655 2022年08月08日
     */
    private WebsocketResponse initResponse(WebsocketRequest request, WebsocketResponse response, ResponseCodeEnum responseCodeEnum) {
        response.setResponseURL(request.getRequestURL());
        //响应的cseq与请求保持一致
        response.setCseq(request.getCseq());
        response.setResponseCode(responseCodeEnum.code);
        response.setResponseString(responseCodeEnum.msg);
        return response;
    }


    /**
     * @param request 请求对象
     * @param response 响应对象
     * @param responseCodeEnum 状态吗
     * @param data 响应数据
     * @return bean.response.WebsocketResponse
     * @description: TODO 初始化成功响应消息体（带data参数）
     * @author l09655 2022年08月08日
     */
    private WebsocketResponse initResponse(WebsocketRequest request, WebsocketResponse response, ResponseCodeEnum responseCodeEnum,  JSONObject data) {
        response.setResponseURL(request.getRequestURL());
        //响应的cseq与请求保持一致
        response.setCseq(request.getCseq());
        response.setResponseCode(responseCodeEnum.code);
        response.setResponseString(responseCodeEnum.msg);
        response.setData(data);
        return response;
    }

}
