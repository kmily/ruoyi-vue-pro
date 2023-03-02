package cn.iocoder.yudao.module.pay.dal.redis;

import org.redisson.api.RLock;

/**
 * 支付 Redis Key 枚举类
 *
 * @author 芋道源码
 */
public interface RedisKeyConstants {

    /**
     * 通知任务的分布式锁
     *
     * KEY 格式：pay_notify:{id}
     * VALUE 数据类型：HASH {@link RLock}
     * 过期时间：动态传参
     */
    String PAY_NOTIFY_LOCK = "pay_notify:lock:%s";

}
