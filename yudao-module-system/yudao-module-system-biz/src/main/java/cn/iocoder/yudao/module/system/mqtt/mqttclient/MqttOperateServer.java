package cn.iocoder.yudao.module.system.mqtt.mqttclient;


import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.system.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mqtt推送客户端
 *
 * @author ZanGe
 */
@Slf4j
@Component
@Primary
public class MqttOperateServer {

    public static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttOperateServer.client = client;
    }

    private static MqttConnectOptions options;
    @Resource
    private MqttProperties mqttProperties;


    /**
     * MQTT连接器
     */
    private void getMqttConnectOptions() {
        options = new MqttConnectOptions();
        try {
            // 设置连接的用户名
            options.setUserName(mqttProperties.getUsername());
            // 设置连接的密码
            options.setPassword(mqttProperties.getPassword().toCharArray());
            // 设置连接的地址
            options.setServerURIs(StrUtil.splitToArray(mqttProperties.getUrl(), ","));
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(mqttProperties.getCompletionTimeout());
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            // 但这个方法并没有重连的机制
            options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
            // 设置 "遗嘱" 消息的话题,并保留，若客户端与服务器之间的连接意外中断，服务器将发布客户端的"遗嘱"消息。
            //options.setWill("willTopic", WILL_DATA, 2, true);
            // 设置清除会话
            options.setCleanSession(true);
            // 断开后重连，但这个方法并没有重新订阅的机制
            // 在尝试重新连接之前，它将首先等待1秒，对于每次失败的重新连接尝试，延迟将加倍，直到达到2分钟，此时延迟将保持在2分钟。
            options.setAutomaticReconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void connect() {
        try {
            String randomClientId = mqttProperties.getClientId() + "-server-" + System.currentTimeMillis();
            log.info("MQTT生产客户端【{}】正在初始化连接【{}】", randomClientId, mqttProperties.getUrl());
            client = new MqttClient(mqttProperties.getUrl(), randomClientId, new MemoryPersistence());
            // MQTT连接器选项
            getMqttConnectOptions();
            // 判断拦截状态，这里注意一下
            if (!client.isConnected()) {
                client.connect(options);
            } else {
                log.warn("MQTT生产客户端已连接");
                return;
            }
            MqttOperateServer.setClient(client);
            log.info("MQTT生产客户端连接成功√");
        } catch (Exception e) {
            log.info("MQTT生产客户端连接失败×");
        }
    }

    /**
     * 消息发布
     *
     * @param qos         服务质量
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public boolean publish(String topic, @NotNull String pushMessage, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        // 从连接中获取某个主题
        MqttTopic mqttTopic = MqttOperateServer.getClient().getTopic(topic);
        // 判断主题是否存在
        if (null == mqttTopic) {
            log.error("主题不存在!!!");
            return false;
        }
        MqttDeliveryToken token;
        try {
            // 发布消息
            token = mqttTopic.publish(message);
            token.waitForCompletion();
            log.info("{}指令发送【{}】", mqttTopic.getName(), token.isComplete() ? "成功" : "失败");
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 消息发布
     *
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public boolean publish(String topic, String pushMessage) {
        return publish(topic, pushMessage, 1, false);
    }
}