package cn.iocoder.yudao.module.system.dal.redis.mqtttopic;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.convert.mqtttopic.MqttTopicConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants.MQTT_TOPIC;

/**
 * @author ZanGe
 * @create 2023/2/2 21:56
 */
@Repository
public class MqttTopicRedisDAO {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public MqttTopicDO get(String subTopic) {
        String redisKey = formatKey(subTopic);
        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(redisKey), MqttTopicDO.class);
    }

    public void set(MqttTopicDO MqttTopicDO) {
        String redisKey = formatKey(MqttTopicDO.getSubTopic());
        stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(MqttTopicConvert.INSTANCE.convert01(MqttTopicDO)));
    }

    public void delete(String subTopic) {
        String redisKey = formatKey(subTopic);
        stringRedisTemplate.delete(redisKey);
    }

    public void deleteList(Collection<String> subTopics) {
        List<String> redisKeys = CollectionUtils.convertList(subTopics, MqttTopicRedisDAO::formatKey);
        stringRedisTemplate.delete(redisKeys);
    }

    private static String formatKey(String subTopic) {
        return String.format(MQTT_TOPIC.getKeyTemplate(), subTopic);
    }

}
