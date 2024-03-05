package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenYoupinApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByIdRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByTemplateRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.ApiCheckTradeUrlReSpVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.ApiPayWalletRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.fin.YouYouOrderService;
import cn.iocoder.yudao.module.steam.service.steam.TradeUrlStatus;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 兼容有品的开放平台接口
 * 地址基准
 * https://gw-openapi.youpin898.com/open/
 */
@Tag(name = "用户APP - 开放平台")
@RestController
@RequestMapping("openapi")
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

    @Autowired
    private YouYouOrderService youYouOrderService;

    /**
     * api余额接口
     * @return
     */
    @PostMapping("v1/api/getAssetsInfo")
    @Operation(summary = "余额查询")
    @PermitAll
    public ApiResult<ApiPayWalletRespVO> getAssetsInfo(@RequestBody  OpenYoupinApiReqVo<Serializable> openApiReqVo) {
        try {
            ApiResult<ApiPayWalletRespVO> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                PayWalletDO wallet = payWalletService.getOrCreateWallet(devAccount.getUserId(), devAccount.getUserType());
                ApiPayWalletRespVO apiPayWalletRespVO=new ApiPayWalletRespVO();
                apiPayWalletRespVO.setAmount(new BigDecimal(wallet.getBalance().toString()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));
                apiPayWalletRespVO.setUserId(devAccount.getUserId().intValue());
                return ApiResult.success(apiPayWalletRespVO);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),ApiPayWalletRespVO.class);
        }
    }
    /**
     * 检查交易链接
     * @return
     */
    @PostMapping("v1/api/checkTradeUrl")
    @Operation(summary = "验证交易链接")
    @PermitAll
    public ApiResult<ApiCheckTradeUrlReSpVo> checkTradeUrl(@RequestBody  OpenYoupinApiReqVo<ApiCheckTradeUrlReqVo> openApiReqVo) {
        try {
            ApiResult<ApiCheckTradeUrlReSpVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                        .eq(BindUserDO::getUserId, devAccount.getUserId())
                        .ne(BindUserDO::getTradeUrl,openApiReqVo.getData().getTradeLinks())
                        .eq(BindUserDO::getUserType, devAccount.getUserType())).stream().findFirst();
                if(!first.isPresent()){
                    throw new ServiceException(-1,"没有检测机器人");
                }
                BindUserDO bindUserDO = first.get();
                SteamWeb steamWeb=new SteamWeb(configService);
                steamWeb.login(bindUserDO.getSteamPassword(),bindUserDO.getMaFile());
                steamWeb.initTradeUrl();
                TradeUrlStatus tradeUrlStatus = steamWeb.checkTradeUrl(openApiReqVo.getData().getTradeLinks());
                SteamWeb steamWeb1=new SteamWeb(configService);
                URI uri = URI.create(openApiReqVo.getData().getTradeLinks());
                String query = uri.getQuery();
                Map<String, String> stringStringMap = steamWeb1.parseQuery(query);
                String partner = steamWeb1.toCommunityID(stringStringMap.get("partner"));

                ApiCheckTradeUrlReSpVo tradeUrlReSpVo=new ApiCheckTradeUrlReSpVo();
                tradeUrlReSpVo.setSteamId(partner);
                tradeUrlReSpVo.setMsg(tradeUrlStatus.getMessage());
                tradeUrlReSpVo.setStatus(tradeUrlStatus.getStatus());
                return ApiResult.success(tradeUrlReSpVo);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),ApiCheckTradeUrlReSpVo.class);
        }
    }
    /**
     * 指定商品购买
     * @return
     */
    @PostMapping("v1/api/byGoodsIdCreateOrder")
    @Operation(summary = "指定商品购买")
    @PermitAll
    public ApiResult<CreateByIdRespVo> byGoodsIdCreateOrder(@RequestBody  OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByIdRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityHashName()) || Objects.isNull(openApiReqVo.getData().getCommodityTemplateId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = youYouOrderService.createInvOrder(loginUser, openApiReqVo.getData());

                CreateByIdRespVo ret=new CreateByIdRespVo();
                ret.setPayAmount(Double.valueOf(invOrder.getPayAmount()/100));
                ret.setOrderNo(invOrder.getOrderNo());
                ret.setMerchantOrderNo(invOrder.getMerchantOrderNo());
                return ApiResult.success(ret);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),CreateByIdRespVo.class);
        }
    }
    /**
     * 指定模板购买
     * @return
     */
    @PostMapping("v1/api/byTemplateCreateOrder")
    @Operation(summary = "指定模板购买")
    @PermitAll
    public ApiResult<CreateByTemplateRespVo> byTemplateCreateOrder(@RequestBody  OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByTemplateRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = youYouOrderService.createInvOrder(loginUser, openApiReqVo.getData());
                CreateByTemplateRespVo ret=new CreateByTemplateRespVo();
                ret.setPayAmount(Double.valueOf(invOrder.getPayAmount()/100));
                ret.setOrderNo(invOrder.getOrderNo());
                ret.setMerchantOrderNo(invOrder.getMerchantOrderNo());
                return ApiResult.success(ret);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),CreateByTemplateRespVo.class);
        }
    }
//    @PostMapping("/createInvOrder")
//    @Operation(summary = "创建库存订单")
//    public CommonResult<AppPayOrderSubmitRespVO> createInvOrder(@Valid @RequestBody PaySteamOrderCreateReqVO createReqVO) {
//        DevAccountDO devAccount = DevAccountContextHolder.getRequiredDevAccount();
//        LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
//        CreateOrderResult invOrder = paySteamOrderService.createInvOrder(loginUser, createReqVO);
//
//        //付款
//        AppPayOrderSubmitReqVO reqVO=new AppPayOrderSubmitReqVO();
//        reqVO.setChannelCode(PayChannelEnum.WALLET.getCode());
//        reqVO.setId(invOrder.getPayOrderId());
//        if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getCode())) {
//            Map<String, String> channelExtras = reqVO.getChannelExtras() == null ?
//                    Maps.newHashMapWithExpectedSize(2) : reqVO.getChannelExtras();
//            channelExtras.put(WalletPayClient.USER_ID_KEY, String.valueOf(devAccount.getUserId()));
//            channelExtras.put(WalletPayClient.USER_TYPE_KEY, String.valueOf(devAccount.getUserType()));
//            reqVO.setChannelExtras(channelExtras);
//        }
//        // 2. 提交支付
//        PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, getClientIP());
//        return success(PayOrderConvert.INSTANCE.convert3(respVO));
//    }
//    @PostMapping("/list/invOrder")
//    @Operation(summary = "库存订单列表")
//    public CommonResult<PageResult<InvOrderResp>> listInvOrder(@Valid @RequestBody InvOrderPageReqVO invOrderPageReqVO) {
//        DevAccountDO devAccount = DevAccountContextHolder.getRequiredDevAccount();
//        invOrderPageReqVO.setUserId(devAccount.getId());
//        invOrderPageReqVO.setUserType(devAccount.getUserType());
//        PageResult<InvOrderResp> invOrderPageOrder = paySteamOrderService.getInvOrderPageOrder(invOrderPageReqVO);
//        return CommonResult.success(invOrderPageOrder);
//    }
}
