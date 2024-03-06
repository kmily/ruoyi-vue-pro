package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.admin.wallet.vo.wallet.PayWalletRespVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.convert.order.PayOrderConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletRechargeMapper;
import cn.iocoder.yudao.module.pay.framework.pay.core.WalletPayClient;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.YouyouDetailsRespVO;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.YouyouOrderRespVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenYoupinApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByIdRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByTemplateRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateOrderCancel;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.CreateReqOrder;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.*;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.InvOrderResp;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.devaccount.DevAccountMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails.YouyouDetailsMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.TradeUrlStatus;
import cn.iocoder.yudao.module.steam.service.youyoudetails.YouyouDetailsService;
import cn.iocoder.yudao.module.steam.service.youyouorder.YouyouOrderService;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.RandomStringUtils;
import org.aspectj.apache.bcel.classfile.Code;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZoneOffset;
import java.util.*;

import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

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

    @Autowired
    private PayWalletRechargeService payWalletRechargeService;

    @Autowired
    private DevAccountService devAccountService;

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private PayWalletRechargeMapper payWalletRechargeMapper;

    @Resource
    private PayWalletMapper payWalletMapper;

    @Resource
    private DevAccountMapper devAccountMapper;

    @Resource
    private YouyouDetailsMapper youyouDetailsMapper;

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Resource
    private PaySteamOrderService paySteamOrderService;
    @Resource
    private PayOrderService payOrderService;

    @Autowired
    private UUOrderService uUOrderService;

    @Autowired
    private YouyouOrderService youyouOrderService;

    @Autowired
    private YouyouDetailsService youyouDetailsService;


    /**
     * api余额接口
     *
     * @return
     */
    @PostMapping("v1/api/getAssetsInfo")
    @Operation(summary = "余额查询")
    @PermitAll
    public ApiResult<ApiPayWalletRespVO> getAssetsInfo(@RequestBody OpenYoupinApiReqVo<Serializable> openApiReqVo) {
        try {
            ApiResult<ApiPayWalletRespVO> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                PayWalletDO wallet = payWalletService.getOrCreateWallet(devAccount.getUserId(), devAccount.getUserType());
                ApiPayWalletRespVO apiPayWalletRespVO = new ApiPayWalletRespVO();
                apiPayWalletRespVO.setAmount(new BigDecimal(wallet.getBalance().toString()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
                apiPayWalletRespVO.setUserId(devAccount.getUserId().intValue());
                return ApiResult.success(apiPayWalletRespVO);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), ApiPayWalletRespVO.class);
        }
    }

    /**
     * 检查交易链接
     *
     * @return
     */
    @PostMapping("v1/api/checkTradeUrl")
    @Operation(summary = "验证交易链接")
    @PermitAll
    public ApiResult<ApiCheckTradeUrlReSpVo> checkTradeUrl(@RequestBody OpenYoupinApiReqVo<ApiCheckTradeUrlReqVo> openApiReqVo) {
        try {
            ApiResult<ApiCheckTradeUrlReSpVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                        .eq(BindUserDO::getUserId, devAccount.getUserId())
                        .ne(BindUserDO::getTradeUrl, openApiReqVo.getData().getTradeLinks())
                        .eq(BindUserDO::getUserType, devAccount.getUserType())).stream().findFirst();
                if (!first.isPresent()) {
                    throw new ServiceException(-1, "没有检测机器人");
                }
                BindUserDO bindUserDO = first.get();
                SteamWeb steamWeb = new SteamWeb(configService);
                steamWeb.login(bindUserDO.getSteamPassword(), bindUserDO.getMaFile());
                steamWeb.initTradeUrl();
                TradeUrlStatus tradeUrlStatus = steamWeb.checkTradeUrl(openApiReqVo.getData().getTradeLinks());
                SteamWeb steamWeb1 = new SteamWeb(configService);
                URI uri = URI.create(openApiReqVo.getData().getTradeLinks());
                String query = uri.getQuery();
                Map<String, String> stringStringMap = steamWeb1.parseQuery(query);
                String partner = steamWeb1.toCommunityID(stringStringMap.get("partner"));

                ApiCheckTradeUrlReSpVo tradeUrlReSpVo = new ApiCheckTradeUrlReSpVo();
                tradeUrlReSpVo.setSteamId(partner);
                tradeUrlReSpVo.setMsg(tradeUrlStatus.getMessage());
                tradeUrlReSpVo.setStatus(tradeUrlStatus.getStatus());
                return ApiResult.success(tradeUrlReSpVo);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), ApiCheckTradeUrlReSpVo.class);
        }
    }

    /**
     * 指定商品购买
     *
     * @return
     */
    @PostMapping("v1/api/byGoodsIdCreateOrder")
    @Operation(summary = "指定商品购买")
    @PermitAll
    public ApiResult<CreateByIdRespVo> byGoodsIdCreateOrder(@RequestBody OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByIdRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if (Objects.isNull(openApiReqVo.getData().getCommodityHashName()) || Objects.isNull(openApiReqVo.getData().getCommodityTemplateId())) {
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());

                CreateByIdRespVo ret = new CreateByIdRespVo();
                ret.setPayAmount(Double.valueOf(invOrder.getPayAmount() / 100));
                ret.setOrderNo(invOrder.getOrderNo());
                ret.setMerchantOrderNo(invOrder.getMerchantOrderNo());
                return ApiResult.success(ret);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), CreateByIdRespVo.class);
        }
    }

    /**
     * 指定模板购买
     *
     * @return
     */
    @PostMapping("v1/api/byTemplateCreateOrder")
    @Operation(summary = "指定模板购买")
    @PermitAll
    public ApiResult<CreateByTemplateRespVo> byTemplateCreateOrder(@RequestBody OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByTemplateRespVo> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if (Objects.isNull(openApiReqVo.getData().getCommodityId())) {
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());
                CreateByTemplateRespVo ret = new CreateByTemplateRespVo();
                ret.setPayAmount(Double.valueOf(invOrder.getPayAmount() / 100));
                ret.setOrderNo(invOrder.getOrderNo());
                ret.setMerchantOrderNo(invOrder.getMerchantOrderNo());
                return ApiResult.success(ret);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), CreateByTemplateRespVo.class);
        }
    }

    @Operation(summary = "创建库存订单")
    @PostMapping("v1/api/io661/createInvOrder")
    @PermitAll
    public ApiResult<AppPayOrderSubmitRespVO> createInvOrder(@RequestBody OpenYoupinApiReqVo<PaySteamOrderCreateReqVO> openApiReqVo) {
        try {
            ApiResult<AppPayOrderSubmitRespVO> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);


                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1l);
                CreateOrderResult invOrder = paySteamOrderService.createInvOrder(loginUser, openApiReqVo.getData());

                //付款
                AppPayOrderSubmitReqVO reqVO = new AppPayOrderSubmitReqVO();
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
            return ApiResult.error(e.getCode(), e.getMessage(), AppPayOrderSubmitRespVO.class);
        }

    }

    @Operation(summary = "库存订单列表")
    @PostMapping("v1/api/io661/listInvOrder")
    @PermitAll
    public ApiResult<PageResult> listInvOrder(@RequestBody OpenYoupinApiReqVo<InvOrderPageReqVO> openApiReqVo) {
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

            return ApiResult.error(e.getCode(), e.getMessage(), PageResult.class);
        }
    }

    /**
     * 申请查询明细
     *
     * @return
     */
    @PostMapping("v1/api/detailDataQueryAplly")
    @Operation(summary = "申请查询明细")
    @PermitAll
    public ApiResult<String> detailDataQueryAplly(@RequestBody OpenYoupinApiReqVo<ApiDetailDataQueryApllyReqVo> openApiReqVo) {
        try {
            ApiResult<String> execute = DevAccountUtils.tenantExecute(1l, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                List<YouyouDetailsDO> detailsDOS = youyouDetailsMapper.selectList(new LambdaQueryWrapperX<YouyouDetailsDO>()
                        .eq(YouyouDetailsDO::getAppkey, devAccount.getUserName()));

                Long userId = devAccount.getUserId();
                if (detailsDOS.isEmpty()) {
                    return ApiResult.error(404, "没有数据", String.class);
                }

                String fileName = RandomStringUtils.random(32,
                        new char[]{'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
                Integer dataType = openApiReqVo.getData().getDataType();
                jusDataType(userId, fileName + ".txt", dataType);

                return ApiResult.success(fileName, "成功");
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), String.class);
        }
    }


    @Async
    @OperateLog(type = EXPORT)
    protected void jusDataType(Long userId, String fileName, Integer dataType) {
        String result = "";
        try {
            switch (dataType) {
                case 1:
                    //  1....订单明细
                    List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                            .eq(YouyouOrderDO::getUserId, userId));
                    result = JSON.toJSONString(youyouOrderDOS);
                    break;
                case 2:
                    //  2....资金流水
                    List<PayWalletDO> payWalletDOS = payWalletMapper.selectList(new LambdaQueryWrapperX<PayWalletDO>()
                            .eq(PayWalletDO::getUserId, userId));
                    result = JSON.toJSONString(payWalletDOS);
                    break;
                default:
                    throw new ServiceException(ErrorCodeConstants.YOUYOU_DETAILS_NOT_EXISTS);
            }
            File file = new File("C:\\Users\\WINDOWS\\Desktop\\orderList\\" + fileName);
            if (!file.exists()) {file.createNewFile();}
            FileWriter writer = new FileWriter(file);
            writer.write(result);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*    @PostMapping("v1/api/detailDataQueryResult")
    @Operation(summary = "查询明细结果")
    @PermitAll
    public ApiResult<String> detailDataQueryResult(@RequestBody OpenYoupinApiReqVo<ApiDetailDataQueryApllyReqVo> openApiReqVo) {
        try {
            ApiResult<String> execute = DevAccountUtils.tenantExecute(1l, () -> {
                if (Objects.isNull(openApiReqVo.getData().getDataType())) {
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                YouyouOrderDO youyouOrderDO = new YouyouOrderDO().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setOrderNo(String.valueOf(1L));
                YouyouOrderDO youyouOrderDO1 = youyouOrderService.getYouyouOrder();
                return ApiResult.success("123");
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), String.class);
        }
    }*/

    /**
     * 买家订单相关接口
     */
    @PostMapping("v1/api/orderCancel")
    @Operation(summary = "买家取消订单")
    @PermitAll
    public ApiResult<CreateOrderCancel> orderCancel(@RequestBody OpenYoupinApiReqVo<CreateReqOrder> openApiReqVo) {
        try {
            ApiResult<CreateOrderCancel> execute = DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);

                List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                        .eq(YouyouOrderDO::getOrderNo, openApiReqVo.getData().getOrderNo())
                        .eq(YouyouOrderDO::getUserId, devAccount.getUserId()));

                CreateOrderCancel ret = new CreateOrderCancel();
                if (youyouOrderDOS.size() == 0) {
                    ret.setResult(3);
                    return ApiResult.success(ret, "取消失败，没有找到该订单");
                } else {
                    // TODO 取消订单
//                    youyouOrderMapper.updateById(youyouOrderDOS.get(0).set);
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
    public ApiResult<String> orderStatus(@RequestBody OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        // TODO
        return ApiResult.success("");
    }

    @PostMapping("v1/api/orderInfo")
    @Operation(summary = "查询订单详情")
    @PermitAll
    public ApiResult<String> orderInfo(@RequestBody OpenYoupinApiReqVo<CreateReqVo> openApiReqVo) {
        // TODO
        return ApiResult.success("");
    }
}
