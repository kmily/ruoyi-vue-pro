package cn.iocoder.yudao.module.system.controller.app.mqtt.client;

import cn.iocoder.yudao.module.system.controller.app.mqtt.callback.MqttMessageCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * mqtt推送客户端
 *
 * @author Hermit
 */
@Slf4j
@Component
@Primary
public class MqttOperateClient {

    @Resource
    private MqttMessageCallback mqttPushCallback;

    /**
     * MQTT客户端
     */
    public static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttOperateClient.client = client;
    }

    /**
     * 客户端与服务器之间的连接意外中断，服务器将发布客户端的"遗嘱"消息
     */
    private static final byte[] WILL_DATA;

    static {
        WILL_DATA = "offline".getBytes();
    }

    /**
     * 客户端连接
     * <p>
     * true为非持久订阅
     * 方法实现说明 断线重连方法，如果是持久订阅，重连是不需要再次订阅，如果是非持久订阅，重连是需要重新订阅主题 取决于options.setCleanSession(true);
     * 就是这里的clientId，服务器用来区分用户的，不能重复,clientId不能和发布的clientId一样，否则会出现频繁断开连接和重连的问题
     * 不仅不能和发布的clientId一样，而且也不能和其他订阅的clientId一样，如果想要接收之前的离线数据，这就需要将client的 setCleanSession
     * 设置为false，这样服务器才能保留它的session，再次建立连接的时候，它就会继续使用这个session了。 这时此连接clientId 是不能更改的。
     * 但是其实还有一个问题，就是使用热部署的时候还是会出现频繁断开连接和重连的问题，可能是因为刚启动时的连接没断开，然后热部署的时候又进行了重连，重启一下就可以了
     * System.currentTimeMillis()
     *
     * @param host      ip+端口
     * @param clientId  客户端Id
     * @param username  用户名
     * @param password  密码
     * @param timeout   超时时间
     * @param keepalive 保留数
     * @throws MqttException
     */
    public void connect(String host, String clientId, String username, String password, int timeout, int keepalive) {
        log.info("【MQTT客户端正在初始化】MQTT client init ......host：{}，clientId：{}", host, clientId);

        MqttClient client;
        try {
            // MemoryPersistence设置clientId的保存形式，默认为以内存保存
            client = new MqttClient(host, clientId, new MemoryPersistence());

            // MQTT连接器选项
            MqttConnectOptions options = getMqttConnectOptions(host, username, password, timeout, keepalive);
            // 判断拦截状态，这里注意一下，如果没有这个判断，是非常坑的
            client.connect(options);

            // 如果是订阅者则添加回调类，发布不需要
            client.setCallback(mqttPushCallback);

            MqttOperateClient.setClient(client);
            log.info("【MQTT客户端连接成功√】MQTT client connected success √......");
        } catch (Exception e) {
            log.info("【MQTT客户端连接失败】MQTT client connected fail ✘......");
        }
    }

    /**
     * MQTT连接器选项
     */
    private MqttConnectOptions getMqttConnectOptions(String host, String username, String password, int timeout, int keepalive) {
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置连接的用户名
        if (StringUtils.isNotBlank(username)) {
            options.setUserName(username);
        }
        // 设置连接的密码
        if (StringUtils.isNotBlank(password)) {
            options.setPassword(password.toCharArray());
        }
        // 设置连接的地址
        options.setServerURIs(StringUtils.split(host, ","));
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(timeout);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        // 但这个方法并没有重连的机制
        options.setKeepAliveInterval(keepalive);
        // 设置 "遗嘱" 消息的话题,并保留，若客户端与服务器之间的连接意外中断，服务器将发布客户端的"遗嘱"消息。
        options.setWill("willTopic", WILL_DATA, 2, true);
        // 设置清除会话
        options.setCleanSession(true);
        // 断开后重连，但这个方法并没有重新订阅的机制
        // 在尝试重新连接之前，它将首先等待1秒，对于每次失败的重新连接尝试，延迟将加倍，直到达到2分钟，此时延迟将保持在2分钟。
        options.setAutomaticReconnect(true);
        return options;
    }

    /**
     * 发布
     *
     * @param qos         服务质量
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public boolean publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        // 从连接中获取某个主题
        MqttTopic mqttTopic = MqttOperateClient.getClient().getTopic(topic);
        // 判断主题是否存在
        if (null == mqttTopic) {
            log.error("【主题不存在】topic not exist...");
            return false;
        }
        MqttDeliveryToken token;
        try {
            // 发布消息
            token = mqttTopic.publish(message);
            token.waitForCompletion();
            return true;
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   服务质量
     */
    public void subscribe(String topic, int qos) {
        log.info("开始订阅主题: " + topic);
        try {
            MqttOperateClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            log.error("连接已断开....");
            e.printStackTrace();
        }
    }

    /**
     * 订阅多主题
     *
     * @param topics 主题
     * @param qos    连接方式
     */
    public void subscribe(String[] topics, int[] qos) {
        log.info("【开始批量订阅主题】Start subscribing to topic collection: {}", Arrays.asList(topics));
        try {
            MqttOperateClient.getClient().subscribe(topics, qos);
        } catch (MqttException e) {
            e.printStackTrace();
            log.error("【主题请阅失败】subscribe topic {} qos {} error!", topics, qos);
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            MqttOperateClient.getClient().disconnect();
        } catch (MqttException me) {
            log.error("断开连接异常", me);
        }
    }

}
