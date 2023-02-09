package cn.iocoder.yudao.module.system.api.mqtttopic;

import cn.iocoder.yudao.module.system.api.mqtttopic.dto.MqttTopicRespDTO;
import cn.iocoder.yudao.module.system.service.mqtttopic.MqttTopicService;

import javax.annotation.Resource;

/**
 * @author xiaopeng
 * @create 2023/2/2 22:23
 */
public class MqttTopicApiImpl implements MqttTopicApi {

    @Resource
    private MqttTopicService mqttTopicService;

    /**
     * 从redis缓存中获取主题信息
     *
     * @param subTopic 订阅主题
     */
    @Override
    public MqttTopicRespDTO getTopicFromCache(String subTopic) {
        return mqttTopicService.getTopicFromCache(subTopic);
    }
}
