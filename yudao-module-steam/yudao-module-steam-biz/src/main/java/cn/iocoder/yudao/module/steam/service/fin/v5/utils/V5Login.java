package cn.iocoder.yudao.module.steam.service.fin.v5.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.LoginRespVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.LoginVO;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;

import javax.annotation.Resource;


public class V5Login {

    @Resource
    private ConfigService configService;
    public  String loginV5() {
        try {
            ConfigDO configUrl = configService.getConfigByKey("v5.url");
            ConfigDO configPassword = configService.getConfigByKey("v5.password");
            ConfigDO configAccount = configService.getConfigByKey("v5.account");
            HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
            builder.url(configUrl.getValue());
            builder.method(HttpUtil.Method.JSON);
            LoginVO user = new LoginVO();
            user.setAccount(configAccount.getValue());
            user.setPassword(configPassword.getValue());
            builder.postObject(user);
            HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
            LoginRespVO json = sent.json(LoginRespVO.class);
            LoginRespVO loginBackVO = JacksonUtils.readValue(JacksonUtils.writeValueAsString(json), LoginRespVO.class);
            return loginBackVO.getData().getTradeToken();

        }catch (ServiceException e){
            throw new ServiceException(OpenApiCode.ERR_V5_LOGIN);
        }


    }