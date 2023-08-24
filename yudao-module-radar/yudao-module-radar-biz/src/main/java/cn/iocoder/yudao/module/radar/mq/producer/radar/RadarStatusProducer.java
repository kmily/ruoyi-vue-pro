package cn.iocoder.yudao.module.radar.mq.producer.radar;

import cn.hutool.core.lang.UUID;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.radar.enums.DeviceDataTypeEnum;
import cn.iocoder.yudao.module.radar.mq.message.notiy.DeviceNotificationMessage;
import cn.iocoder.yudao.module.radar.mq.message.radar.RadarStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: RadarStatusProducer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2411:08
 */

@Component
@Slf4j
public class RadarStatusProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link cn.iocoder.yudao.module.radar.mq.message.radar.RadarStatusMessage} 消息
     */
    public void sendRadarStatusMessage(Long deviceId, LocalDateTime time) {
        RadarStatusMessage message = new RadarStatusMessage()
                .setDeviceId(deviceId).setTime(time);

        redisMQTemplate.send(message);
    }
}
