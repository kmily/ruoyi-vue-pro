package cn.iocoder.yudao.module.im.dal.redis;


/**
 * im Redis Key 枚举类
 *
 * @author 芋道源码
 */
public interface RedisKeyConstants {

    /**
     * 收件箱序号生成器
     * KEY 格式：  im:inbox:sequence:{userId}
     * VALUE 数据类型： String
     */
    String INBOX_SEQUENCE = "im_inbox_sequence:%s";

    /**
     * 收件箱的分布式锁
     * KEY 格式：  im:inbox:lock:{userId}
     * VALUE 数据类型： String
     */
    String INBOX_LOCK = "im_inbox_lock:%s";
}
