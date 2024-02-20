package cn.iocoder.yudao.module.steam.controller.app.wallet;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayRefundNotifyReqDTO;
import cn.iocoder.yudao.module.pay.controller.admin.demo.vo.order.PayDemoOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
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
    private PaySteamOrderService payDemoOrderService;

    /**
     * 创建提现订单
     * @param createReqVO
     * @return
     */
    @PostMapping("/create/withdrawal")
    @Operation(summary = "创建提现订单")
    @PreAuthenticated
    public CommonResult<Long> createWithdrawal(@Valid @RequestBody PayWithdrawalOrderCreateReqVO createReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long demoOrder = payDemoOrderService.createWithdrawalOrder(loginUser, createReqVO);
        //自动支付

        return CommonResult.success(demoOrder);
    }
//    @PostMapping("/testOrder")
//    @Operation(summary = "创建示例订单")
//    @PermitAll
//    public CommonResult<Long> createDemoOrder(@Valid @RequestBody PayDemoOrderCreateReqVO createReqVO) {
//        TenantUtils.execute(1l,()->{
//            PaySteamOrderCreateReqVO paySteamOrderCreateReqVO=new PaySteamOrderCreateReqVO();
//            paySteamOrderCreateReqVO.setPrice(200);
//            paySteamOrderCreateReqVO.setAssetId("35644141857");
//            paySteamOrderCreateReqVO.setClassId("3035569977");
//            paySteamOrderCreateReqVO.setInstanceId("302028390");
//            paySteamOrderCreateReqVO.setName("测试商品");
//            Long demoOrder = payDemoOrderService.createDemoOrder(1l, paySteamOrderCreateReqVO);
//            return CommonResult.success(demoOrder);
//        });
//        return CommonResult.error(-1,"出错");
//    }
    @PostMapping("/withdrawal/update-paid")
    @Operation(summary = "更新示例订单为已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateDemoOrderPaid(@RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        payDemoOrderService.updateWithdrawalOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        return success(true);
    }
//    @PostMapping("/update-refunded")
//    @Operation(summary = "更新示例订单为已退款") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
//    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
//    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
//    public CommonResult<Boolean> updateDemoOrderRefunded(@RequestBody PayRefundNotifyReqDTO notifyReqDTO) {
//        payDemoOrderService.updateDemoOrderRefunded(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
//                notifyReqDTO.getPayRefundId());
//        return success(true);
//    }
//    @PostMapping("/refundDemoOrder")
//    @Operation(summary = "更新示例订单为已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
//    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
//    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
//    public CommonResult<Boolean> refundDemoOrder(@RequestParam("id") Long id) {
//        payDemoOrderService.refundDemoOrder(id, ServletUtils.getClientIP());
//        return success(true);
//    }
}
