package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import cn.iocoder.yudao.module.pay.convert.order.PayOrderConvert;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.framework.pay.core.WalletPayClient;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.TradeUrlStatus;
import cn.iocoder.yudao.module.steam.utils.DevAccountContextHolder;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserType;

@Tag(name = "用户APP - 开放平台")
@RestController
@RequestMapping("api")
@Validated
public class AppApiController {
    @Resource
    private OpenApiService openApiService;

    @Resource
    private PayWalletService payWalletService;

    @Autowired
    private ConfigService configService;

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private PaySteamOrderService paySteamOrderService;
    @Resource
    private PayOrderService payOrderService;
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
    /**
     * api余额接口
     * @return
     */
    @PostMapping("/checkTradeUrl")
    @Operation(summary = "检查交易链接")
    public CommonResult<TradeUrlStatus> checkTradeUrl(ApiCheckTradeUrlReqVo apiCheckTradeUrlVo) {
        DevAccountDO devAccount = DevAccountContextHolder.getRequiredDevAccount();
        Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, devAccount.getUserId())
                .ne(BindUserDO::getTradeUrl,apiCheckTradeUrlVo.getTradeUrl())
                .eq(BindUserDO::getUserType, devAccount.getUserType())).stream().findFirst();
        if(!first.isPresent()){
            return success(TradeUrlStatus.NOBOT);
//            return error(-1,"你帐号下没有有效帐号无法检测,请先完成绑定");
        }
        BindUserDO bindUserDO = first.get();
        SteamWeb steamWeb=new SteamWeb(configService);
        steamWeb.login(bindUserDO.getSteamPassword(),bindUserDO.getMaFile());
        steamWeb.initTradeUrl();
        TradeUrlStatus tradeUrlStatus = steamWeb.checkTradeUrl(apiCheckTradeUrlVo.getTradeUrl());
        return success(tradeUrlStatus);
    }

    @PostMapping("/createInvOrder")
    @Operation(summary = "创建库存订单")
    public CommonResult<AppPayOrderSubmitRespVO> createInvOrder(@Valid @RequestBody PaySteamOrderCreateReqVO createReqVO) {
        DevAccountDO devAccount = DevAccountContextHolder.getRequiredDevAccount();
        LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
        CreateOrderResult invOrder = paySteamOrderService.createInvOrder(loginUser, createReqVO);

        //付款
        AppPayOrderSubmitReqVO reqVO=new AppPayOrderSubmitReqVO();
        reqVO.setChannelCode(PayChannelEnum.WALLET.getCode());
        reqVO.setId(invOrder.getPayOrderId());
        if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getCode())) {
            Map<String, String> channelExtras = reqVO.getChannelExtras() == null ?
                    Maps.newHashMapWithExpectedSize(2) : reqVO.getChannelExtras();
            channelExtras.put(WalletPayClient.USER_ID_KEY, String.valueOf(devAccount.getUserId()));
            channelExtras.put(WalletPayClient.USER_TYPE_KEY, String.valueOf(devAccount.getUserType()));
            reqVO.setChannelExtras(channelExtras);
        }
        // 2. 提交支付
        PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, getClientIP());
        return success(PayOrderConvert.INSTANCE.convert3(respVO));
    }
}
