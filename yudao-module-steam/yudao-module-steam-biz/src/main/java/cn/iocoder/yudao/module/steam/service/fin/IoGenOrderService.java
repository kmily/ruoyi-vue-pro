package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;

import javax.validation.Valid;

/**
 * 库存订单 PaySteamOrderService 接口
 *
 * @author glzaboy
 */
public interface IoGenOrderService {
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

}
