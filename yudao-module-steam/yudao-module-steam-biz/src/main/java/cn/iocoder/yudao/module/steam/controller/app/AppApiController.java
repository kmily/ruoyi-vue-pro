package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.convert.order.PayOrderConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.framework.pay.core.WalletPayClient;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByIdRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByTemplateRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateOrderCancel;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateOrderStatus;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateReqOrderCancel;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateReqOrderStatus;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReSpVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiPayWalletRespVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.InvOrderResp;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.uu.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderServiceImpl;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.TradeUrlStatus;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;

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
    private YouyouOrderMapper youyouOrderMapper;

    @Resource
    private UUOrderServiceImpl uuOrderServiceImpl;
    @Resource
    private PaySteamOrderService paySteamOrderService;
    @Resource
    private PayOrderService payOrderService;

    @Autowired
    private UUOrderService uUOrderService;

    /**
     * api余额接口
     * @return
     */
    @PostMapping("v1/api/getAssetsInfo")
    @Operation(summary = "余额查询")
    @PermitAll
    public ApiResult<ApiPayWalletRespVO> getAssetsInfo(@RequestBody OpenApiReqVo<Serializable> openApiReqVo) {
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
    public ApiResult<ApiCheckTradeUrlReSpVo> checkTradeUrl(@RequestBody OpenApiReqVo<ApiCheckTradeUrlReqVo> openApiReqVo) {
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
    public ApiResult<CreateByIdRespVo> byGoodsIdCreateOrder(@RequestBody OpenApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByIdRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityHashName()) || Objects.isNull(openApiReqVo.getData().getCommodityTemplateId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());

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
    public ApiResult<CreateByTemplateRespVo> byTemplateCreateOrder(@RequestBody OpenApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByTemplateRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());
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
    @Operation(summary = "创建库存订单")
    @PostMapping("v1/api/io661/createInvOrder")
    @PermitAll
    public ApiResult<AppPayOrderSubmitRespVO> createInvOrder(@RequestBody OpenApiReqVo<PaySteamOrderCreateReqVO> openApiReqVo) {
        try {
            ApiResult<AppPayOrderSubmitRespVO> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);


                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                CreateOrderResult invOrder = paySteamOrderService.createInvOrder(loginUser, openApiReqVo.getData());

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
                PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, ServletUtils.getClientIP());
                return ApiResult.success(PayOrderConvert.INSTANCE.convert3(respVO));
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),AppPayOrderSubmitRespVO.class);
        }

    }
    @Operation(summary = "库存订单列表")
    @PostMapping("v1/api/io661/listInvOrder")
    @PermitAll
    public ApiResult<PageResult> listInvOrder(@RequestBody OpenApiReqVo<InvOrderPageReqVO> openApiReqVo) {
        try {
            ApiResult<PageResult> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                InvOrderPageReqVO data = openApiReqVo.getData();
                data.setUserId(devAccount.getId());
                data.setUserType(devAccount.getUserType());
                PageResult<InvOrderResp> invOrderPageOrder = paySteamOrderService.getInvOrderPageOrder(data);

                return ApiResult.success(invOrderPageOrder);
            });
            return execute;
        } catch (ServiceException e) {

            return ApiResult.error(e.getCode(),  e.getMessage(),PageResult.class);
        }
    }

    /**
     * 买家订单相关接口
     */
    @PostMapping("v1/api/orderCancel")
    @Operation(summary = "买家取消订单")
    @PermitAll
    public ApiResult<CreateOrderCancel> orderCancel(@RequestBody OpenApiReqVo<CreateReqOrderCancel> openApiReqVo) {
        try {
            ApiResult<CreateOrderCancel> execute = DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);

                List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                        .eq(YouyouOrderDO::getOrderNo, openApiReqVo.getData().getOrderNo())
                        .eq(YouyouOrderDO::getUserId, devAccount.getUserId()));

                CreateOrderCancel ret = new CreateOrderCancel();
                if (youyouOrderDOS.isEmpty()) {
                    ret.setResult(3);
                    return ApiResult.success(ret, "取消失败，没有找到该订单");
                } else {
                    LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                    uuOrderServiceImpl.refundInvOrder(loginUser, youyouOrderDOS.get(0).getId(), getClientIP());
                    ret.setResult(1);
                    return ApiResult.success(ret, "取消成功");
                }
                // TODO 文档有个处理中(2)的状态，我感觉应该是用不上
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), CreateOrderCancel.class);
        }
    }


    @PostMapping("v1/api/orderStatus")
    @Operation(summary = "查询订单状态")
    @PermitAll
    public ApiResult<CreateOrderStatus> orderStatus(@RequestBody OpenApiReqVo<CreateReqOrderStatus> openApiReqVo) {
        try {
            ApiResult<CreateOrderStatus> execute = DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);

                List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                        .eq(YouyouOrderDO::getOrderNo, openApiReqVo.getData().getOrderNo())
                        .eq(YouyouOrderDO::getUserId, devAccount.getUserId()));

                CreateOrderStatus ret = new CreateOrderStatus();
                // TODO 根据数据库信息返回详细内容
                if (youyouOrderDOS.isEmpty()) {
                    return ApiResult.success(ret, "商户订单号参数错误");
                } else if (false) {
                    return ApiResult.success(ret, "多种错误类型判断");
                } else {
                    return ApiResult.success(ret, "成功");
                }
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), CreateOrderStatus.class);
        }
    }

    @PostMapping("v1/api/orderInfo")
    @Operation(summary = "查询订单详情")
    @PermitAll
    public ApiResult<String> orderInfo(@RequestBody OpenApiReqVo<CreateReqVo> openApiReqVo) {
        // TODO
        return ApiResult.success("");
    }
}
