package cn.iocoder.yudao.module.pay.dal.redis.notify;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.module.pay.dal.redis.RedisKeyConstants.PAY_NOTIFY_LOCK;

/**
 * 支付通知的锁 Redis DAO
 *
 * @author 芋道源码
 */
@Repository
public class PayNotifyLockRedisDAO {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 获得分布式锁，执行逻辑
     *
     * @param id 通知任务编号
     * @param timeoutMillis 过期时间
     * @param runnable 逻辑
     */
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

    private static String formatKey(Long id) {
        return String.format(PAY_NOTIFY_LOCK, id);
    }

}
