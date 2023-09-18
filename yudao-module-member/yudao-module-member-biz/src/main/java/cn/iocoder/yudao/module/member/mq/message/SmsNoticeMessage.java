package cn.iocoder.yudao.module.member.mq.message;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author whycode
 * @title: SmsNoticeMessage
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1510:49
 */

@Data
public class SmsNoticeMessage extends AbstractChannelMessage {


    private String code;
    private Long userId;

    private List<String> mobile;

    private Map<String, Object> params;

    @Override
    public String getChannel() {
        return "radar.device.alarm.notice";
    }
}
