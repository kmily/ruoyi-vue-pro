package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderCancelVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderInfoResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;

import javax.validation.Valid;

/**
 * 示例订单 Service 接口
 *
 * @author 芋道源码
 */
public interface UUOrderService {
    /**
     * 创建提现订单
     *
     * @param loginUser      用户
     * @param createReqVO 创建信息
     * @return 编号
     */
    CreateOrderResult createWithdrawalOrder(LoginUser loginUser, @Valid PayWithdrawalOrderCreateReqVO createReqVO);
    /**
     * 更新提现订单为已支付
     *
     * @param id 编号
     * @param payOrderId 支付订单号
     */
    void updateWithdrawalOrderPaid(Long id, Long payOrderId);
    /**
     * 创建示例订单
     *
     * @param loginUser      用户
     * @param createReqVO 创建信息
     * @return 编号
     */
    YouyouOrderDO createInvOrder(LoginUser loginUser, @Valid CreateCommodityOrderReqVo createReqVO);

    /**
     * 获得订单详情
     * 订单是以买家身份进行查询
     * @param loginUser 订单用户
     * @param queryOrderReqVo 订单号
     * @return 示例订单
     */
    YouyouOrderDO getUUOrder(LoginUser loginUser, QueryOrderReqVo queryOrderReqVo);
    OrderInfoResp orderInfo(YouyouOrderDO youyouOrderDO);
    /**
     * 获得示例订单列表
     * @param youyouOrderPageReqVO
     * @return
     */
//    PageResult<YouyouOrderDO> getInvOrderPageOrder(YouyouOrderPageReqVO youyouOrderPageReqVO);
    /**
    /**
     * 更新示例订单为已支付
     *
     * @param id 编号
     * @param payOrderId 支付订单号
     */
    void updateInvOrderPaid(Long id, Long payOrderId);

    /**
     * 发起示例订单的退款
     *
     * @param id 编号
     * @param userIp 用户编号
     */
    Integer refundInvOrder(LoginUser loginUser, OrderCancelVo id, String userIp);

    /**
     * 更新示例订单为已退款
     *
     * @param id 编号
     * @param payRefundId 退款订单号
     */
    void updateInvOrderRefunded(Long id, Long payRefundId);

    void processNotify(NotifyReq notifyReq);

}
