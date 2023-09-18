package cn.iocoder.yudao.module.member.mq.consumer.sms;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.member.enums.RadarMessageEnum;
import cn.iocoder.yudao.module.member.mq.message.SmsNoticeMessage;
import cn.iocoder.yudao.module.system.api.sms.SmsSendApi;
import cn.iocoder.yudao.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whycode
 * @title: AlarmNoticeConsumer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1511:10
 */

@Component
@Slf4j
public class AlarmNoticeConsumer extends AbstractChannelMessageListener<SmsNoticeMessage> {

    @Resource
    private SmsSendApi smsSendApi;

    @Override
    public void onMessage(SmsNoticeMessage message) {
        List<String> mobile = message.getMobile();
        mobile.forEach(item -> {
            SmsSendSingleToUserReqDTO reqDTO = new SmsSendSingleToUserReqDTO()
                    .setMobile(item)
                    .setUserId(message.getUserId())
                    .setTemplateCode(message.getCode())
                    .setTemplateParams(message.getParams());
            smsSendApi.sendSingleSmsToMember(reqDTO);
        });
    }
}
