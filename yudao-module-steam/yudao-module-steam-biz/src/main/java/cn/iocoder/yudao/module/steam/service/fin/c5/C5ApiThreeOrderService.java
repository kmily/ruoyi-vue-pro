package cn.iocoder.yudao.module.steam.service.fin.c5;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.utils.C5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class C5ApiThreeOrderService implements ApiThreeOrderService {

    @Resource
    private SteamService steamService;
//    @Resource
//    private ApiOrderMapper
    @Override
    public PlatCodeEnum getPlatCode() {
        return ApiThreeOrderService.super.getPlatCode();
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
            apiCommodityRespVo.setPrice(BigDecimal.valueOf(productData.getPrice()).multiply(BigDecimal.valueOf(100)).intValue());
        } else {
            apiCommodityRespVo.setPrice(null);
        }
        apiCommodityRespVo.setPlatCode(PlatCodeEnum.C5);
        return apiCommodityRespVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        //买家身份检测
        BindUserDO buyBindUserDO=null;
        if(StringUtils.hasText(createReqVO.getTradeLinks())){
            buyBindUserDO = steamService.getTempBindUserByLogin(loginUser, createReqVO.getTradeLinks(),true);
        }
        if(Objects.isNull(buyBindUserDO)){
            throw new ServiceException(OpenApiCode.ERR_5201);
        }

        ApiCommodityRespVo query = query(loginUser, createReqVO);
        if (query != null && query.getPrice() <= createReqVO.getPurchasePrice() ){ //满足C5下单条件
        // TODO
//            C5ApiUtils.buyC5Product()
        }

        ApiBuyItemRespVo apiBuyItemRespVo=new ApiBuyItemRespVo();
//        SellingDO sellingDO = sellingMapper.selectById(reqVo.getSellId());
//        if(Objects.isNull(sellingDO)){
//            throw new ServiceException(OpenApiCode.ERR_5214);
//        }

        //创建订单
        ApiOrderDO apiOrderDO = new ApiOrderDO();

        return null;
    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, Long orderNo) {
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        //获取订单详情
        return C5ApiUtils.getC5OrderInfo(orderNo, null);//TODO 待调试
    }

    @Override
    public ApiOrderDO queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
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
    private static void checkLoginUser(Object loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
    }

}
