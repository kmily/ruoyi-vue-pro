package cn.iocoder.yudao.module.pay.controller.app.wallet;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.admin.wallet.vo.rechargepackage.WalletRechargePackagePageReqVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.recharge.AppPayWalletPackageRespVO;
import cn.iocoder.yudao.module.pay.convert.wallet.WalletRechargePackageConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 钱包充值套餐")
@RestController
@RequestMapping("/pay/wallet-recharge-package")
@Validated
@Slf4j
public class AppPayWalletRechargePackageController {

    @Resource
    private PayWalletRechargePackageService payWalletRechargePackageService;

    @GetMapping("/list")
    @Operation(summary = "获得钱包充值套餐列表")
    public CommonResult<List<AppPayWalletPackageRespVO>> getWalletRechargePackageList() {
        // 只查询开启；需要按照 payPrice 排序；
        PageResult<PayWalletRechargePackageDO> pageResult =  payWalletRechargePackageService.getWalletRechargePackagePage(
                new WalletRechargePackagePageReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus())
        );
        return success(WalletRechargePackageConvert.INSTANCE.convertAppPage(pageResult).getList());
    }

}
