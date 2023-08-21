package cn.iocoder.yudao.module.system.mq.producer.notice;

import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.system.api.mq.notice.NoticeRefreshMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: NoticeProducer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2110:53
 */

@Slf4j
@Component
public class NoticeProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link NoticeRefreshMessage} 消息
     */
    public void sendNoticeRefreshMessage(NoticeRefreshMessage message) {
        redisMQTemplate.send(message);
    }
}
