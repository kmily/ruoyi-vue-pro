package cn.iocoder.yudao.module.radar.mq.message.radar;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: RadarStatusMessage
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2411:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RadarStatusMessage extends AbstractChannelMessage {

    private Long deviceId;

    private LocalDateTime time;


    @Override
    public String getChannel() {
        return "radar.device-status.notice";
    }
}
