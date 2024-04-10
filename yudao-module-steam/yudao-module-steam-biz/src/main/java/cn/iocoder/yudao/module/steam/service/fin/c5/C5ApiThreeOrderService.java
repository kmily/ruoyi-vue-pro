package cn.iocoder.yudao.module.steam.service.fin.c5;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderInfoResp;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiCommodityRespVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import org.springframework.stereotype.Service;

@Service
public class C5ApiThreeOrderService implements ApiThreeOrderService {
    @Override
    public PlatCodeEnum getPlatCode() {
        return ApiThreeOrderService.super.getPlatCode();
    }

    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, CreateCommodityOrderReqVo createReqVO) {
        return null;
    }

    @Override
    public void buyItem(LoginUser loginUser, CreateCommodityOrderReqVo createReqVO) {

    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, Long orderNo) {
        return null;
    }

    @Override
    public ApiOrderDO queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public OrderInfoResp getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }
}
