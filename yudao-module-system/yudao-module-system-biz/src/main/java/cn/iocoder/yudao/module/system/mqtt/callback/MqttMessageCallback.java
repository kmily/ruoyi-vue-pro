package cn.iocoder.yudao.module.system.mqtt.callback;

import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicExportReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import cn.iocoder.yudao.module.system.mqtt.config.MqttProperties;
import cn.iocoder.yudao.module.system.service.mqtttopic.MqttTopicService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消费监听类
 *
 * @author ZanGe
 */
@Slf4j
@Component
public class MqttMessageCallback implements MqttCallbackExtended {
    private MqttClient client;
    @Resource
    private MqttTopicService mqttTopicService;
    @Resource
    private MqttProperties mqttProperties;
    //@Resource
    //private MqttCmdApi mqttCmdApi;

    public void setMqClient(MqttClient mqClient) {
        this.client = mqClient;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("连接断开，等待重新连接...");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //log.warn("MQTT消息发布【{}】", iMqttDeliveryToken.isComplete() ? "成功" : "失败");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        // 消息会执行到这里面，在这里做消费处理
        String content = new String(mqttMessage.getPayload());
        log.info("MQTT接收消息,主题【{}】,QoS【{}】,消息长度【{}】", topic, mqttMessage.getQos(), content.length());
        try {
            // 这里把收到的数据(可以在这里过滤处理垃圾消息)发给自己的解析类来消费消息，如果消费能力差可以做异步处理，保证后续消息不会堵塞
            //mqttCmdApi.mqttCmdAnalyzer(topic, content);
        } catch (Exception e) {
            log.error("消息解析异常：" + new String(mqttMessage.getPayload()));
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUrl) {
        log.info("MQTT消费客户端" + (reconnect ? "重新" : "") + "连接完成,正在加载订阅主题...");
        try {
            int qos = Integer.parseInt(mqttProperties.getDefaultQos());
            String shareTopic = mqttProperties.getShareTopic();
            // 1、订阅配置文件中定义的默认主题
            String[] defaultTopics = mqttProperties.getDefaultTopic().split(",");
            for (String topic : defaultTopics) {
                client.subscribe(shareTopic + topic, qos);
                log.info("订阅默认主题【{}】", shareTopic + topic);
            }
            // 2、订阅数据库中定义的其他主题
            List<MqttTopicDO> topicList = mqttTopicService.getMqttTopicList(new MqttTopicExportReqVO().setStatus(0));
            for (MqttTopicDO mqttTopicDO : topicList) {
                client.subscribe(shareTopic + mqttTopicDO.getSubTopic(), qos);
                log.info("订阅主题({})【{}】", mqttTopicDO.getTopicName(), shareTopic + mqttTopicDO.getSubTopic());
            }
            log.info("MQTT消费客户端【{}】订阅成功√,共计【{}】条", client.getClientId(), defaultTopics.length + topicList.size());
        } catch (Exception e) {
            log.error("订阅主题异常");
        }
    }
}