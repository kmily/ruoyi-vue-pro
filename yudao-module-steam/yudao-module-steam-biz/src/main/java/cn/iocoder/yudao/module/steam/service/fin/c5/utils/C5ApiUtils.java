package cn.iocoder.yudao.module.steam.service.fin.c5.utils;

import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
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
    public  static ProductPriceInfoRes.ProductPriceInfoResponse getProductPriceInfo(List<String> marketHashNameList) {
        C5ProductVo c5FastPayVo = new C5ProductVo(CSGO_ID, marketHashNameList);
        String requestBodyJson = gson.toJson(c5FastPayVo);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_POST_PRODUCT_PRICE_URL)
                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
                .build();
        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求商品价格信息响应失败: " + response);
            }
            if (response.body() != null) {
                String responseData = response.body().string();
                // 关闭响应体
                response.body().close();
                // 使用 Gson 将 JSON 字符串转换为对象
                return gson.fromJson(responseData, ProductPriceInfoRes.ProductPriceInfoResponse.class);
            }
            return null;
        } catch (IOException e) {
            log.error("请求商品价格信息时发生异常", e);
        }
        return null;
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
    public static ProductBuyRes buyC5Product(C5FastPayVo payVo) {
        C5FastPayVo c5FastPayVo = new C5FastPayVo(payVo.getAppId(), 1, payVo.getItemId(),
                payVo.getLowPrice(), payVo.getMarketHashName(), payVo.getMaxPrice(), payVo.getOutTradeNo(),
                payVo.getTradeUrl());
        String requestBodyJson = gson.toJson(c5FastPayVo);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_BUY_C5_PRODUCT_URL)
                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
                .build();
        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("购买指定商品失败: " + response);
            }
            // 获取响应数据
            String responseData = null;
            if (response.body() != null) {
                responseData = response.body().string();
                // 关闭响应体
                response.body().close();
                // 使用 Gson 将 JSON 字符串转换为对象
                return gson.fromJson(responseData, ProductBuyRes.class);
            }
        } catch (IOException e) {
            log.error("请求购买指定商品时发生异常", e);
        }
        return null;
    }

}
