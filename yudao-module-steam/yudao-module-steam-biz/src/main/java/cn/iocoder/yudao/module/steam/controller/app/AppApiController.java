package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.utils.DevAccountContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户APP - 开放平台")
@RestController
@RequestMapping("api")
@Validated
public class AppApiController {
    @Resource
    private OpenApiService openApiService;

    @Resource
    private PayWalletService payWalletService;
    /**
     * 类别选择
     */
    @PostMapping("/openapi")
    @Operation(summary = "")
    public CommonResult<String> openApi(@RequestBody @Validated OpenApiReqVo openApi) {
        try {
            CommonResult<String> execute = TenantUtils.execute(1l, () -> {
                try {
                    String dispatch = openApiService.dispatch(openApi);
                    return CommonResult.success(dispatch);
                } catch (ServiceException e) {
                    return CommonResult.error(new ErrorCode(-1, "出错:" + e.getMessage()));
                }
            });
            return execute;
        } catch (ServiceException e) {
            return CommonResult.error(new ErrorCode(-1, "接口出错原因:" + e.getMessage()));
        }
    }

    /**
     * api余额接口
     * @return
     */
    @PostMapping("/getPayWallet")
    @Operation(summary = "查询余额")
    public CommonResult<AppPayWalletRespVO> getPayWallet() {
        DevAccountDO devAccount = DevAccountContextHolder.getRequiredDevAccount();
        PayWalletDO wallet = payWalletService.getOrCreateWallet(devAccount.getUserId(), devAccount.getUserType());
        return success(PayWalletConvert.INSTANCE.convert(wallet));
    }
}
