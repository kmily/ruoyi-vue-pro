package cn.iocoder.yudao.framework.idempotent.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 幂等 Redis DAO
 *
 * @author 芋道源码
 */
@AllArgsConstructor
public class IdempotentRedisDAO {

    /**
     * 幂等操作的 Redis Key 模板
     *
     * KEY 格式：idempotent::{uuid}
     * VALUE 格式：空字符串
     * 过期时间：动态传参
     */
    private static final String IDEMPOTENT_KEY_TEMPLATE = "idempotent:%s";

    private final StringRedisTemplate redisTemplate;

    public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(key);
        return redisTemplate.opsForValue().setIfAbsent(redisKey, "", timeout, timeUnit);
    }

    private static String formatKey(String key) {
        return String.format(IDEMPOTENT_KEY_TEMPLATE, key);
    }

}
