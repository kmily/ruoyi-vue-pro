package cn.iocoder.yudao.module.system.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * MQTT 配置项
 *
 * @author ZanGe
 */
@Validated
@Component
@Data
@ConfigurationProperties("spring.mqtt")
public class MqttProperties {

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
     * clientId
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

    /**
     * 共享订阅主题分组
     */
    private String shareTopic;
}
