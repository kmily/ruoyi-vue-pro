package cn.iocoder.yudao.module.system.api.mqtttopic;

import cn.iocoder.yudao.module.system.api.mqtttopic.dto.MqttTopicRespDTO;

/**
 * @author ZanGe
 * @create 2023/2/2 22:24
 */
public interface MqttTopicApi {

    /**
     * 根据订阅主题从redis缓存中获取对应主题信息
     *
     * @param subTopic 订阅主题
     */
    MqttTopicRespDTO getTopicFromCache(String subTopic);
}
