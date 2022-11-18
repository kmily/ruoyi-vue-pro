package cn.iocoder.yudao.module.yr.mq.producer.dictTree;


import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;

import cn.iocoder.yudao.module.yr.mq.message.dictTree.QgDictTreeRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 业务字典分类相关消息的 Producer
 */
@Component
public class QgDictTreeProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link QgDictTreeRefreshMessage} 消息
     */
    public void sendQgDictTreeRefreshMessage() {
        QgDictTreeRefreshMessage message = new QgDictTreeRefreshMessage();
        redisMQTemplate.send(message);
    }

}
