package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.InvOrderListReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;

import javax.validation.Valid;

/**
 * 示例订单 Service 接口
 *
 * @author 芋道源码
 */
public interface PaySteamOrderService {
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
    CreateOrderResult createInvOrder(LoginUser loginUser, @Valid PaySteamOrderCreateReqVO createReqVO);

    /**
     * 获得示例订单
     *
     * @param id 编号
     * @return 示例订单
     */
    InvOrderDO getInvOrder(Long id);

    /**
     * 获得示例订单列表
     * @param invOrderPageReqVO
     * @return
     */
    PageResult<InvOrderDO> getInvPageOrder(InvOrderPageReqVO invOrderPageReqVO);
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
    void refundInvOrder(LoginUser loginUser,Long id, String userIp);

    /**
     * 更新示例订单为已退款
     *
     * @param id 编号
     * @param payRefundId 退款订单号
     */
    void updateInvOrderRefunded(Long id, Long payRefundId);

}
