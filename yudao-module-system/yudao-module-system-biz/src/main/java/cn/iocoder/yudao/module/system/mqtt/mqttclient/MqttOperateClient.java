package cn.iocoder.yudao.module.system.mqtt.mqttclient;


import cn.iocoder.yudao.module.system.mqtt.callback.MqttMessageCallback;
import cn.iocoder.yudao.module.system.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * mqtt消费客户端
 *
 * @author ZanGe
 */
@Slf4j
@Component
public class MqttOperateClient {
    @Resource
    private MqttMessageCallback mqttPushCallback;
    @Resource
    private MqttProperties mqttProperties;
    private MqttConnectOptions options;

    public static MqttClient client;

    /**
     * MQTT连接器选项
     */
    private void getMqttConnectOptions() {
        options = new MqttConnectOptions();
        try {
            // 设置连接的用户名
            options.setUserName(mqttProperties.getUsername());
            // 设置连接的密码
            options.setPassword(mqttProperties.getPassword().toCharArray());
            // 设置连接的地址
            options.setServerURIs(StringUtils.split(mqttProperties.getUrl(), ","));
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

    /**
     * 客户端连接
     */
    public synchronized void connect() {
        try {
            String randomClientId = mqttProperties.getClientId() + "-client-" + System.currentTimeMillis();
            log.info("MQTT消费客户端【{}】正在初始化连接【{}】", randomClientId, mqttProperties.getUrl());
            getMqttConnectOptions();
            if (client == null) {
                client = new MqttClient(mqttProperties.getUrl(), randomClientId, new MemoryPersistence());
                ;
                mqttPushCallback.setMqClient(client);
                client.setCallback(mqttPushCallback);
            }
            // 判断拦截状态，这里注意一下
            if (!client.isConnected()) {
                client.connect(options);
            } else {
                log.warn("MQTT消费客户端已连接！");
                return;
            }
            log.info("MQTT消费客户端初始连接成功√");
        } catch (Exception e) {
            log.error("MQTT消费客户端初始连接失败×");
        }
    }


    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   服务质量
     */
    public void subscribe(String topic, int qos) {
        log.info("开始订阅主题【{}】,Qos【{}】", topic, qos);
        try {
            client.subscribe(topic, qos);
            if (client.getTopic(topic) == null) {
                log.error("主题订阅失败!!!");
                return;
            }
            log.info("订阅主题完成【{}】,Qos【{}】", mqttProperties.getShareTopic() + topic, qos);
        } catch (MqttException e) {
            log.error("主题订阅异常!!!");
            e.printStackTrace();
        }
    }

    /**
     * 订阅多主题
     *
     * @param topics 主题数组
     * @param qos    服务质量
     */
    public void subscribe(String[] topics, int[] qos) {
        log.info("开始批量订阅主题:{}", Arrays.asList(topics));
        try {
            client.subscribe(topics, qos);
            for (int i = 0; i < topics.length; i++) {
                if (client.getTopic(topics[i]) == null) {
                    log.error("主题【{}】订阅失败", topics[i]);
                    ArrayUtils.remove(topics, i);
                }
            }
            log.info("批量订阅主题完成,共计【{}】条：{}", topics.length, Arrays.toString(topics));
        } catch (MqttException e) {
            log.error("主题订阅异常!!!");
            e.printStackTrace();
        }
    }

}