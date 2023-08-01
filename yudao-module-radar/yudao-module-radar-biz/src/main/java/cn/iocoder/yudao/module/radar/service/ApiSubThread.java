package cn.iocoder.yudao.module.radar.service;

import cn.iocoder.yudao.module.radar.bean.enums.WebSocketApiEnum;
import cn.iocoder.yudao.module.radar.bean.request.SubscribeRadarCondition;
import cn.iocoder.yudao.module.radar.bean.request.SubscribeVehicleCondition;
import cn.iocoder.yudao.module.radar.bean.request.SubscriptionRequest;
import cn.iocoder.yudao.module.radar.utils.WebSocketUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by l09655 on 2022/7/28.
 * 订阅数据线程
 */
@Slf4j
public class ApiSubThread implements Runnable {

    /**
     * 订阅接口状态
     */
    public static int apiSubStatus = 0;

    /**
     * 订阅类型
     */
    public static long subType = 0b0000000000000000000000000000000;

    /**
     * 订阅图片类型
     * 0：BASE64（默认）
     * 1：本地URL
     * 2：云存储URL
     */
    public static Long pushImageType = 0L;
    /**
     * 订阅车辆交通数据类型
     */
    public static long subTypeFlowRate = 0b0000000000000000000000000000000;

    private static int currPort;

    private static String currIp;

    @Override
    public void run() {
        /**
         * 遍历注册成功的map，key为channelId，value为最近一次订阅时间戳，未发起订阅的value为0
         * 这次遍历处理的是开启对接但未开启订阅期间注册成功的设备
         */
        for (Map.Entry<ChannelId, Long> entry : ChannelSupervise.channelSubTimeMap.entrySet()) {
            String requestStr = "";
            ChannelId channelId = entry.getKey();
            //最近一次发起订阅时间戳
            long lastSubTimeStamp = entry.getValue();
            if (0 == lastSubTimeStamp) {
                //如果最近发送订阅时间为0，代表没有发过，立即发送订阅数据请求
                SubscriptionRequest subscriptionRequest = initSubscriptionRequest(channelId);
                JSONObject data = JSON.parseObject(JSON.toJSONString(subscriptionRequest));

                //发送订阅请求
                ChannelSupervise.channelDeviceMap.get(channelId).setSubDuration(subscriptionRequest.getDuration());
                requestStr = WebSocketUtil.sendWebSocketRequest(WebSocketApiEnum.SUBSCRIPTION.url, "POST", data, channelId);
                log.info(channelId + "发送订阅请求" + requestStr + "\n");
                entry.setValue(System.currentTimeMillis());
            }
        }
        //进入刷新订阅循环
        refreshSub();

    }

    /**
     * @description: TODO 循环刷新订阅，每次循环间隔一段时间节省资源
     * @author l09655 2022年08月08日
     */
    public void refreshSub() {
        while (apiSubStatus == 1) {
            /**
             * 遍历注册成功的map，key为channelId，value为最近一次订阅时间戳，未发起订阅的value为0
             * 这次遍历处理的是开启订阅后注册成功的设备
             */
            for (Map.Entry<ChannelId, Long> entry : ChannelSupervise.channelSubTimeMap.entrySet()) {
                String requestStr = "";
                ChannelId channelId = entry.getKey();
                //刷新订阅的时间点，订阅周期时长的85%
                double freshSubLimit = ChannelSupervise.channelDeviceMap.get(channelId).getSubDuration() * 0.85 * 1000;
                //最近订阅时间点
                long lastSubTimeStamp = entry.getValue();
                if (System.currentTimeMillis() - lastSubTimeStamp >= freshSubLimit) {
                    //如果最近订阅时间与当前时间的间隔，超过订阅周期时长的85%，刷新订阅
                    JSONObject data = new JSONObject();
                    /**
                     * 订阅周期，后续可能需要做可配，目前写死3600s
                     */
                     data.put("Duration", 3600L);
                     long ID = ChannelSupervise.channelDeviceMap.get(channelId).getId();
                     ChannelSupervise.channelDeviceMap.get(channelId).setSubDuration((Long) data.get("Duration"));
                     //发送续订请求
                     requestStr = WebSocketUtil.sendWebSocketRequest(WebSocketApiEnum.SUBSCRIPTION.url + "/" + ID, "PUT", data, channelId);

                    log.info(channelId + "即将到达保活时间，发送续订请求" + requestStr + "\n");
                     //存储当前时间戳作为最后订阅时间
                     entry.setValue(System.currentTimeMillis());
                    log.info("---------------------------\n");
                }

            }

            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
               log.error("循环订阅休息出现异常");
            }
        }
    }

    /**
     * @description: TODO 停止订阅
     * @author l09655 2022年08月02日
     */
    public static void stopSub() {
        apiSubStatus = 0;
        for (Map.Entry<ChannelId, Long> entry : ChannelSupervise.channelSubTimeMap.entrySet()) {
            Long recentTimestamp = entry.getValue();
            //最近订阅时间不为0的，发送停止订阅请求
            if (0 != recentTimestamp) {
                ChannelId channelId = entry.getKey();
                //取消订阅
                long ID = ChannelSupervise.channelDeviceMap.get(channelId).getId();
                String requestStr = WebSocketUtil.sendWebSocketRequest(WebSocketApiEnum.SUBSCRIPTION.url + "/" + ID, "DELETE", new JSONObject(), channelId);
                log.info(channelId + "发送停止订阅请求" + requestStr + "\n");
                log.info(channelId + "数据订阅已暂停\n");
            }
        }
    }

    /**
     * @param channelId
     * @return bean.request.SubscriptionRequest
     * @description: TODO 初始化订阅请求消息体
     * @author l09655 2022年08月05日
     */
    public static SubscriptionRequest initSubscriptionRequest(ChannelId channelId) {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setDeviceID(ChannelSupervise.channelDeviceMap.get(channelId).getDeviceCode());
        subscriptionRequest.setIpAddress(currIp);
        subscriptionRequest.setPort(currPort);
        subscriptionRequest.setType(subType);

        if (0 != (subType & 0b0000000000000000000100000000000) || 0 != (subType & 0b0000000000000000000000000100000)) {
            //第11位是1，说明是订阅车辆数据类型，第5位是1，说明是订阅结构化数据类型，需要细分订阅数据类型
            SubscribeVehicleCondition subscribeVehicleCondition = new SubscribeVehicleCondition();
            /**
             * 车辆交通数据：
             * Bit2：区域&统计&排队流量
             * Bit3：即时&过车数量
             * Bit4：事件数据
             * 结构化数据：
             * Bit5：结构化过车数据
             * Bit6：结构化违法数据
             */
            subscribeVehicleCondition.setType(subTypeFlowRate);
            if (0 != (subTypeFlowRate & 0b0000000000000000000000000010000) || 0 != (subTypeFlowRate & 0b0000000000000000000000000100000) || 0 != (subTypeFlowRate & 0b0000000000000000000000001000000)) {
                //第4位是1说明是订阅车辆数据类型，第5位是1说明是订阅结构化过车数据类型，第6位是1说明是结构化违法数据类型，需要增加订阅图片类型
                subscriptionRequest.setImagePushMode(pushImageType);
            } else {
                pushImageType = null;
                subscriptionRequest.setImagePushMode(pushImageType);
            }
            subscriptionRequest.setSubscribeVehicleCondition(subscribeVehicleCondition);



        }

        if (0 != (subType & 0b0000000000000100000000000000000) || 0 != (subType & 0b0000000000000000000000000100000)){
            SubscribeRadarCondition subscribeRadarCondition = new SubscribeRadarCondition();
            subscribeRadarCondition.setType(0b0000000000000000000000000000111);
            subscriptionRequest.setSubscribeRadarCondition(subscribeRadarCondition);
        }



        /**
         * 订阅周期，后续可能需要做可配，目前写死3600s
         */
        subscriptionRequest.setDuration(3600L);
        return subscriptionRequest;
    }


    public ApiSubThread( int currPort, String currIp) {
        ApiSubThread.currPort = currPort;
        ApiSubThread.currIp = currIp;
    }

}
