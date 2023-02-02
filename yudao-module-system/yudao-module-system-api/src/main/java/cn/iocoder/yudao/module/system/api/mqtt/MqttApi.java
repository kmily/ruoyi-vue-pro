package cn.iocoder.yudao.module.system.api.mqtt;

/**
 * @author ZanGe
 * @create 2023/2/2 12:45
 */
public interface MqttApi {
    /**
     * MQTT消息发布
     *
     * @param topic       主题
     * @param pushMessage 消息体
     * @return 结果
     */
    boolean publishMsg(String topic, String pushMessage);

    /**
     * 消息发布
     *
     * @param qos         服务质量
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     * @return 结果
     */
    boolean publishMsg(String topic, String pushMessage, int qos, boolean retained);

    /**
     * 消费者订阅主题
     *
     * @param topic 主题
     * @param qos   服务质量
     */
    void subscribe(String topic, int qos);

    /**
     * 订阅多主题
     *
     * @param topics 主题数组
     * @param qos    服务质量
     */
    void subscribe(String[] topics, int[] qos);
}
