package cn.iocoder.yudao.module.member.mq.procuder.notice;

import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.member.mq.message.SmsNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author whycode
 * @title: SmsNoticeProcuder
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1510:46
 */
@Component
@Slf4j
public class SmsNoticeProcuder {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    public void sendSmsNotice(Long userId, String code, List<String> mobiles, Map<String, Object> params){

        redisMQTemplate.send(new SmsNoticeMessage().setCode(code).setParams(params).setMobile(mobiles).setUserId(userId));

    }

}
