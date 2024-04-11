package cn.iocoder.yudao.module.steam.service.fin.v5;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderMapper;

import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5BuyProductVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5cancelOrderRespVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5queryOrderStatusReqVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5queryOrderStatusRespVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.utils.V5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiBuyItemRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiCommodityRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiOrderCancelRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class V5ApiThreeOrderServiceImpl implements ApiThreeOrderService {

    @Resource
    private ApiOrderMapper apiOrderMapper;
    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;

    private static final String MERCHANTKEY = "529606f226e6461ca5bac93047976177";
    private static final String Query_Order_Status_URL = "https://delivery.v5item.com/open/api/queryOrderStatus";
    private static final String Cancel_Order_URL = "https://delivery.v5item.com/open/api/queryOrderStatus";



    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.V5;
    }
    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        checkLoginUser(loginUser);
        //获取v5平台商品最低价
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
            apiCommodityRespVo.setPrice(BigDecimal.valueOf(productData.getMinSellPrice()).multiply(BigDecimal.valueOf(100)).intValue());
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
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        V5BuyProductVo v5BuyProductVo = new V5BuyProductVo(createReqVO.getCommodityHashName(),createReqVO.getPurchasePrice(),
                createReqVO.getTradeLinks(),createReqVO.getMerchantNo(),MERCHANTKEY,0);
        V5ProductBuyRes v5ProductBuyRes = V5ApiUtils.buyV5Product(v5BuyProductVo);

        ApiBuyItemRespVo apiBuyItemRespVo = new ApiBuyItemRespVo();
        apiBuyItemRespVo.setIsSuccess(true);
        if (v5ProductBuyRes != null) {
            apiBuyItemRespVo.setOrderNo(v5ProductBuyRes.getData().getOrderNo());
        }
        apiBuyItemRespVo.setTradeLink(createReqVO.getTradeLinks());
        apiBuyItemRespVo.setTradeOfferId(null);// TODO

        //下单成功
        if (apiBuyItemRespVo.getIsSuccess()){

        }
        //
//        apiOrderExtMapper.selectOne(ApiOrderExtDO::)
        return apiBuyItemRespVo;
    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, Long orderNo,Long orderId) {
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        //获取订单详情
        String v5OrderInfo = V5ApiUtils.getV5OrderInfo(null, String.valueOf(orderNo));
        ApiOrderExtDO apiOrderExtDO = apiOrderExtMapper.selectOne(ApiOrderExtDO::getOrderId, orderId);
        if (apiOrderExtDO == null){
            throw new ServiceException(-1,"该订单不存在，请检查订单号");
        }
        apiOrderExtMapper.updateById(apiOrderExtDO.setOrderInfo(v5OrderInfo));
        return v5OrderInfo;//TODO 待调试
    }



    /**
     * 查询商品信息
     * @param loginUser 订单用户
     * @param orderNo  第三方 订单号
     * @param orderId  主订单ID
     * @return 买家的订单
     */
    @Override
    public ApiOrderDO queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }


    /**
     * 查询订单简状态
     * @param orderNo  第三方 订单号
     * @param orderId  主订单ID
     * @return  1,进行中，2完成，3作废
     */
    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        V5queryOrderStatusReqVO reqVO = new V5queryOrderStatusReqVO();
        reqVO.setMerchantKey(MERCHANTKEY); // 商户密钥
        reqVO.setOrderNo(orderNo); // V5订单号
        reqVO.setMerchantOrderNo(String.valueOf(orderId)); // 商户订单号
        // 查询
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(Query_Order_Status_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ApiResult json = sent.json(ApiResult.class);
        Object data = json.getData();
        V5queryOrderStatusRespVO respVO = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data), V5queryOrderStatusRespVO.class);
//        // 更新订单状态
//        ApiOrderDO apiOrderDO = new ApiOrderDO();
//        apiOrderDO.setId(orderId);
//        apiOrderDO.setOrderStatus(respVO.getStatus());
//        apiOrderDO.setOrderSubStatus(respVO.getStatus());
//        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
//        apiOrderExtDO.setOrderId(orderId);
//        apiOrderExtDO.setOrderSubStatus(respVO.getStatus());
//        if(respVO.getStatus() == 0){
//            apiOrderExtDO.setOrderStatus(3);
//        }
//        if(respVO.getStatus() == 0){
//            apiOrderExtDO.setOrderStatus(3);
//        }
//        // 更新订单状态
//        apiOrderMapper.updateById(apiOrderDO);
//        apiOrderExtMapper.update(apiOrderExtDO,new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderId,orderId));
        return respVO.getStatus();
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
        reqVO.setMerchantKey(MERCHANTKEY); // 商户密钥
        reqVO.setOrderNo(orderNo); // V5订单号
        reqVO.setMerchantOrderNo(String.valueOf(orderId)); // 商户订单号
        // 查询
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(Cancel_Order_URL);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(reqVO);
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

        ApiOrderDO apiOrderDO = new ApiOrderDO();
        apiOrderDO.setId(orderId);
        apiOrderDO.setOrderStatus(Integer.valueOf(respVO.getStatus()));
        apiOrderDO.setOrderSubStatus(Integer.valueOf(respVO.getStatus()));
        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setOrderId(orderId);
        apiOrderExtDO.setOrderSubStatus(Integer.valueOf(respVO.getStatus()));
        if(Integer.parseInt(respVO.getStatus()) == 0){
            apiOrderExtDO.setOrderStatus(3);
        }
        // 更新订单状态
        apiOrderMapper.updateById(apiOrderDO);
        apiOrderExtMapper.update(apiOrderExtDO,new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderId,orderId));
        return respVo;
    }
}
