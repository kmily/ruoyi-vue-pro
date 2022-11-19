package cn.iocoder.yudao.module.yr.mq.consumer.dict;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;

import cn.iocoder.yudao.module.yr.mq.message.dict.QgDictDataRefreshMessage;
import cn.iocoder.yudao.module.yr.service.sys.dict.QgDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link QgDictDataRefreshMessage} 的消费者
 *
 * @author alex
 */
@Component
@Slf4j
public class QgDictRefreshConsumer extends AbstractChannelMessageListener<QgDictDataRefreshMessage> {

    @Resource
    private QgDictDataService dictService;

    @Override
    public void onMessage(QgDictDataRefreshMessage message) {
        log.info("[onMessage][收到 qg.dict 刷新消息]");
        dictService.initLocalCache();
    }

}
