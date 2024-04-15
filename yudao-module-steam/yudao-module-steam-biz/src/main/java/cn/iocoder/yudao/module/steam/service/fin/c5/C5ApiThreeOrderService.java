package cn.iocoder.yudao.module.steam.service.fin.c5;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.utils.C5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.c5.vo.C5FastPayVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductBuyRes;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5BuyProductVo;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5ItemListVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5page;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class C5ApiThreeOrderService implements ApiThreeOrderService {


    public static final Integer CSGO_ID = 730;
    private static final String APP_KEY = "3453fd2c502c51bcd6a7a68c6e812f85";
    public static final String API_POST_PRODUCT_PRICE_URL = "https://app.zbt.com/open/product/price/info?language=zh-CN&app-key="+APP_KEY;

    private static final String API_GET_C5_ORDER_INFO_URL = "https://app.zbt.com/open/order/v2/buy/detail?language=zh-CN&app-key="+APP_KEY;
    private static final String API_BUY_C5_PRODUCT_URL = "https://app.zbt.com/open/trade/v2/quick-buy?language=zh-CN&app-key="+APP_KEY;

    public static final BigDecimal NO1 = new BigDecimal("1.05");
    public static final BigDecimal NO2 = new BigDecimal("0.998");
    public static final BigDecimal NO3 = new BigDecimal("100");
    public static final BigDecimal NO4 = new BigDecimal("6.75");// 老板定的汇率
//    @Resource
//    private ApiOrderMapper
    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.C5;
    }
    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        checkLoginUser(loginUser);
        //获取c5平台商品最低价
        ProductPriceInfoRes productPriceInfo = C5ApiUtils.getProductPriceInfo(Collections.singletonList(createReqVO.getCommodityHashName()));
        ApiCommodityRespVo apiCommodityRespVo = new ApiCommodityRespVo();
        List<ProductPriceInfoRes.ProductData> data = null;
        if (productPriceInfo != null && productPriceInfo.getErrorCode().equals(0) ) {
            data = productPriceInfo.getData();
        }
        if (data != null && !data.isEmpty()) {
            // 获取第一个 ProductData 对象
            ProductPriceInfoRes.ProductData productData = data.get(0);
            int lowestPrice = productData.getPrice().multiply(NO1)
                    .multiply(NO2).multiply(NO3).multiply(NO4).intValue();
            log.info("经过计算返回v5的最低价为："+ lowestPrice +"分");
            apiCommodityRespVo.setPrice(lowestPrice);
            apiCommodityRespVo.setErrorCode(OpenApiCode.OK);
            apiCommodityRespVo.setIsSuccess(true);
        } else {
            apiCommodityRespVo.setIsSuccess(false);
            apiCommodityRespVo.setErrorCode(OpenApiCode.ERR_5214);
        }
        apiCommodityRespVo.setPlatCode(PlatCodeEnum.C5);
        return apiCommodityRespVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO,Long orderId) {
        BigDecimal maxPrice = new BigDecimal(Double.toString(createReqVO.getPurchasePrice()))
                .divide(NO1, 7, RoundingMode.HALF_UP)
                .divide(NO2, 7, RoundingMode.HALF_UP)
                .divide(NO3, 7, RoundingMode.HALF_UP)
                .divide(NO4, 7, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        log.info("经过计算购买最高价格为："+ maxPrice);
        ProductBuyRes productBuyRes = C5ApiUtils.buyC5Product(createReqVO, maxPrice);
        ApiBuyItemRespVo apiBuyItemRespVo = new ApiBuyItemRespVo();

        if (productBuyRes != null) {
            // 根据返回的状态码进行处理
            switch (productBuyRes.getErrorCode()) {
                case 0: // 返回成功
                    if (productBuyRes.getData() != null) {
                        apiBuyItemRespVo.setOrderNo(String.valueOf(productBuyRes.getData().getOrderId())); //订单id
                    }
//                    apiBuyItemRespVo.setIsSuccess(true);
//                    apiBuyItemRespVo.setErrorCode(OpenApiCode.OK);
//                    ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
//                    apiOrderExtDO.setCreator(String.valueOf(loginUser.getId()));
//                    apiOrderExtDO.setPlatCode(PlatCodeEnum.V5.getName());
//                    apiOrderExtDO.setOrderNo(apiBuyItemRespVo.getOrderNo());
//                    apiOrderExtDO.setTradeOfferLinks(createReqVO.getTradeLinks());
//                    apiOrderExtDO.setOrderId(orderId);
//                    apiOrderExtDO.setOrderInfo(sent.html());
//                    apiOrderExtDO.setOrderStatus(1);
//                    apiOrderExtDO.setOrderSubStatus(json.getMsg());
//                    apiOrderExtDO.setCommodityInfo(createReqVO.getCommodityHashName());
//                    apiOrderExtMapper.insert(apiOrderExtDO);
                    break;
                default:
                    apiBuyItemRespVo.setIsSuccess(false);
                    apiBuyItemRespVo.setErrorCode(OpenApiCode.ERR_1);
                    throw new ServiceException(1, productBuyRes.getErrorMsg());
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
        return C5ApiUtils.getC5OrderInfo(orderId, null);//TODO 待调试
    }

    @Override
    public String queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
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
