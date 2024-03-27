package cn.iocoder.yudao.module.im.dal.redis.inbox;

import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static cn.iocoder.yudao.module.im.dal.redis.RedisKeyConstants.INBOX_SEQUENCE;

// TODO @芋艿：这个名字，需要在考虑下；

/**
 * 序号生成器 Redis DAO
 *
 * @author anhaohao
 */
@Repository
public class SequenceRedisDao {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    private static String formatKey(Long userId) {
        return String.format(INBOX_SEQUENCE, userId);
    }

    /**
     * 生成序号
     *
     * @param userId 用户编号
     * @return 序号
     */
    public Long generateSequence(Long userId) {
        return redisTemplate.opsForValue().increment(formatKey(userId), 1);
    }

}
