package cn.iocoder.yudao.module.steam.service.fin.c5;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.C5OrderInfo;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.OrderCancelRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.utils.C5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.CancelC5OrderVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5OrderDetailRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5ItemListVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5page;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class C5ApiThreeOrderServiceImpl implements ApiThreeOrderService {

    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;
    public static final BigDecimal NO1 = new BigDecimal("1.05");
    public static final BigDecimal NO2 = new BigDecimal("0.998");
    public static final BigDecimal NO3 = new BigDecimal("100");
    public static final BigDecimal NO4 = new BigDecimal("6.75"); //与c5的汇率定为这么多
    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.C5;
    }
    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        checkLoginUser(loginUser);
        //获取c5平台商品最低价
        ProductPriceInfoRes.ProductPriceInfoResponse productPriceInfo =
                C5ApiUtils.getProductPriceInfo(Collections.singletonList(createReqVO.getCommodityHashName()));
        ApiCommodityRespVo apiCommodityRespVo = new ApiCommodityRespVo();
        List<ProductPriceInfoRes.ProductPriceInfoResponse.ProductData> data = null;
        if (productPriceInfo != null) {
            data = productPriceInfo.getData();
        }
        if (data != null && !data.isEmpty()) {
            // 获取第一个 ProductData 对象
            ProductPriceInfoRes.ProductPriceInfoResponse.ProductData productData = data.get(0);
            apiCommodityRespVo.setPrice(BigDecimal.valueOf(productData.getPrice()).multiply(BigDecimal.valueOf(675)).intValue());
            apiCommodityRespVo.setIsSuccess(true);
            apiCommodityRespVo.setErrorCode(OpenApiCode.OK);
        } else {
            apiCommodityRespVo.setIsSuccess(false);
            apiCommodityRespVo.setErrorCode(OpenApiCode.ERR_C5_QUERY_ERROR);
        }
        apiCommodityRespVo.setPlatCode(PlatCodeEnum.C5);
        return apiCommodityRespVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO,Long orderId) {
        BigDecimal divide = new BigDecimal(createReqVO.getPurchasePrice())
                .divide(NO1, 7, RoundingMode.HALF_UP)
                .divide(NO2, 7, RoundingMode.HALF_UP)
                .divide(NO3, 7, RoundingMode.HALF_UP)
                .divide(NO4, 7, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        log.info("C5经过计算购买价格为："+ divide + "元");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String merchantOrderNo = "C" + uuid.substring(1, 21); // 使用UUID的一部分截取20位作为唯一ID 作为C5的商户订单号
        C5FastPayVo c5FastPayVo = new C5FastPayVo(C5ApiUtils.CSGO_ID,1,null,
                1, createReqVO.getCommodityHashName(),divide, merchantOrderNo, createReqVO.getTradeLinks());
        log.info("购买"+createReqVO.getCommodityHashName()+ "商品的入参为"+ c5FastPayVo);
        ProductBuyRes json = C5ApiUtils.buyC5Product(c5FastPayVo);
        ApiBuyItemRespVo apiBuyItemRespVo = new ApiBuyItemRespVo();

        if (json != null) {
            // 设置交易链接
            apiBuyItemRespVo.setTradeLink(createReqVO.getTradeLinks());
            if (json.getErrorCode() != 0){
                apiBuyItemRespVo.setIsSuccess(false);
                apiBuyItemRespVo.setErrorCode(new ErrorCode(json.getErrorCode(), json.getErrorMsg()));
                return apiBuyItemRespVo;
            }
            ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
            apiOrderExtDO.setCreator(String.valueOf(loginUser.getId()));
            apiOrderExtDO.setPlatCode(PlatCodeEnum.C5.getName());
            apiOrderExtDO.setOrderNo(String.valueOf(json.getData().getOrderId()));
            apiOrderExtDO.setTradeOfferLinks(createReqVO.getTradeLinks());
            apiOrderExtDO.setOrderId(orderId);//apiOrder表的主键id
            apiOrderExtDO.setOrderInfo(String.valueOf(json));
            apiOrderExtDO.setOrderStatus(1);
            apiOrderExtDO.setOrderSubStatus(json.getErrorMsg());
            apiOrderExtDO.setMerchantNo(merchantOrderNo);
            apiOrderExtMapper.insert(apiOrderExtDO);
            apiBuyItemRespVo.setOrderNo(String.valueOf(json.getData().getOrderId()));//这里返回的是C5接口的orderId
            apiBuyItemRespVo.setErrorCode(OpenApiCode.OK);
            apiBuyItemRespVo.setIsSuccess(true);
            queryOrderDetail(loginUser,String.valueOf(json.getData().getOrderId()),orderId);
            return apiBuyItemRespVo;

        } else {
            throw new ServiceException(-1,"请求返回C5的json为空");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String queryOrderDetail(LoginUser loginUser, String orderNo,Long orderId) {
        checkLoginUser(loginUser);
        //获取订单详情
        C5OrderInfo c5OrderInfo = C5ApiUtils.getC5OrderInfo(Long.valueOf(orderNo), null);
        ApiOrderExtDO apiOrderExtDO = apiOrderExtMapper.selectOne(ApiOrderExtDO::getOrderNo, orderNo);
        if (apiOrderExtDO == null){
            throw new ServiceException(-1,"该订单不存在，请检查订单号");
        }
        apiOrderExtDO.setOrderNo(orderNo);
        apiOrderExtDO.setOrderSubStatus(String.valueOf(c5OrderInfo.getData().getStatus()));
        apiOrderExtDO.setOrderInfo(JacksonUtils.writeValueAsString(c5OrderInfo));
        apiOrderExtDO.setTradeOfferId(c5OrderInfo.getData().getOfferInfoDTO().getTradeOfferId());
        apiOrderExtDO.setCommodityInfo(JacksonUtils.writeValueAsString(c5OrderInfo.getData().getOpenItemInfo()));
        apiOrderExtMapper.updateById(apiOrderExtDO);
        return JacksonUtils.writeValueAsString(c5OrderInfo);
    }

    @Override
    public String queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {

        return null;
    }

    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        checkLoginUser(loginUser);
        C5OrderInfo c5OrderInfo = C5ApiUtils.getC5OrderInfo(Long.valueOf(orderNo), null);
        if (c5OrderInfo != null && c5OrderInfo.isSuccess() && c5OrderInfo.getData() != null ){
            if (c5OrderInfo.getData().getStatus() == 10){
                return 2;
            }
            if (c5OrderInfo.getData().getStatus() == 11){
                return 3;
            }
            return 1;
        }
        throw new ServiceException(-1,"查询订单出错或者不存在这笔订单，请检查订单号");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {
        checkLoginUser(loginUser);
        CancelC5OrderVo cancelC5OrderVo = new CancelC5OrderVo();
        cancelC5OrderVo.setOrderId(orderNo);
        OrderCancelRes orderCancelRes = C5ApiUtils.cancelC5Order(cancelC5OrderVo);
        if (orderCancelRes != null){
            ApiOrderCancelRespVo apiOrderCancelRespVo = new ApiOrderCancelRespVo();
            if (orderCancelRes.isSuccess()){
                apiOrderCancelRespVo.setIsSuccess(true);
                apiOrderCancelRespVo.setErrorCode(OpenApiCode.OK);
            }
            apiOrderCancelRespVo.setIsSuccess(false);
            apiOrderCancelRespVo.setErrorCode(new ErrorCode(orderCancelRes.getErrorCode(),orderCancelRes.getErrorMsg()));
            return apiOrderCancelRespVo;
        }
        throw new ServiceException(-1,"订单退款异常，请联系管理员");
    }

    @Override
    public ApiOrderCancelRespVo releaseIvn(LoginUser loginUser, String orderNo, Long orderId) {
        return ApiThreeOrderService.super.releaseIvn(loginUser, orderNo, orderId);
    }

    @Override
    public ApiProcessNotifyResp processNotify(String jsonData, String msgNo) {
        return null;
    }

    @Override
    public V5ItemListVO getItemList(V5page v5page) {
        return null;
    }

    private static void checkLoginUser(Object loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
    }

}
