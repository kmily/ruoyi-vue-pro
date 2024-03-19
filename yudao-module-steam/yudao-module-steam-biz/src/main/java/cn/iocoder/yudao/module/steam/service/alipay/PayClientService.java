package cn.iocoder.yudao.module.steam.service.alipay;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.steam.enums.PayApiCode;
import cn.iocoder.yudao.module.steam.service.alipay.vo.CreateIsvVo;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenInviteOrderCreateRequest;
import com.alipay.api.response.AlipayOpenInviteOrderCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class PayClientService {
    @Resource
    private PayChannelService channelService;

    private static final Long PAY_WITHDRAWAL_APP_ID = 10L;
    private DefaultAlipayClient getPayClient(){
        PayChannelDO channel = channelService.validPayChannel(PAY_WITHDRAWAL_APP_ID, PayChannelEnum.ALIPAY_PC.getCode());
        PayClient client = channelService.getPayClient(channel.getId());
        return client.getDefaultAliPayClient();
    }
    public String createIsv(CreateIsvVo req) {
        DefaultAlipayClient payClient = getPayClient();
        AlipayOpenInviteOrderCreateRequest request = new AlipayOpenInviteOrderCreateRequest();
        request.setBizContent(JacksonUtils.writeValueAsString(req));
        try{
            AlipayOpenInviteOrderCreateResponse response = payClient.pageExecute(request);
            String submitFormData = response.getBody();
            return submitFormData;
        }catch (AlipayApiException e){
            throw new ServiceException(PayApiCode.PAY_ERROR);
        }
    }
}
