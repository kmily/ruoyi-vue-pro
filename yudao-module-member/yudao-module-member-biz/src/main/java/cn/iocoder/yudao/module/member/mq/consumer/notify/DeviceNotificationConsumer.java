package cn.iocoder.yudao.module.member.mq.consumer.notify;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.member.service.devicenotice.DeviceNoticeService;
import cn.iocoder.yudao.module.radar.mq.message.notiy.DeviceNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: DeviceNotificationConsumer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1114:17
 */
@Component
@Slf4j
public class DeviceNotificationConsumer extends AbstractChannelMessageListener<DeviceNotificationMessage> {

    @Resource
    private DeviceNoticeService noticeService;

    @Override
    public void onMessage(DeviceNotificationMessage message) {

        log.info("[DEVICE NOTICE] 收到消息 [{}] -- [{}]", message.getType(), message.getContent());

        noticeService.createDeviceNotice(message.getType(), message.getContent(), message.getNumber());

    }
}
