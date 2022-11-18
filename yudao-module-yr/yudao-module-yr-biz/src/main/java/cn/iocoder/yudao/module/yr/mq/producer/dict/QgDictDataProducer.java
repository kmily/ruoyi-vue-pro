package cn.iocoder.yudao.module.yr.mq.producer.dict;

import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.yr.mq.message.dict.QgDictDataRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Dict 业务字典相关消息的 Producer
 */
@Component
public class QgDictDataProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link QgDictDataRefreshMessage} 消息
     */
    public void sendDictRefreshMessage() {
        QgDictDataRefreshMessage message = new QgDictDataRefreshMessage();
        redisMQTemplate.send(message);
    }

}
