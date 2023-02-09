package cn.iocoder.yudao.module.system.mqtt.config;

import cn.iocoder.yudao.module.system.mqtt.mqttclient.MqttOperateClient;
import cn.iocoder.yudao.module.system.mqtt.mqttclient.MqttOperateServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mqtt相关配置
 *
 * @author ZanGe
 */
@Component
@ConditionalOnProperty(prefix = "spring.mqtt", value = "enable", matchIfMissing = true)
public class MqttConfiguration {

    @Resource
    private MqttOperateClient mqttOperateClient;
    @Resource
    private MqttOperateServer mqttOperateServer;

    @Bean
    public MqttOperateClient getMqttOperateClient() {
        mqttOperateClient.connect();
        return mqttOperateClient;
    }

    @Bean
    public MqttOperateServer getMqttOperateServer() {
        mqttOperateServer.connect();
        return mqttOperateServer;
    }

}