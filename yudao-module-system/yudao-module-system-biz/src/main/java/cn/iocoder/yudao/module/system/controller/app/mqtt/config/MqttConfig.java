package cn.iocoder.yudao.module.system.controller.app.mqtt.config;

import cn.iocoder.yudao.module.system.controller.app.mqtt.client.MqttOperateClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mqtt相关配置信息
 *
 * @author Hermit
 */
@Data
@Component
@ConfigurationProperties("spring.mqtt")
public class MqttConfig {

    @Resource
    private MqttOperateClient mqttOperateClient;

    /**
     * MQTT登录名
     */
    private String username;

    /**
     * MQTT登录密码
     */
    private String password;

    /**
     * MQTT连接地址
     */
    private String url;

    /**
     * MQTT会话心跳时间
     */
    private int keepAliveInterval;
    /**
     * clientId前缀
     */
    private String clientId;
    /**
     * 消费者订阅主题（消费者）
     */
    private String defaultTopic;

    /**
     * 消费者订阅主题设置服务质量（消费者）
     */
    private String defaultQos;

    /**
     * 消费者操作完成的超时时长（消费者）
     */
    private int completionTimeout;

    @Bean
    public MqttOperateClient getMqttOperateClient() {

        String randomClientId = clientId + "_" + System.currentTimeMillis();

        // 连接
        mqttOperateClient.connect(url, clientId, username, password, completionTimeout, keepAliveInterval);
        // 订阅
        subscribe();
        return mqttOperateClient;
    }

    /**
     * 订阅消息
     */
    public void subscribe() {
        // 订阅设备信息
        mqttOperateClient.subscribe("device_info", 1);
        // 订阅设备状态
        mqttOperateClient.subscribe("status", 1);
        // 订阅设备配置
        mqttOperateClient.subscribe("setting", 1);
        // 订阅设备离线遗嘱
        mqttOperateClient.subscribe("offline", 1);
        // 订阅CMD指令消息
        mqttOperateClient.subscribe("cmd", 1);

        // 主题集合
//        String[] topics = StringUtils.split(defaultTopic, ",");
//        // 服务质量集合
//        String[] qos = StringUtils.split(defaultQos, ",");
//
//        // 服务质量集合
//        int[] qosArray = new int[qos.length];
//
//        for (int i = 0; i < qos.length; i++) {
//            qosArray[i] = Integer.parseInt(qos[i]);
//        }
//        // 批量订阅
//        mqttOperateClient.subscribe(topics, qosArray);
    }

}

