package cn.iocoder.yudao.module.steam.service.fin.c5.utils;

import cn.iocoder.yudao.module.steam.service.fin.c5.res.C5OrderInfo;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.OrderCancelRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.CancelC5OrderVo;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class C5ApiUtils {
    public static final Integer CSGO_ID = 730;
    public static final String JSON = "application/json";

    private static final String APP_KEY = "3453fd2c502c51bcd6a7a68c6e812f85";
    public static final String API_POST_PRODUCT_PRICE_URL = "https://app.zbt.com/open/product/price/info?language=zh-CN&app-key="+APP_KEY;

    private static final String API_GET_C5_ORDER_INFO_URL = "https://app.zbt.com/open/order/v2/buy/detail?language=zh-CN&app-key="+APP_KEY;
    private static final String API_BUY_C5_PRODUCT_URL = "https://app.zbt.com/open/trade/v2/quick-buy?language=zh-CN&app-key="+APP_KEY;
    private static final String API_CANCEL_C5_ORDER_URL = "https://app.zbt.com/open/order/buyer-cancel?language=zh-CN&app-key="+APP_KEY;

    /**
     * 获取c5商品价格

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

    /**
     *获取C5订单详情
     */
    public static C5OrderInfo getC5OrderInfo(Long orderId, String outTradeNo) {
        // 构建带有参数的URL
        String urlWithParams = API_GET_C5_ORDER_INFO_URL + "&orderId=" + orderId + "&outTradeNo=" + outTradeNo;
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(urlWithParams);
        builder.method(HttpUtil.Method.GET);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        return sent.json(C5OrderInfo.class);
    }

    /**
     * 购买C5的商品
     */
    public static ProductBuyRes buyC5Product(C5FastPayVo payVo) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_BUY_C5_PRODUCT_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(payVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ProductBuyRes json = sent.json(ProductBuyRes.class);
        log.info("购买C5商品返回的json：" + json);
        return json;
    }

    public static OrderCancelRes cancelC5Order(CancelC5OrderVo cancelC5OrderVo) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_CANCEL_C5_ORDER_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(cancelC5OrderVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        OrderCancelRes json = sent.json(OrderCancelRes.class);
        log.info("取消C5订单订单返回json：" + json);
        return json;
    }

}
