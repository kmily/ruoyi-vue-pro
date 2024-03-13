package cn.iocoder.yudao.module.im.dal.redis.inbox;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static cn.iocoder.yudao.module.im.dal.redis.RedisKeyConstants.INBOX_SEQUENCE;

/**
 * 序号生成器 Redis DAO
 *
 * @author anhaohao
 */
@Repository
public class SequenceGeneratorRedisDao {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    private static String formatKey(Long userId) {
        return String.format(INBOX_SEQUENCE, userId);
    }

    public Long generateSequence(Long userId) {
        return redisTemplate.opsForValue().increment(formatKey(userId), 1);
    }

}
