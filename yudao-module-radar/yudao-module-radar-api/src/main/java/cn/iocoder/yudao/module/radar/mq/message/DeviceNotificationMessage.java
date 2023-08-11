package cn.iocoder.yudao.module.radar.mq.message;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author whycode
 * @title: DeviceNotificationMessage
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1114:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceNotificationMessage extends AbstractChannelMessage {

    private String number;

    private int type;


    private String content;

    @Override
    public String getChannel() {
        return "radar.device-event.notice";
    }
}
