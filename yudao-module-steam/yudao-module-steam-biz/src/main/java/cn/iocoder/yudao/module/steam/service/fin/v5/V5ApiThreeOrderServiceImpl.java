package cn.iocoder.yudao.module.steam.service.fin.v5;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.enums.IvnStatusEnum;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5OrderDetailRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.utils.V5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.v5.utils.V5Login;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.*;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
public class V5ApiThreeOrderServiceImpl implements ApiThreeOrderService {

    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;
    @Resource
    private RedisTemplate redisTemplate;

    private static final String MERCHANT_KEY = "529606f226e6461ca5bac93047976177";
    private static final String Query_Order_Status_URL = "https://delivery.v5item.com/open/api/queryOrderStatus";
    private static final String Cancel_Order_URL = "https://delivery.v5item.com/open/api/cancelOrder";
    private static final String Query_Commodity_Detail = "https://delivery.v5item.com/open/api/queryOrderDetailInfo";
    public static final String V5_GetItem_URL = "https://delivery.v5item.com/open/api/getItemList";
    public static final BigDecimal NO1 = new BigDecimal("1.02");
    public static final BigDecimal NO2 = new BigDecimal("0.998");
    public static final BigDecimal NO3 = new BigDecimal("100");
    private static final String API_POST_BUY_V5_PRODUCT_URL = "https://delivery.v5item.com/open/api/createOrderByMarketHashName";
    private static final String V5_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyVHlwZSI6Im1lcmNoYW50IiwiZXhwIjoxNzE0MDQyOTAxLCJ1c2VyaWQiOiIxMTU3MTAifQ.GRz2fCed-_t6tHZ6a8v8ZAlQWvef68-gN553EkyrXrN8_TyJdCL_98i-wsN-8C3uroLsh_SP5ILH9lxpK7ZIaVnHJrPxjSU2fA1EBnRRl2bCBWNcrsO7XsPpqhLbYOuOi_9CzGgBe7Abby8YYOwoyrZDQiDtjatm3yImcdyjB42RnWXoagUf7U56yatmE-tjWWzzRVYiEuQmtujtkMlLBwrB0KBO1eBasstVm2E4GeV1jNGBdtZxbGZqgPPQ7q3-yQifyAMfESEp02NZpz3H8k-Hda_pL5apj9Emd5XliAegfChDaYC4Gn5b095LRcOVld7zbjBGlSpcaDPqH5WSAA";
    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.V5;
    }
    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        checkLoginUser(loginUser);
        //获取v5平台商品最低价
//        String token = getTokenFromRedisOrSetNew();
        V5ProductPriceInfoRes.V5ProductPriceInfoResponse productPriceInfo =
                V5ApiUtils.getV5ProductLowestPrice(Collections.singletonList(createReqVO.getCommodityHashName()));
        ApiCommodityRespVo apiCommodityRespVo = new ApiCommodityRespVo();
        List<V5ProductPriceInfoRes.V5ProductPriceInfoResponse.V5ProductData> data = null;
        if (productPriceInfo != null) {
            data = productPriceInfo.getData();
        }
        if (data != null && !data.isEmpty()) {
            // 获取第一个 ProductData 对象
            V5ProductPriceInfoRes.V5ProductPriceInfoResponse.V5ProductData productData = data.get(0);
            int i = BigDecimal.valueOf(productData.getMinSellPrice()).multiply(NO1)
                    .multiply(NO2).multiply(NO3).intValue();
            apiCommodityRespVo.setPrice(i);
            log.info("经过计算返回v5的最低价为："+ i);
            apiCommodityRespVo.setIsSuccess(true);
            apiCommodityRespVo.setPlatCode(PlatCodeEnum.V5);
            return apiCommodityRespVo;
        } else {
            apiCommodityRespVo.setIsSuccess(false);
            return apiCommodityRespVo;
        }
    }
    private static void checkLoginUser(Object loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO,Long orderId) {
        BigDecimal divide = new BigDecimal(Double.toString(createReqVO.getPurchasePrice()))
                .divide(NO1, 7, RoundingMode.HALF_UP)
                .divide(NO2, 7, RoundingMode.HALF_UP)
                .divide(NO3, 7, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        log.info("经过计算购买价格为："+ divide);
        V5BuyProductVo v5BuyProductVo = new V5BuyProductVo(createReqVO.getCommodityHashName(), divide,
                createReqVO.getTradeLinks(),createReqVO.getMerchantNo(),MERCHANT_KEY,createReqVO.getFastShipping());
        log.info("V5BuyProduct的有参构造方法执行了" + v5BuyProductVo);
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        log.info("builder对象创建成功" + builder);
        builder.url(API_POST_BUY_V5_PRODUCT_URL);
        log.info("购买url拼接成功：" + API_POST_BUY_V5_PRODUCT_URL);
        HashMap<String,String> headers = new HashMap<>();
        log.info("hashMap对象创建成功" + builder);
        headers.put("Authorization",V5_TOKEN);
        builder.headers(headers);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(v5BuyProductVo);
        log.info("准备发起http请求：" + builder);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        log.info("发起http请求返回：" + sent.html());
        V5ProductBuyRes json = sent.json(V5ProductBuyRes.class);
        log.info("返回的json对象V5ProductBuyRes为：" + json);
        ApiBuyItemRespVo apiBuyItemRespVo = new ApiBuyItemRespVo();
        if (json != null) {
            // 设置交易链接
            apiBuyItemRespVo.setTradeLink(createReqVO.getTradeLinks());
            // 根据返回的状态码进行处理
            switch (json.getCode()) {
                case 0: // 返回成功
                    if (json.getData() != null) {
                        apiBuyItemRespVo.setOrderNo(json.getData().getOrderNo());
                    }
                    apiBuyItemRespVo.setIsSuccess(true);
                    apiBuyItemRespVo.setErrorCode(OpenApiCode.OK);
                    ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
                    apiOrderExtDO.setCreator(String.valueOf(loginUser.getId()));
                    apiOrderExtDO.setPlatCode(PlatCodeEnum.V5.getName());
                    apiOrderExtDO.setOrderNo(apiBuyItemRespVo.getOrderNo());
                    apiOrderExtDO.setTradeOfferLinks(createReqVO.getTradeLinks());
                    apiOrderExtDO.setOrderId(orderId);
                    apiOrderExtDO.setOrderInfo(sent.html());
                    apiOrderExtDO.setOrderStatus(1);
                    apiOrderExtDO.setOrderSubStatus(json.getMsg());
//                    apiOrderExtDO.setCommodityInfo(createReqVO.getCommodityHashName());
                    apiOrderExtMapper.insert(apiOrderExtDO);
                    log.info("插入的apiOrderExtDO为：" + apiOrderExtDO);
                    queryCommodityDetail(loginUser,json.getData().getOrderNo(),orderId);
                    break;
                case 1: // 返回错误码为1
                    apiBuyItemRespVo.setIsSuccess(false);
                    apiBuyItemRespVo.setErrorCode(OpenApiCode.ERR_1);
                    throw new ServiceException(1, json.getMsg());
                case 1001: // 返回错误码为1001
                    apiBuyItemRespVo.setIsSuccess(false);
                    apiBuyItemRespVo.setErrorCode(OpenApiCode.ERR_1001);
                    break;
                case 1002: // 返回错误码为1002
                    apiBuyItemRespVo.setIsSuccess(false);
                    apiBuyItemRespVo.setErrorCode(OpenApiCode.ERR_1002);
                    break;
                default:
                    // 处理其他未知状态码
                    break;
            }
        } else {
            throw new ServiceException(-1,"请求返回json为空");
        }
        return apiBuyItemRespVo;

    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, String orderNo,Long orderId) {
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        //获取订单详情
//        String token = getTokenFromRedisOrSetNew();
        String v5OrderInfo = V5ApiUtils.getV5OrderInfo(null, orderNo);
        ApiOrderExtDO apiOrderExtDO = apiOrderExtMapper.selectOne(ApiOrderExtDO::getOrderId, orderId);
        if (apiOrderExtDO == null){
            throw new ServiceException(-1,"该订单不存在，请检查订单号");
        }
        Gson gson = new Gson();
        V5OrderDetailRes v5OrderDetailRes = gson.fromJson(v5OrderInfo, V5OrderDetailRes.class);
        apiOrderExtDO.setOrderNo(String.valueOf(orderNo));
        apiOrderExtDO.setOrderSubStatus(String.valueOf(v5OrderDetailRes.getData().getOrderStatus()));
        apiOrderExtDO.setOrderInfo(v5OrderInfo);
        apiOrderExtMapper.updateById(apiOrderExtDO);

        return v5OrderInfo;//TODO 待调试
    }



    /**
     * 查询商品信息
     * @param loginUser 订单用户
     * @param orderNo  v5 订单号
     * @param orderId  主订单ID
     * @return 买家的订单
     */
    @Override
    public String queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {

        V5queryOrderStatusReqVO reqVO = new V5queryOrderStatusReqVO();
        reqVO.setMerchantKey(MERCHANT_KEY); // 商户密钥
        reqVO.setOrderNo(orderNo); // V5订单号
//        reqVO.setMerchantOrderNo("d656e0cbbf3801735d7602731486774e"); // 商户订单号
        // 查询
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(Query_Commodity_Detail);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization",V5_TOKEN);
        builder.headers(headers);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        V5queryCommodityDetailRespVO json = sent.json(V5queryCommodityDetailRespVO.class);
        V5queryCommodityDetailRespVO.OrderDetailInfo data = json.getData();
        if (data == null){
            return json.getMsg();
        }
        V5queryCommodityDetailRespVO.OrderDetailInfo readValue = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data), V5queryCommodityDetailRespVO.OrderDetailInfo.class);
        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setOrderId(orderId);
//        apiOrderExtDO.setOrderInfo(JacksonUtils.writeValueAsString(data));
        apiOrderExtDO.setCommodityInfo(JacksonUtils.writeValueAsString(readValue.getItemInfo()));
        apiOrderExtMapper.update(apiOrderExtDO,new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderNo,orderNo));
        return JacksonUtils.writeValueAsString(data);
    }


    /**
     * 查询订单简单状态
     * @param orderNo  第三方 订单号
     * @param orderId  主订单ID
     * @return  1,进行中，2完成，3作废
     */
    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        V5queryOrderStatusReqVO reqVO = new V5queryOrderStatusReqVO();
        reqVO.setMerchantKey(MERCHANT_KEY); // 商户密钥
        reqVO.setOrderNo(orderNo); // V5订单号
//        reqVO.setMerchantOrderNo(String.valueOf(orderId)); // 商户订单号
        // 查询
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(Query_Order_Status_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization",V5_TOKEN);
        builder.headers(headers);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        log.info("getOrderSimpleStatus{}",sent.html());
        ApiResult json = sent.json(ApiResult.class);
        Object data = json.getData();
        V5queryOrderStatusRespVO respVO = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data), V5queryOrderStatusRespVO.class);
        // 更新订单状态
        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setOrderId(orderId); // 订单ID
        apiOrderExtDO.setOrderSubStatusErrText(respVO.getStatusMsg()); // 订单子状态错误信息
        apiOrderExtDO.setOrderSubStatus(String.valueOf(respVO.getStatus())); // 订单子状态
        apiOrderExtDO.setOrderStatus(IvnStatusEnum.ACTIONING.getCode()); // 订单状态
        if(respVO.getStatus() == 3){
            apiOrderExtDO.setOrderStatus(IvnStatusEnum.DONE.getCode());
        }
        if(respVO.getStatus() == 4){
            apiOrderExtDO.setOrderStatus(IvnStatusEnum.CANCEL.getCode());
        }
        // 更新订单状态
        apiOrderExtMapper.update(apiOrderExtDO,new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderNo,orderNo));
        return apiOrderExtDO.getOrderStatus();
    }

    /**
     * 订单取消   卖家未发送报价的订单才能取消
     * @param orderNo  第三方 订单号
     * @param orderId  主订单ID
     * @Description     0-取消订单成功   1-取消订单正在处理中   2-取消订单失败
     */
    @Override
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {
        V5queryOrderStatusReqVO reqVO = new V5queryOrderStatusReqVO();
        reqVO.setMerchantKey(MERCHANT_KEY); // 商户密钥
        reqVO.setOrderNo(orderNo); // V5订单号
//        reqVO.setMerchantOrderNo(String.valueOf(orderId)); // 商户订单号
        // 查询
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(Cancel_Order_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization",V5_TOKEN);
        builder.headers(headers);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ApiResult json = sent.json(ApiResult.class);
        Object data = json.getData();
        V5cancelOrderRespVO respVO = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data), V5cancelOrderRespVO.class);
        ApiOrderCancelRespVo respVo = new ApiOrderCancelRespVo();
        // 此处接收字段开发文档不一致  Status 或者 cancelStatus
        respVo.setIsSuccess(respVO.getStatus().equals("1"));
        if(!respVo.getIsSuccess()){
            respVo.setErrorCode(new ErrorCode(Integer.valueOf(respVO.getStatus()),respVO.getStatusMsg()));
        }

        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setOrderId(orderId);
//        apiOrderExtDO.setOrderInfo(JacksonUtils.writeValueAsString(respVo));
        apiOrderExtDO.setOrderSubStatus(respVO.getStatus());
        if(Integer.parseInt(respVO.getStatus()) == 0){
            apiOrderExtDO.setOrderStatus(IvnStatusEnum.CANCEL.getCode());
        }
        // 更新订单状态
        apiOrderExtMapper.update(apiOrderExtDO,new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderNo,orderNo));
        return respVo;
    }


    /**
     * V5订单服务器回调
     *
     * @param jsonData V5回调接口
     * @return
     */
    @Override
    public ApiProcessNotifyResp processNotify(String jsonData, String msgNo) {
        V5callBackResult v5respVO = JacksonUtils.readValue(jsonData, V5callBackResult.class);
        String callBackInfo = v5respVO.getCallBackInfo();
        CallBackInfoVO callBackInfoVO = JacksonUtils.readValue(callBackInfo, CallBackInfoVO.class);
        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setOrderNo(callBackInfoVO.getOrderNo());
        // 更新订单小状态
        apiOrderExtDO.setOrderSubStatus(String.valueOf(callBackInfoVO.getOrderStatus()));
        // 更新原因
        String keyword = "余额不足";
        apiOrderExtDO.setOrderSubStatusErrText(callBackInfoVO.getFailReason());
        if(callBackInfoVO.getFailReason().contains(keyword)){
            apiOrderExtDO.setOrderSubStatusErrText("购买失败！请联系工作人员处理。");
        }
        apiOrderExtMapper.update(apiOrderExtDO, new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderNo, callBackInfoVO.getOrderNo()));
        ApiOrderExtDO apiOrderExtDO1 = apiOrderExtMapper.selectOne(ApiOrderExtDO::getOrderNo, callBackInfoVO.getOrderNo());
        ApiProcessNotifyResp respVo = new ApiProcessNotifyResp();
        respVo.setOrderId(apiOrderExtDO1.getOrderId());
        respVo.setOrderNo(apiOrderExtDO1.getOrderNo());
        return respVo;
    }

    @Override
    public V5ItemListVO getItemList(V5page v5page){
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        V5page reqVO = new V5page();
        reqVO.setMerchantKey(MERCHANT_KEY); // 商户密钥
        reqVO.setPageNum(v5page.getPageNum());
        reqVO.setPageIndex(v5page.getPageIndex());
        builder.url(V5_GetItem_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization",V5_TOKEN);
        builder.headers(headers);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        V5ItemListVO json = sent.json(V5ItemListVO.class);
        return json;
    }


//    public String getTokenFromRedisOrSetNew() {
//        // 从 Redis 中获取 token
//        String token = (String) redisTemplate.opsForValue().get("v5_login_token");
//        // 如果 token 为空，则调用方法重新生成并存储 token
//        if (token == null) {
//            token = generateAndStoreToken();
//        }
//        return token;
//    }

//    private String generateAndStoreToken() {
//        // 调用方法生成新的 token
//        String newToken = V5Login.LoginV5();
//        // 将新生成的 token 存储到 Redis 中
//        redisTemplate.opsForValue().set("v5_login_token", newToken);
//
//        return newToken;
//    }

}
