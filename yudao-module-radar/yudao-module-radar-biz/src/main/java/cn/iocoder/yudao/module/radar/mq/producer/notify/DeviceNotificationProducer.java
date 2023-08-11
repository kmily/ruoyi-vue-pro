package cn.iocoder.yudao.module.radar.mq.producer.notify;

import cn.hutool.core.lang.UUID;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.radar.enums.DeviceDataTypeEnum;
import cn.iocoder.yudao.module.radar.mq.message.DeviceNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: DeviceNotificationProducer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1114:23
 */

@Component
@Slf4j
public class DeviceNotificationProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link cn.iocoder.yudao.module.radar.mq.message.DeviceNotificationMessage} 消息
     */
    public void sendNotifyMessage(DeviceDataTypeEnum typeEnum, String content) {
        DeviceNotificationMessage message = new DeviceNotificationMessage();
        message.setContent(content);
        message.setType(typeEnum.type);
        message.setNumber(UUID.fastUUID().toString());
        redisMQTemplate.send(message);
    }

}
