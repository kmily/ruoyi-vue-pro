package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.yudao.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.framework.pay.core.WalletPayClient;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityDO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.CommodityList;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByIdRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateByTemplateRespVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.*;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.ApiDetailDataQueryApllyReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.user.ApiDetailDataQueryResultReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails.YouyouDetailsMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.UUOrderStatus;
import cn.iocoder.yudao.module.steam.enums.UUOrderSubStatus;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.steam.SteamMaFile;
import cn.iocoder.yudao.module.steam.service.steam.TradeUrlStatus;
import cn.iocoder.yudao.module.steam.service.uu.OpenApiService;
import cn.iocoder.yudao.module.steam.service.uu.UUNotifyService;
import cn.iocoder.yudao.module.steam.service.uu.UUService;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReSpVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiPayWalletRespVO;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
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
@Slf4j
public class AppApiController {
    @Resource
    private OpenApiService openApiService;

    @Resource
    private PayWalletService payWalletService;

    private ConfigService configService;
    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Resource
    private PayWalletMapper payWalletMapper;
    @Resource
    private YouyouDetailsMapper youyouDetailsMapper;
    @Resource
    private FileApi fileApi;

    @Resource
    private PayOrderService payOrderService;

    private UUOrderService uUOrderService;
    @Resource
    private UUService uuService;

    @Autowired
    public void setuUOrderService(UUOrderService uUOrderService) {
        this.uUOrderService = uUOrderService;
    }

    @Resource
    private BindUserService bindUserService;
    @Resource
    private UUNotifyService uuNotifyService;

    @Resource
    private UUCommodityMapper uuCommodityMapper;

    @Resource
    private ObjectMapper objectMapper;


    /**
     * 下载UU商品模板
     * @param
     * @return CommodityList
     */
    @PostMapping("/v1/api/templateQuery")
    @Operation(summary = "插入UU商品平台")
    public ApiResult youyouTemplate() throws IOException {
        ApiResult<String> templateId = uuService.getTemplateId();
        log.info("{}",templateId);
        return ApiResult.success(templateId.getData());
        // 提取下载链接中的商品模板
//        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
//        builder.method(HttpUtil.Method.GET).url(templateId.getData());
//        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),HttpUtil.getClient());
//        ArrayList json = sent.json(ArrayList.class);
//        List<ApiUUTemplateVO> templateVOS = new ArrayList<>();
//        for (Object item:json){
//            ApiUUTemplateVO apiUUTemplateVO = objectMapper.readValue(objectMapper.writeValueAsString(item), ApiUUTemplateVO.class);
//            templateVOS.add(apiUUTemplateVO);
//        }
//        for(ApiUUTemplateVO item:templateVOS){
//            YouyouTemplateDO templateDO = new YouyouTemplateDO();
//            templateDO.setTemplateId(item.getId());
//            templateDO.setName(item.getName());
//            templateDO.setHashName(item.getHashName());
//            templateDO.setTypeId(item.getTypeId());
//            templateDO.setTypeHashName(item.getTypeHashName());
//            templateDO.setWeaponId(item.getWeaponId());
//            templateDO.setWeaponName(item.getWeaponName());
//            uuTemplateMapper.insert(templateDO);
//        }
//        return ApiResult.success(templateId.getData());
    }

    /**
     * 查询UU商品列表
     * @param reqVo
     * @return CommodityList
     */
    @PostMapping("/v1/api/goodsQuery")
    @Operation(summary = "查询UU商品列表")
    public ApiResult<CommodityList> youyouCommodityList(@RequestBody ApiUUCommodityReqVO reqVo) throws JsonProcessingException {
        ApiResult<CommodityList> commodityList = uuService.getCommodityList(reqVo);
        String commodityListJson = objectMapper.writeValueAsString(commodityList.getData());
        List<ApiUUCommodityDO> apiUUCommodityDOS = objectMapper.readValue(commodityListJson, new TypeReference<List<ApiUUCommodityDO>>() {});

        YouyouCommodityDO goods = new YouyouCommodityDO();
        for (ApiUUCommodityDO apiUUCommodityDO : apiUUCommodityDOS) {
            goods.setId(apiUUCommodityDO.getId());
            goods.setTemplateId(apiUUCommodityDO.getTemplateId());
            goods.setShippingMode(apiUUCommodityDO.getShippingMode());
            goods.setCommodityName(apiUUCommodityDO.getCommodityName());
            goods.setCommodityPrice(apiUUCommodityDO.getCommodityPrice());
            goods.setCommodityAbrade(apiUUCommodityDO.getCommodityAbrade());
            goods.setCommodityPaintSeed(apiUUCommodityDO.getCommodityPaintSeed());
            goods.setCommodityPaintIndex(apiUUCommodityDO.getCommodityPaintIndex());
            goods.setCommodityHaveNameTag(apiUUCommodityDO.getCommodityHaveNameTag());
            goods.setCommodityHaveBuzhang(apiUUCommodityDO.getCommodityHaveBuzhang());
            goods.setCommodityHaveSticker(apiUUCommodityDO.getCommodityHaveSticker());
            goods.setTemplateisFade(apiUUCommodityDO.getTemplateisFade());
            goods.setTemplateisHardened(apiUUCommodityDO.getTemplateisHardened());
            goods.setTemplateisDoppler(apiUUCommodityDO.getTemplateisDoppler());
            goods.setCommodityStickers(apiUUCommodityDO.getCommodityStickers().toString());
            uuCommodityMapper.insert(goods);
        }
        return commodityList;
    }



//    /**
//     * 批量查询在售商品价格
//     * @param reqVo
//     * @return CommodityList
//     */
//    @PostMapping("/v1/api/goodsQuery")
//    @Operation(summary = "查询UU商品列表")
//    public ApiResult<CommodityList> youyouCommodityList(@RequestBody ApiUUCommodityReqVO reqVo) throws JsonProcessingException {
//
//    }



    /**
     * UU订单服务器回调
     * @param notifyReq UU回调接口
     * @return
     */
    @PostMapping("/uu/notify")
    @Operation(summary = "订单") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    @PermitAll
    public CommonResult<Map<String,Object>> uuNotify(@RequestBody NotifyReq notifyReq) {
        DevAccountUtils.tenantExecute(1L,()->{
            uuNotifyService.notify(notifyReq);
            return null;
        });
        CommonResult<Map<String,Object>> ret=new CommonResult<>();
        ret.setCode(200);
        ret.setMsg("成功");
        ret.setData(MapUtils.convertMap(Arrays.asList(
                new KeyValue<>("messageNo",notifyReq.getMessageNo()),
                new KeyValue<>("flag",true)
        )));
        return ret;
    }
    /**
     * api余额接口
     * @return
     */
    @PostMapping("v1/api/getAssetsInfo")
    @Operation(summary = "余额查询")
    @PermitAll
    public ApiResult<ApiPayWalletRespVO> getAssetsInfo(@RequestBody OpenApiReqVo<Serializable> openApiReqVo) {
        try {
            return DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                PayWalletDO wallet = payWalletService.getOrCreateWallet(devAccount.getUserId(), devAccount.getUserType());
                ApiPayWalletRespVO apiPayWalletRespVO=new ApiPayWalletRespVO();
                apiPayWalletRespVO.setAmount(new BigDecimal(wallet.getBalance().toString()).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP));
                apiPayWalletRespVO.setUserId(devAccount.getUserId().intValue());
                return ApiResult.success(apiPayWalletRespVO);
            });
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
            return DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                BindUserDO bindUserDO = new BindUserDO();
                ConfigDO configMa = configService.getConfigByKey("steam.bot.ma");
                ConfigDO configCookie = configService.getConfigByKey("steam.bot.cookie");
                ConfigDO configPasswd = configService.getConfigByKey("steam.bot.passwd");

                bindUserDO.setSteamPassword(configPasswd.getValue());
                SteamMaFile steamMaFile = JacksonUtils.readValue(Objects.requireNonNull(configMa.getValue(),()->""), SteamMaFile.class);
                bindUserDO.setMaFile(steamMaFile);
                if(Objects.nonNull(configCookie)){
                    bindUserDO.setLoginCookie(Objects.requireNonNull(configCookie.getValue(),()->"登录密码不能为空"));
                }
                SteamWeb steamWeb=new SteamWeb(configService);
                if(steamWeb.checkLogin(bindUserDO)){
                    ConfigSaveReqVO configSaveReqVO=new ConfigSaveReqVO();
                    if(Objects.isNull(configCookie)){
                        configSaveReqVO.setCategory("steam.bot");
                        configSaveReqVO.setName("steam.bot 机器人cookie");
                        configSaveReqVO.setKey("steam.bot.cookie");
                        configSaveReqVO.setValue(steamWeb.getCookieString());
                        configSaveReqVO.setVisible(false);
                        configService.createConfig(configSaveReqVO);
                    }else{
                        configSaveReqVO.setId(configCookie.getId());
                        configSaveReqVO.setCategory("steam.bot");
                        configSaveReqVO.setName("steam.bot 机器人cookie");
                        configSaveReqVO.setKey("steam.bot.cookie");
                        configSaveReqVO.setValue(steamWeb.getCookieString());
                        configSaveReqVO.setVisible(false);
                        configService.updateConfig(configSaveReqVO);
                    }
                }
//                steamWeb.initTradeUrl();
                TradeUrlStatus tradeUrlStatus = steamWeb.checkTradeUrl(openApiReqVo.getData().getTradeLinks());
                URI uri = URI.create(openApiReqVo.getData().getTradeLinks());
                String query = uri.getQuery();
                Map<String, String> stringStringMap = steamWeb.parseQuery(query);
                String partner = steamWeb.toCommunityID(stringStringMap.get("partner"));

                ApiCheckTradeUrlReSpVo tradeUrlReSpVo=new ApiCheckTradeUrlReSpVo();
                tradeUrlReSpVo.setSteamId(partner);
                tradeUrlReSpVo.setMsg(tradeUrlStatus.getMessage());
                tradeUrlReSpVo.setStatus(tradeUrlStatus.getStatus());
                return ApiResult.success(tradeUrlReSpVo);
            });
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
    public ApiResult<CreateByIdRespVo> byGoodsIdCreateOrder(@RequestBody OpenApiReqVo<CreateCommodityOrderReqVo> openApiReqVo) {
        try {
            return DevAccountUtils.tenantExecute(1L, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityHashName()) || Objects.isNull(openApiReqVo.getData().getCommodityTemplateId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1L);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());

                YouyouOrderDO youyouOrderDO = uUOrderService.payInvOrder(loginUser, invOrder.getId());


//
//                //付款
//                AppPayOrderSubmitReqVO reqVO=new AppPayOrderSubmitReqVO();
//                reqVO.setChannelCode(PayChannelEnum.WALLET.getCode());
//                reqVO.setId(invOrder.getPayOrderId());
//                if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getCode())) {
//                    Map<String, String> channelExtras = reqVO.getChannelExtras() == null ?
//                            Maps.newHashMapWithExpectedSize(2) : reqVO.getChannelExtras();
//                    channelExtras.put(WalletPayClient.USER_ID_KEY, String.valueOf(devAccount.getUserId()));
//                    channelExtras.put(WalletPayClient.USER_TYPE_KEY, String.valueOf(devAccount.getUserType()));
//                    reqVO.setChannelExtras(channelExtras);
//                }
//                // 2. 提交支付
//                PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, ServletUtils.getClientIP());
////                AppPayOrderSubmitRespVO appPayOrderSubmitRespVO = PayOrderConvert.INSTANCE.convert3(respVO);
////                YouyouOrderDO uuOrder = uUOrderService.getUUOrder(loginUser,new QueryOrderReqVo().setId(invOrder.getId()));





                CreateByIdRespVo ret=new CreateByIdRespVo();
                ret.setPayAmount(Double.valueOf(youyouOrderDO.getPayAmount()/100));
                ret.setOrderNo(youyouOrderDO.getOrderNo());
                ret.setMerchantOrderNo(youyouOrderDO.getMerchantOrderNo());
                ret.setOrderStatus(PayOrderStatusRespEnum.isSuccess(youyouOrderDO.getPayOrderStatus())?1:0);
                return ApiResult.success(ret);
            });
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
    public ApiResult<CreateByTemplateRespVo> byTemplateCreateOrder(@RequestBody OpenApiReqVo<CreateCommodityOrderReqVo> openApiReqVo) {
        try {
            ApiResult<CreateByTemplateRespVo> execute = DevAccountUtils.tenantExecute(1L, () -> {
                if(Objects.isNull(openApiReqVo.getData().getCommodityId())){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1L);
                YouyouOrderDO invOrder = uUOrderService.createInvOrder(loginUser, openApiReqVo.getData());

                YouyouOrderDO youyouOrderDO = uUOrderService.payInvOrder(loginUser, invOrder.getId());


//                //付款
//                AppPayOrderSubmitReqVO reqVO=new AppPayOrderSubmitReqVO();
//                reqVO.setChannelCode(PayChannelEnum.WALLET.getCode());
//                reqVO.setId(invOrder.getPayOrderId());
//                if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getCode())) {
//                    Map<String, String> channelExtras = reqVO.getChannelExtras() == null ?
//                            Maps.newHashMapWithExpectedSize(2) : reqVO.getChannelExtras();
//                    channelExtras.put(WalletPayClient.USER_ID_KEY, String.valueOf(devAccount.getUserId()));
//                    channelExtras.put(WalletPayClient.USER_TYPE_KEY, String.valueOf(devAccount.getUserType()));
//                    reqVO.setChannelExtras(channelExtras);
//                }
//                // 2. 提交支付
//                PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, ServletUtils.getClientIP());
//                return ApiResult.success(PayOrderConvert.INSTANCE.convert3(respVO));


                CreateByTemplateRespVo ret=new CreateByTemplateRespVo();
                ret.setPayAmount(Double.valueOf(youyouOrderDO.getPayAmount()/100));
                ret.setOrderNo(youyouOrderDO.getOrderNo());
                ret.setMerchantOrderNo(youyouOrderDO.getMerchantOrderNo());
                ret.setOrderStatus(PayOrderStatusRespEnum.isSuccess(youyouOrderDO.getPayOrderStatus())?1:0);
                return ApiResult.success(ret);
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(),  e.getMessage(),CreateByTemplateRespVo.class);
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
    public ApiResult<String> detailDataQueryAplly(@RequestBody OpenApiReqVo<ApiDetailDataQueryApllyReqVo> openApiReqVo) {
        try {
            ApiResult<String> execute = DevAccountUtils.tenantExecute(1L, () -> {
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
                YouyouDetailsDO youyouDetailsDO = new YouyouDetailsDO();
                youyouDetailsDO.setApplyCode(fileName);
                youyouDetailsDO.setDataType(openApiReqVo.getData().getDataType());
                youyouDetailsMapper.updateBatch(youyouDetailsDO);
                return ApiResult.success(fileName, "成功");
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), String.class);
        }
    }

    //TODO  异步优化到其他层
    @Async
    @OperateLog(type = EXPORT)
    protected void jusDataType(Long userId, String fileName, Integer dataType) {
        String result = "";
        try {
            switch (dataType) {
                case 1:
                    //  1....订单明细
                    List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                            .eq(YouyouOrderDO::getBuyUserId, userId));
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
            File file = new File("" + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(result);
            writer.flush();
            writer.close();
            byte[] byte1 = result.getBytes();
            fileApi.createFile(fileName, "", byte1);
            YouyouDetailsDO youyouDetailsDO = new YouyouDetailsDO();
            youyouDetailsDO.setData(fileApi.createFile(fileName, "", byte1));
            youyouDetailsMapper.updateBatch(youyouDetailsDO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询明细结果
     *
     * @return
     */
    @PostMapping("v1/api/detailDataQueryResult")
    @Operation(summary = "查询明细结果")
    @PermitAll
    public ApiResult<String> detailDataQueryResult(@RequestBody OpenApiReqVo<ApiDetailDataQueryResultReqVo> openApiReqVo) {
        try {
            ApiResult<String> execute = DevAccountUtils.tenantExecute(1L, () -> {
                String applycode = openApiReqVo.getData().getApplyCode();
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                List<YouyouDetailsDO> detailsDOS = youyouDetailsMapper.selectList(new LambdaQueryWrapperX<YouyouDetailsDO>()
                        .eq(YouyouDetailsDO::getAppkey, devAccount.getUserName()));
                String url1 = detailsDOS.get(0).getData();
                if (applycode == null) {
                    return ApiResult.error(404, "没有数据", String.class);
                }
                return ApiResult.success((url1), "成功");
            });
            return execute;
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), String.class);
        }
    }



    /**
     * 买家订单相关接口
     */
    @PostMapping("v1/api/orderCancel")
    @Operation(summary = "买家取消订单")
    @PermitAll
    public ApiResult<OrderCancelResp> orderCancel(@RequestBody OpenApiReqVo<OrderCancelVo> openApiReqVo) {
        try {
            return DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1L);
                Integer integer = uUOrderService.refundInvOrder(loginUser, openApiReqVo.getData(), getClientIP());
                OrderCancelResp ret = new OrderCancelResp();
                /*ret.setResult(integer);
                return ApiResult.success(ret);*/
                return null;
            });
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), OrderCancelResp.class);
        }
    }


    @PostMapping("v1/api/orderStatus")
    @Operation(summary = "查询订单状态")
    @PermitAll
    public ApiResult<QueryOrderStatusResp> orderStatus(@RequestBody OpenApiReqVo<QueryOrderReqVo> openApiReqVo) {
        try {
            return DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1L);
                YouyouOrderDO uuOrder = uUOrderService.getUUOrder(loginUser, openApiReqVo.getData());

                QueryOrderStatusResp ret = new QueryOrderStatusResp();
                ret.setOrderNumber(uuOrder.getOrderNo());
                ret.setShippingMode(uuOrder.getShippingMode());
                ret.setTradeOfferId(uuOrder.getUuTradeOfferId());
                ret.setTradeOfferLinks(uuOrder.getUuTradeOfferLinks());
                ret.setBigStatus(uuOrder.getUuOrderStatus());
                if(Objects.nonNull(ret.getBigStatus())){
                    ret.setBigStatusMsg(UUOrderStatus.valueOf(ret.getBigStatus()).getMsg());
                }
                ret.setSmallStatus(uuOrder.getUuOrderSubStatus());
                if(Objects.nonNull(ret.getSmallStatus())){
                    ret.setSmallStatusMsg(UUOrderSubStatus.valueOf(ret.getSmallStatus()).getMsg());
                }
                ret.setFailCode(uuOrder.getUuFailCode());
                ret.setFailReason(uuOrder.getUuFailReason());
                ret.setTradeOfferLinks(uuOrder.getUuTradeOfferLinks());
                return ApiResult.success(ret);
            });
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), QueryOrderStatusResp.class);
        }
    }

    @PostMapping("v1/api/orderInfo")
    @Operation(summary = "查询订单详情")
    @PermitAll
    public ApiResult<OrderInfoResp> orderInfo(@RequestBody OpenApiReqVo<QueryOrderReqVo> openApiReqVo) {
        try {
            return DevAccountUtils.tenantExecute(1L, () -> {
                DevAccountDO devAccount = openApiService.apiCheck(openApiReqVo);
                LoginUser loginUser = new LoginUser().setUserType(devAccount.getUserType()).setId(devAccount.getUserId()).setTenantId(1L);
                YouyouOrderDO uuOrder = uUOrderService.getUUOrder(loginUser, openApiReqVo.getData());
                return ApiResult.success(uUOrderService.orderInfo(uuOrder));
            });
        } catch (ServiceException e) {
            return ApiResult.error(e.getCode(), e.getMessage(), OrderInfoResp.class);
        }
    }
}
