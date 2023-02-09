package cn.iocoder.yudao.module.system.api.mqtt;

import cn.iocoder.yudao.module.system.mqtt.mqttclient.MqttOperateClient;
import cn.iocoder.yudao.module.system.mqtt.mqttclient.MqttOperateServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MQTT消息发布 API 实现类
 *
 * @author ZanGe
 * @create 2023/2/2 11:16
 */
@Service
public class MqttApiImpl implements MqttApi {
    @Resource
    private MqttOperateServer mqttOperateServer;
    @Resource
    private MqttOperateClient mqttOperateClient;

    /**
     * MQTT消息发布
     *
     * @param topic       主题
     * @param pushMessage 消息体
     * @return 结果
     */
    @Override
    public boolean publishMsg(String topic, String pushMessage) {
        return mqttOperateServer.publish(topic, pushMessage);
    }

    /**
     * MQTT消息发布
     *
     * @param topic       主题
     * @param pushMessage 消息体
     * @param qos         服务质量
     * @param retained    是否保留
     * @return 结果
     */
    @Override
    public boolean publishMsg(String topic, String pushMessage, int qos, boolean retained) {
        return mqttOperateServer.publish(topic, pushMessage, qos, retained);
    }

    /**
     * 订阅主题
     *
     * @param topic 主题
     * @param qos   服务质量
     */
    @Override
    public void subscribe(String topic, int qos) {
        mqttOperateClient.subscribe(topic, qos);
    }

    /**
     * 批量订阅多主题
     *
     * @param topics 主题数组
     * @param qos    服务质量
     */
    @Override
    public void subscribe(String[] topics, int[] qos) {
        mqttOperateClient.subscribe(topics, qos);
    }
}
