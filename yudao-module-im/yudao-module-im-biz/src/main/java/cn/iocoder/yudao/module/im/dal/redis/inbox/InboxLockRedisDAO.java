package cn.iocoder.yudao.module.im.dal.redis.inbox;

import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.module.im.dal.redis.RedisKeyConstants.INBOX_LOCK;

/**
 * 收件箱的锁 Redis DAO
 *
 * @author 芋道源码
 */
@Repository
public class InboxLockRedisDAO {

    @Resource
    private RedissonClient redissonClient;

    private static String formatKey(Long id) {
        return String.format(INBOX_LOCK, id);
    }

    public void lock(Long id, Long timeoutMillis, Runnable runnable) {
        String lockKey = formatKey(id);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(timeoutMillis, TimeUnit.MILLISECONDS);
            // 执行逻辑
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

}
