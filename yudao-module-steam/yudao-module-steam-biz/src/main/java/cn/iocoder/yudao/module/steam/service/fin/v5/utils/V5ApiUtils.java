package cn.iocoder.yudao.module.steam.service.fin.v5.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5ProductVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5BuyProductVo;
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

    private static final String API_POST_V5_ORDER_INFO_URL = "https://delivery.v5item.com/open/api/queryOrderDetailInfo";
//    private static final String API_POST_BUY_V5_PRODUCT_URL = "https://delivery.v5item.com/open/api/createOrderByMarketHashName";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static final String V5_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyVHlwZSI6Im1lcmNoYW50IiwiZXhwIjoxNzE0MDQyOTAxLCJ1c2VyaWQiOiIxMTU3MTAifQ.GRz2fCed-_t6tHZ6a8v8ZAlQWvef68-gN553EkyrXrN8_TyJdCL_98i-wsN-8C3uroLsh_SP5ILH9lxpK7ZIaVnHJrPxjSU2fA1EBnRRl2bCBWNcrsO7XsPpqhLbYOuOi_9CzGgBe7Abby8YYOwoyrZDQiDtjatm3yImcdyjB42RnWXoagUf7U56yatmE-tjWWzzRVYiEuQmtujtkMlLBwrB0KBO1eBasstVm2E4GeV1jNGBdtZxbGZqgPPQ7q3-yQifyAMfESEp02NZpz3H8k-Hda_pL5apj9Emd5XliAegfChDaYC4Gn5b095LRcOVld7zbjBGlSpcaDPqH5WSAA";
    /**
     * 获取c5商品价格
     * @param marketHashNameList
     * @return
     */
    public  static V5ProductPriceInfoRes.V5ProductPriceInfoResponse getV5ProductLowestPrice(List<String> marketHashNameList) {
        V5queryOnSaleInfoReqVO v5FastPayVo = new V5queryOnSaleInfoReqVO(marketHashNameList,MERCHANT_KEY);
        String requestBodyJson = gson.toJson(v5FastPayVo);
        Headers headers = new Headers.Builder()
                .add("Authorization", V5_TOKEN)
                .build();
        Request request = new Request.Builder()
                .url(API_POST_V5_PRODUCT_PRICE_URL)
                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
                .headers(headers)
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
        Headers headers = new Headers.Builder()
                .add("Authorization", V5_TOKEN)
                .build();
        Request request = new Request.Builder()
                .url(API_POST_V5_ORDER_INFO_URL)
                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
                .headers(headers)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ServiceException( -1,"请求订单详情失败");
            }
            if (response.body() != null) {
                String string = response.body().string();
                // 关闭响应体
                response.body().close();
                return string;
            }
        } catch (IOException e) {
            log.error("请求订单详情时发生异常", e);
            throw new ServiceException( -1,"请求订单详情时发生异常");
        }
        return "未获取到订单详情";
    }
//    public static V5ProductBuyRes buyV5Product(V5BuyProductVo buyVo) {
//
//        String requestBodyJson = gson.toJson(buyVo);
//        Headers headers = new Headers.Builder()
//                .add("Authorization", TOKEN)
//                .build();
//        Request request = new Request.Builder()
//                .url(API_POST_BUY_V5_PRODUCT_URL)
//                .post(RequestBody.create(MediaType.parse(JSON), requestBodyJson))
//                .headers(headers)
//                .build();
//        // 发送请求并处理响应
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("购买指定商品失败: " + response);
//            }
//            // 获取响应数据
//            String responseData = null;
//            if (response.body() != null) {
//                responseData = response.body().string();
//                // 关闭响应体
//                response.body().close();
//                // 使用 Gson 将 JSON 字符串转换为对象
//                return gson.fromJson(responseData, V5ProductBuyRes.class);
//            }
//        } catch (IOException e) {
//            log.error("请求购买指定商品时发生异常", e);
//        }
//        return null;
//    }

}
