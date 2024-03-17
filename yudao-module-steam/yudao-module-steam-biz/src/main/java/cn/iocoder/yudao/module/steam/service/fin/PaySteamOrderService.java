package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.Io661OrderInfoResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;

import javax.validation.Valid;
import java.util.List;

/**
 * 库存订单 PaySteamOrderService 接口
 *
 * @author glzaboy
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
     * 获取订单列表
     * @param reqVo 订单入参
     * @param loginUser 用户
     * @return
     */
    PageResult<Io661OrderInfoResp> getInvOrderWithPage(QueryOrderReqVo reqVo, LoginUser loginUser);
    /**
     * 获取订单详情
     * @param reqVo 订单入参
     * @param loginUser 用户
     * @return
     */
    Io661OrderInfoResp getOrderInfo(QueryOrderReqVo reqVo, LoginUser loginUser);
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
    /**
     * 关闭订单,
     * 用于未支持的订单进行关闭,并释放库存
     *
     * @param id InvOrderId
     */
    void closeUnPayInvOrder(Long id);
//    /**
//     * 关闭已经支付的订单,
//     * 主要用于后期交易失败时恢复库存
//     *
//     * @param id InvOrderId
//     */
//    void closeInvOrder(Long id);
    /**
     * 交易发货
     * @param id 交易订单号 invOrderId
     */
    void tradeAsset(Long id);

    /**
     * 获取当前sellId下的有效订单
     * @param sellId 出售ID
     * @return 返回此sellId下有效订单列表
     */
    List<InvOrderDO> getExpOrder(Long sellId);
//    /**
//     * 违约关闭订单
//     *
//     * @param invOrderId InvOrderId
//     */
//    void damagesCloseInvOrder(Long invOrderId);
//    /**
//     * 订单打款给买家
//     *
//     * @param invOrderId InvOrderId
//     */
//    void cashInvOrder(Long invOrderId);


    /**
     * 关闭订单,
     * 用于未支持的订单进行关闭,并释放库存
     *
     * @param invOrderId InvOrderId
     */
    void checkTransfer(Long invOrderId);

}
