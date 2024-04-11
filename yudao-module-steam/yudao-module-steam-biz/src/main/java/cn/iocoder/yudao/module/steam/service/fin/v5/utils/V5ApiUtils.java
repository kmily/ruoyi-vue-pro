package cn.iocoder.yudao.module.steam.service.fin.v5.utils;

import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5OrderInfo;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5queryOnSaleInfoReqVO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

@Slf4j
public class V5ApiUtils {

    public static final String JSON = "application/json";

    private static final String MERCHANT_KEY = "529606f226e6461ca5bac93047976177";
    public static final String API_POST_V5_PRODUCT_PRICE_URL = "https://delivery.v5item.com/open/api/queryOnSaleInfo";

    private static final String API_POST_V5_ORDER_INFO_URL = "https://delivery.v5item.com/open/api/queryOrderStatus";
    private static final String API_BUY_C5_PRODUCT_URL = "https://app.zbt.com/open/trade/v2/quick-buy?language=zh-CN&app-key=";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    /**
     * 获取c5商品价格
     * @param marketHashNameList
     * @return
     */
    public  static V5ProductPriceInfoRes.V5ProductPriceInfoResponse getV5ProductLowestPrice(List<String> marketHashNameList) {
        V5queryOnSaleInfoReqVO v5FastPayVo = new V5queryOnSaleInfoReqVO(marketHashNameList,MERCHANT_KEY);
        String requestBodyJson = gson.toJson(v5FastPayVo);
        Request request = new Request.Builder()
                .url(API_POST_V5_PRODUCT_PRICE_URL)
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
                return gson.fromJson(responseData, V5ProductPriceInfoRes.V5ProductPriceInfoResponse.class);
            }
            return null;
        } catch (IOException e) {
            log.error("请求商品价格信息时发生异常", e);
        }
        return null;
    }


    public static String getV5OrderInfo(String merchantOrderNo, String orderNo) {

        V5OrderInfo v5OrderInfo = new V5OrderInfo(MERCHANT_KEY, merchantOrderNo, orderNo);
        String requestBodyJson = gson.toJson(v5OrderInfo);
        Request request = new Request.Builder()
                .url(API_POST_V5_ORDER_INFO_URL)
                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
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
    public static ProductBuyRes buyV5Product(C5FastPayVo payVo) {
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
