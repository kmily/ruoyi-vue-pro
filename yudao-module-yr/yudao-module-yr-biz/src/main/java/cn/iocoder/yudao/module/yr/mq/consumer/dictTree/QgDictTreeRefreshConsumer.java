package cn.iocoder.yudao.module.yr.mq.consumer.dictTree;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.yr.mq.message.dictTree.QgDictTreeRefreshMessage;
import cn.iocoder.yudao.module.yr.service.sys.dictTree.QgDictTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link QgDictTreeRefreshMessage} 的消费者
 *
 * @author alex
 */
@Component
@Slf4j
public class QgDictTreeRefreshConsumer extends AbstractChannelMessageListener<QgDictTreeRefreshMessage> {

    @Resource
    private QgDictTreeService qgDictTreeService;

    @Override
    public void onMessage(QgDictTreeRefreshMessage message) {
        log.info("[onMessage][收到 ProductTree 刷新消息]");
        qgDictTreeService.initLocalCache();
    }

}
