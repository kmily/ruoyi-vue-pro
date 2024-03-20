package cn.iocoder.yudao.module.steam.service.alipay;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.steam.controller.app.alipay.vo.CreateIsvVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PayApiCode;
import cn.iocoder.yudao.module.steam.service.alipayisv.AlipayIsvExtService;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenInviteOrderCreateRequest;
import com.alipay.api.response.AlipayOpenInviteOrderCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class PayClientService {
    @Resource
    private PayChannelService channelService;

    private static final Long PAY_WITHDRAWAL_APP_ID = 10l;

    private AlipayIsvExtService alipayIsvExtService;

    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Autowired
    public void setAlipayIsvExtService(AlipayIsvExtService alipayIsvExtService) {
        this.alipayIsvExtService = alipayIsvExtService;
    }

    private DefaultAlipayClient getPayClient(){
        PayChannelDO channel = channelService.validPayChannel(PAY_WITHDRAWAL_APP_ID, PayChannelEnum.ALIPAY_PC.getCode());
        PayClient client = channelService.getPayClient(channel.getId());
        return client.getDefaultAliPayClient();
    }

    /**
     * 签约授权页面申请（获取签约链接）
     * @param req
     * @return
     */
    public String createIsv(CreateIsvVo req, LoginUser loginUser) {
        req.setIsvBizId(noRedisDAO.generate("ISV"));
        AlipayIsvDO alipayIsvDO = alipayIsvExtService.initIsv(req,loginUser);
        DefaultAlipayClient payClient = getPayClient();
        AlipayOpenInviteOrderCreateRequest request = new AlipayOpenInviteOrderCreateRequest();
        request.setBizContent(JacksonUtils.writeValueAsString(req));
        try{
            AlipayOpenInviteOrderCreateResponse response = payClient.pageExecute(request);
            checkAlipayResponse(response);
            String submitFormData = response.getBody();
            return submitFormData;
        }catch (AlipayApiException e){
            throw new ServiceException(PayApiCode.PAY_ERROR);
        }
    }
    private void checkAlipayResponse(AlipayResponse response){
        log.error("alipay checkAlipayResponse{}", JsonUtils.toJsonString(response));
        if(Objects.isNull(response) && Objects.isNull(response.getBody())){
            throw new ServiceException(OpenApiCode.TO_MANY);
        }
        if("0".equals(response.getCode()) || Objects.nonNull(response.getBody())){
            return;
        }
        throw new ServiceException(Integer.valueOf(response.getCode()),response.getMsg()+"--"+response.getSubMsg());
    }
}
