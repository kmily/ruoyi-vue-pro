package cn.iocoder.yudao.module.member.mq.consumer.radar;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.radar.mq.message.radar.RadarStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: RadarStatusConsumer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2411:15
 */

@Component
@Slf4j
public class RadarStatusConsumer extends AbstractChannelMessageListener<RadarStatusMessage> {

    @Resource
    private DeviceUserService deviceUserService;

    @Override
    public void onMessage(RadarStatusMessage message) {

        Long deviceId = message.getDeviceId();
        if(deviceId == null){
            log.error("[RADAR - CONSUMER] - 雷达在线消费消息中不存在设备编号. {}", message);
        }

        deviceUserService.updateKeepalive(deviceId, message.getTime());

    }
}
