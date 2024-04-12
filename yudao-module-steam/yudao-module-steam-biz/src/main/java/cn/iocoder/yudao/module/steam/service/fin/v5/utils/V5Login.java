package cn.iocoder.yudao.module.steam.service.fin.v5.utils;

import cn.iocoder.yudao.module.steam.service.fin.v5.vo.LoginRespVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.LoginVO;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;

public class V5Login {

    public static final String API_POST_V5_PRODUCT_PRICE_URL = "https://delivery.v5item.com/open/api/queryMerchantInfo";
    private static final String MERCHANT_KEY = "529606f226e6461ca5bac93047976177";
    private static final String ACCOUNT = "15002361201";
    private static final String PASSWORD = "bbbhui342680800";

    public static String LoginV5() {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_POST_V5_PRODUCT_PRICE_URL);
        builder.method(HttpUtil.Method.JSON);
        LoginVO user = new LoginVO();
        user.setAccount(ACCOUNT);
        user.setPassword(PASSWORD);
        builder.postObject(user);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        LoginRespVO json = sent.json(LoginRespVO.class);
        LoginRespVO loginBackVO = JacksonUtils.readValue(JacksonUtils.writeValueAsString(json), LoginRespVO.class);
        return loginBackVO.getData().getTradeToken();
    }
}
