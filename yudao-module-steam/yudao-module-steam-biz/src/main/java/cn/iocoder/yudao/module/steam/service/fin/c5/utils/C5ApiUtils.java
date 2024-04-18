package cn.iocoder.yudao.module.steam.service.fin.c5.utils;

import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5BuyProductVo;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class C5ApiUtils {
    public static final Integer CSGO_ID = 730;
    public static final String JSON = "application/json";

    private static final String APP_KEY = "3453fd2c502c51bcd6a7a68c6e812f85";
    public static final String API_POST_PRODUCT_PRICE_URL = "https://app.zbt.com/open/product/price/info?language=zh-CN&app-key="+APP_KEY;

    private static final String API_GET_C5_ORDER_INFO_URL = "https://app.zbt.com/open/order/v2/buy/detail?language=zh-CN&app-key="+APP_KEY;
    private static final String API_BUY_C5_PRODUCT_URL = "https://app.zbt.com/open/trade/v2/quick-buy?language=zh-CN&app-key="+APP_KEY;


    private static final Gson gson = new Gson();
    /**
     * 获取c5商品价格
     * @param marketHashNameList
     * @return
     */
    public  static ProductPriceInfoRes.ProductPriceInfoResponse getProductPriceInfo(List<String> marketHashNameList) {
        C5ProductVo c5FastPayVo = new C5ProductVo(CSGO_ID, marketHashNameList);
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_POST_PRODUCT_PRICE_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(c5FastPayVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ProductPriceInfoRes.ProductPriceInfoResponse json = sent.json(ProductPriceInfoRes.ProductPriceInfoResponse.class);
        log.info("C5查询最低价格返回的json：" + json);
        return json;
    }


    public static String getC5OrderInfo(Long orderId, String outTradeNo) {
        // 构建带有参数的URL
        String urlWithParams = API_GET_C5_ORDER_INFO_URL + "&orderId=" + orderId + "&outTradeNo=" + outTradeNo;
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(urlWithParams);
        builder.method(HttpUtil.Method.GET);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        return sent.html();
    }
    public static ProductBuyRes buyC5Product(C5FastPayVo payVo) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_BUY_C5_PRODUCT_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(payVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ProductBuyRes json = sent.json(ProductBuyRes.class);
        log.info("json：" + json);
        return json;
    }

}
