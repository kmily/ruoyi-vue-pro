package cn.iocoder.yudao.module.member.mq.consumer.notice;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.member.service.devicenotice.DeviceNoticeService;
import cn.iocoder.yudao.module.system.api.mq.notice.NoticeRefreshMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author whycode
 * @title: NoticeConsumer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2111:04
 */

@Component
@Slf4j
public class NoticeConsumer extends AbstractChannelMessageListener<NoticeRefreshMessage> {

    @Resource
    private DeviceNoticeService noticeService;

    @Override
    public void onMessage(NoticeRefreshMessage message) {

        Long id = message.getId();
        Byte status = message.getStatus();
        if(Objects.equals((byte)1, status)){
            noticeService.deleteByNoticeId(id);
        }else{
            noticeService.updateByNoticeId(id, message.getContent());
        }
    }
}
