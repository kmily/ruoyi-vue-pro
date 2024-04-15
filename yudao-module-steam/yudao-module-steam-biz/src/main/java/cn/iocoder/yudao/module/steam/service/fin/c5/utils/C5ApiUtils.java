package cn.iocoder.yudao.module.steam.service.fin.c5.utils;

import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
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

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    /**
     * 获取c5商品价格
     * @param marketHashNameList
     * @return
     */
    public  static ProductPriceInfoRes getProductPriceInfo(List<String> marketHashNameList) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        C5ProductVo c5ProductVo = new C5ProductVo(CSGO_ID, marketHashNameList);
        builder.url(API_POST_PRODUCT_PRICE_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(c5ProductVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        return sent.json(ProductPriceInfoRes.class);

    }


    public static String getC5OrderInfo(Long orderId, String outTradeNo) {
        StringBuilder urlBuilder = new StringBuilder(API_GET_C5_ORDER_INFO_URL);
        // 添加可选参数
        if (orderId != null) {
            urlBuilder.append("&orderId=").append(orderId);
        }
        if (outTradeNo != null && !outTradeNo.isEmpty()) {
            urlBuilder.append("&outTradeNo=").append(outTradeNo);
        }
        Request request = new Request.Builder()
                .url(String.valueOf(urlBuilder))
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求订单详情失败: " + response);
            }
            if (response.body() != null) {
                String string = response.body().string();
                // 关闭响应体
                response.body().close();
                return string;
            }
        } catch (IOException e) {
            log.error("请求订单详情时发生异常", e);
        }
        return "未获取到订单详情";
    }
    public static ProductBuyRes buyC5Product(ApiQueryCommodityReqVo createReqVO, BigDecimal maxPrice) {

        C5FastPayVo c5FastPayVo = new C5FastPayVo(CSGO_ID,0,null,null,
                createReqVO.getCommodityHashName(),maxPrice,null, createReqVO.getTradeLinks());
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(API_BUY_C5_PRODUCT_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(c5FastPayVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        V5ProductBuyRes json = sent.json(V5ProductBuyRes.class);
        return sent.json(ProductBuyRes.class);
    }
}
