package cn.iocoder.yudao.module.steam.controller.app.wallet;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayRefundNotifyReqDTO;
import cn.iocoder.yudao.module.steam.controller.app.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "steam后台 - 钱包")
@RestController
@RequestMapping("/steam/wallet")
@Validated
public class AppWalletController {

    @Resource
    private PaySteamOrderService paySteamOrderService;

    /**
     * 创建提现订单
     * @param createReqVO
     * @return
     */
    @PostMapping("/create/withdrawal")
    @Operation(summary = "创建提现订单")
    @PreAuthenticated
    public CommonResult<CreateOrderResult> createWithdrawal(@Valid @RequestBody PayWithdrawalOrderCreateReqVO createReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        CreateOrderResult withdrawalOrder = paySteamOrderService.createWithdrawalOrder(loginUser, createReqVO);
        //自动支付
        return CommonResult.success(withdrawalOrder);
    }
    @PostMapping("/withdrawal/update-paid")
    @Operation(summary = "更新提现订单已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> withdrawalUpdateOrderPaid(@RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        paySteamOrderService.updateWithdrawalOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        return success(true);
    }
    @PostMapping("/create/invOrder")
    @Operation(summary = "创建库存订单")
    @PreAuthenticated
    public CommonResult<CreateOrderResult> createInvOrder(@Valid @RequestBody PaySteamOrderCreateReqVO createReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        CreateOrderResult invOrder = paySteamOrderService.createInvOrder(loginUser, createReqVO);
        return CommonResult.success(invOrder);
    }

    @PostMapping("/update-paid")
    @Operation(summary = "更新示例订单为已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateDemoOrderPaid(@RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        paySteamOrderService.updateInvOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        return success(true);
    }


    @PostMapping("/invOrder/update-refunded")
    @Operation(summary = "更新示例订单为已退款") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateInvOrderRefunded(@RequestBody PayRefundNotifyReqDTO notifyReqDTO) {
        paySteamOrderService.updateInvOrderRefunded(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayRefundId());
        return success(true);
    }
    @PostMapping("/invOrder/refundOrder")
    @Operation(summary = "订单") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    @PreAuthenticated
    public CommonResult<Boolean> refundInvOrder(@RequestParam("id") Long id) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        paySteamOrderService.refundInvOrder(loginUser,id, ServletUtils.getClientIP());
        return success(true);
    }
}
