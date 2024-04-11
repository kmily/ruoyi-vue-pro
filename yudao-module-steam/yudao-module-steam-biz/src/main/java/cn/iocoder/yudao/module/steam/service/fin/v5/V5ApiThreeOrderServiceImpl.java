package cn.iocoder.yudao.module.steam.service.fin.v5;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.c5.res.ProductPriceInfoRes;
import cn.iocoder.yudao.module.steam.service.fin.c5.utils.C5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.v5.utils.V5ApiUtils;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiBuyItemRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiCommodityRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiOrderCancelRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class V5ApiThreeOrderServiceImpl implements ApiThreeOrderService {

    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;
    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        checkLoginUser(loginUser);
        //获取v5平台商品最低价
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
            apiCommodityRespVo.setIsSuccess(true);

        } else {
            apiCommodityRespVo.setIsSuccess(false);
        }
        apiCommodityRespVo.setPlatCode(PlatCodeEnum.C5);

        return null;
    }
    private static void checkLoginUser(Object loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
    }

    @Override
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        return null;
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



        return null;
    }

    /**
     * 订单取消
     * @param orderNo  第三方 订单号
     * @param orderId  主订单ID
     */
    @Override
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {

        return null;
    }
}
