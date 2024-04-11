package cn.iocoder.yudao.module.steam.service.fin.v5;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiBuyItemRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiCommodityRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiOrderCancelRespVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import org.springframework.stereotype.Service;

@Service
public class V5ApiThreeOrderServiceImpl implements ApiThreeOrderService {

    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        return null;
    }

    @Override
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        return null;
    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, Long orderNo) {
        return null;
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
