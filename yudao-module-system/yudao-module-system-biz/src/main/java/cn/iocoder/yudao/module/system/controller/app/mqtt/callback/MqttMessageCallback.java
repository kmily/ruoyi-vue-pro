package cn.iocoder.yudao.module.system.controller.app.mqtt.callback;

import cn.iocoder.yudao.module.system.controller.app.mqtt.config.MqttConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费监听类
 *
 * @author Hermit
 */
@Slf4j
@Component
public class MqttMessageCallback implements MqttCallback {

    @Resource
    private MqttConfig mqttConfig;

    private static MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
        log.error("连接断开，正在尝试重新连接 >>>>>>>");
        if (client == null || !client.isConnected()) {
            // 重新连接并订阅
            mqttConfig.getMqttOperateClient();
        }
//        try {
//            client.reconnect();
//            mqttConfig.subscribe();
//        } catch (MqttException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.debug("send success ? --> {}", iMqttDeliveryToken.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        // subscribe后得到的消息会执行到这里面

        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + mqttMessage.getQos());
        log.info("接收消息内容 : " + new String(mqttMessage.getPayload()));

        try {
            JSONObject jsonObject = JSON.parseObject(new String(mqttMessage.getPayload()));
        } catch (Exception e) {
            log.error("未知消息格式,请发布和订阅两端约定好消息体之后操作：" + new String(mqttMessage.getPayload()));
            return;
        }

        // 处理自己的业务
        log.info("自己的业务已处理完毕...");
    }

}
