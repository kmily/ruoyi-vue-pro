package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.pay.api.refund.PayRefundApi;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundRespDTO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.refund.PayRefundStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplatePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderCancelResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderCancelVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderInfoResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.UUOrderStatus;
import cn.iocoder.yudao.module.steam.enums.UUOrderSubStatus;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.InvSellCashStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.YouPingOrder;
import cn.iocoder.yudao.module.steam.service.uu.UUService;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReSpVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyVo;
import cn.iocoder.yudao.module.steam.service.youyoucommodity.YouyouCommodityService;
import cn.iocoder.yudao.module.steam.service.youyoutemplate.UUTemplateService;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.hutool.core.util.ObjectUtil.notEqual;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.PAY_ORDER_NOT_FOUND;

/**
 * 示例订单 Service 实现类
 * 因需要两服务费和商品费用分开收取，所以这里不再使用以前的订单，直接走钱包支付
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class UUOrderServiceImpl implements UUOrderService {

    /**
     * 接入的实力应用编号
     *
     * 从 [支付管理 -> 应用信息] 里添加
     */
    private static final Long PAY_APP_ID = 11L;
    private static final Long PAY_WITHDRAWAL_APP_ID = 12L;
    private static final Long UU_CASH_ACCOUNT_ID = 250L;//UU收款账号ID
    /**
     * 支付单流水的 no 前缀
     */
    private static final String PAY_NO_PREFIX = "YY";

    @Resource
    private PayNoRedisDAO noRedisDAO;


    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private PayRefundApi payRefundApi;



    private SteamService steamService;

    @Autowired
    public void setSteamService(SteamService steamService) {
        this.steamService = steamService;
    }


    @Resource
    private UUService uuService;



    private PayWalletService payWalletService;
    @Autowired
    public void setPayWalletService(PayWalletService payWalletService) {
        this.payWalletService = payWalletService;
    }

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Resource
    private UUCommodityMapper UUCommodityMapper;

    @Resource
    private UUTemplateService uuTemplateService;

    private YouyouCommodityService youyouCommodityService;
    @Autowired
    public void setYouyouCommodityService(YouyouCommodityService youyouCommodityService) {
        this.youyouCommodityService = youyouCommodityService;
    }

    @Resource
    private BindUserMapper bindUserMapper;

    /**
     * 方法只要用于查询，
     * 操作请走API
     */
    @Resource
    private PayOrderService payOrderService;

    @Resource
    private MemberUserApi memberUserApi;

    public UUOrderServiceImpl() {
    }

    @Override
    public YouyouOrderDO createInvOrder(LoginUser loginUser, CreateCommodityOrderReqVo reqVo) {
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        //买家身份检测
        BindUserDO buyBindUserDO=null;
        if(StringUtils.hasText(reqVo.getTradeLinks())){
            buyBindUserDO = steamService.getTempBindUserByLogin(loginUser, reqVo.getTradeLinks(),true);
        }
        if(Objects.isNull(buyBindUserDO)){
            throw new ServiceException(OpenApiCode.ERR_5201);
        }

        BigDecimal bigDecimal = new BigDecimal(reqVo.getPurchasePrice());





        YouyouOrderDO youyouOrderDO=new YouyouOrderDO()
                //设置买家
                .setBuyUserId(loginUser.getId()).setBuyUserType(loginUser.getUserType())
                .setBuyBindUserId(buyBindUserDO.getId()).setBuySteamId(buyBindUserDO.getSteamId()).setBuyTradeLinks(buyBindUserDO.getTradeUrl())
                //专家信息
                .setSellUserId(UU_CASH_ACCOUNT_ID).setSellUserType(UserTypeEnum.MEMBER.getValue())
                .setSellCashStatus(InvSellCashStatusEnum.INIT.getStatus())
                //订单信息
                .setOrderNo(noRedisDAO.generate(PAY_NO_PREFIX)).setMerchantOrderNo(reqVo.getMerchantOrderNo())

                //设置支付信息
                .setPayStatus(false)
                .setPayOrderStatus(PayOrderStatusEnum.WAITING.getStatus())
                //设置退款
                .setRefundPrice(0)
                //设置商品信息
                .setCommodityId(reqVo.getCommodityId()).setCommodityHashName(reqVo.getCommodityHashName()).setCommodityTemplateId(reqVo.getCommodityTemplateId())
                .setPurchasePrice(reqVo.getPurchasePrice())
                .setPayAmount(bigDecimal.multiply(new BigDecimal("100")).intValue());
        validateInvOrderCanCreate(youyouOrderDO);
        youyouOrderMapper.insert(youyouOrderDO);
//        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(youyouOrderDO.getId().toString()) // 业务的订单编号
                .setSubject("购买").setBody("出售编号："+reqVo.getCommodityId()).setPrice(youyouOrderDO.getPayAmount()) // 价格信息
                .setExpireTime(addTime(Duration.ofHours(2L)))); // 支付的过期时间
        youyouOrderDO.setPayOrderId(payOrderId);
//        // 2.2 更新支付单到 demo 订单
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(youyouOrderDO.getId())
                .setPayOrderId(payOrderId));
        YouyouCommodityDO youyouCommodityDO = UUCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());
//        //更新库存的标识
        youyouCommodityDO.setTransferStatus(InvTransferStatusEnum.INORDER.getStatus());
        UUCommodityMapper.updateById(youyouCommodityDO);
        return youyouOrderDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YouyouOrderDO payInvOrder(LoginUser loginUser, Long invOrderId) {
        QueryOrderReqVo queryOrderReqVo=new QueryOrderReqVo();
        queryOrderReqVo.setId(invOrderId);
        YouyouOrderDO uuOrder = getUUOrder(loginUser, queryOrderReqVo);
        if(Objects.isNull(uuOrder)){
            log.error("订单不存在{}",JacksonUtils.writeValueAsString(invOrderId));
            throw new ServiceException(OpenApiCode.CONCAT_ADMIN);
        }
        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(uuOrder.getBuyUserId(), uuOrder.getBuyUserType());
        if(Objects.isNull(orCreateWallet) || orCreateWallet.getBalance()<uuOrder.getPayAmount()){
            throw exception(OpenApiCode.ERR_5212);
        }
        //生成支付流水
        //扣除服务费
        PayWalletTransactionDO payWalletTransactionDO = payWalletService.reduceWalletBalance(orCreateWallet.getId(), uuOrder.getId(),
                PayWalletBizTypeEnum.INV_SERVICE_FEE, uuOrder.getServiceFee());
        //获取卖家家钱包并进行打款
        PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.reduceWalletBalance(orCreateWallet.getId(),uuOrder.getId(),
                PayWalletBizTypeEnum.STEAM_CASH, uuOrder.getCommodityAmount());
        List<PayWalletTransactionDO> payWalletTransactionDOS = Arrays.asList(payWalletTransactionDO, payWalletTransactionDO1);
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(uuOrder.getId()).setPayPayRet(JacksonUtils.writeValueAsString(payWalletTransactionDOS)).setPayStatus(true)
                .setPayOrderStatus(PayOrderStatusEnum.SUCCESS.getStatus()));
        YouyouOrderDO uuOrder1 = getUUOrder(loginUser, queryOrderReqVo);
//        try{
            YouPingOrder youPingOrder = uploadYY(uuOrder1);
            if(youPingOrder.getCode().intValue()==0){
                //回写数据表
                youyouOrderMapper.updateById(new YouyouOrderDO().setId(uuOrder1.getId()).
                        setUuOrderNo(youPingOrder.getData().getOrderNo())
                        .setUuMerchantOrderNo(youPingOrder.getData().getMerchantOrderNo())
                        .setUuShippingMode(youPingOrder.getData().getShippingMode())
                        .setUuOrderStatus(youPingOrder.getData().getOrderStatus())
                );
            }else{
                throw new ServiceException(youPingOrder.getCode(),youPingOrder.getMsg());
            }
//            if(youPingOrder.getCode().intValue()==0){
//                //获取专家钱包并进行打款
//                PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(youyouOrderDO.getSellUserId(), youyouOrderDO.getSellUserType());
//                payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(youyouOrderDO.getId()),
//                        PayWalletBizTypeEnum.STEAM_CASH, youyouOrderDO.getPayAmount());
//                youyouOrderDO.setSellCashStatus(InvSellCashStatusEnum.CASHED.getStatus());
//                youyouOrderMapper.updateById(youyouOrderDO);
//            }else{
//                throw new ServiceException(-1,"发货失败原因"+youPingOrder.getMsg());
//            }


//        }catch (ServiceException e){
//            log.error("发货失败，自动退款单号{}",uuOrder1);
//            DevAccountUtils.tenantExecute(1L,()->{
//                if(Objects.nonNull(youyouOrderDO)){
//                    LoginUser loginUser=new LoginUser();
//                    loginUser.setId(youyouOrderDO.getBuyUserId());
//                    loginUser.setUserType(youyouOrderDO.getBuyUserType());
//                    OrderCancelVo orderCancelVo=new OrderCancelVo();
//                    orderCancelVo.setOrderNo(youyouOrderDO.getOrderNo());
//                    refundInvOrder(loginUser,orderCancelVo, ServletUtils.getClientIP());
//                }
//                return "";
//            });
//        }

        return uuOrder1;
    }

    private YouyouOrderDO validateInvOrderCanCreate(YouyouOrderDO youyouOrderDO) {
        //检测交易链接
        try{
            ApiResult<ApiCheckTradeUrlReSpVo> apiCheckTradeUrlReSpVoApiResult = uuService.checkTradeUrl(new ApiCheckTradeUrlReqVo().setTradeLinks(youyouOrderDO.getBuyTradeLinks()));
            if(apiCheckTradeUrlReSpVoApiResult.getCode()!=0){
                throw exception(OpenApiCode.CONCAT_ADMIN);
            }
            switch (apiCheckTradeUrlReSpVoApiResult.getData().getStatus()){
                case 1://1：正常交易   6:该账户库存私密无法交易 7:该账号个人资料私密无法交易
                    break;
                case 2://2:交易链接格式错误
                    throw exception(OpenApiCode.ERR_5408);
                case 3://3:请稍后重试
                    throw exception(OpenApiCode.ERR_5402);
                case 4://4:账号交易权限被封禁，无法交易
                    throw exception(OpenApiCode.ERR_5406);
                case 5://5:该交易链接已不再可用
                    throw exception(OpenApiCode.ERR_5405);
                case 6:// 6:该账户库存私密无法交易
                    throw exception(OpenApiCode.ERR_5403);
                case 7://7:该账号个人资料私密无法交易
                    throw exception(OpenApiCode.ERR_5403);
                default:
                    throw exception(OpenApiCode.CONCAT_ADMIN);
            }
        }catch (ServiceException e){
            throw exception(OpenApiCode.ERR_5408);
        }
        //检测交易链接是否是自己
        Long aLong = bindUserMapper.selectCount(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, youyouOrderDO.getBuyUserId())
                .eqIfPresent(BindUserDO::getUserType, youyouOrderDO.getBuyUserType())
                .eqIfPresent(BindUserDO::getTradeUrl, youyouOrderDO.getBuyTradeLinks())
        );
        if(aLong>0){
            throw exception(OpenApiCode.ERR_5407);
        }
        if(Objects.isNull(youyouOrderDO.getCommodityHashName()) && Objects.isNull(youyouOrderDO.getCommodityTemplateId()) && Objects.isNull(youyouOrderDO.getCommodityId())){
            throw exception(ErrorCodeConstants.UU_GOODS_ERR);
        }
        youyouOrderDO.setRealCommodityId(youyouOrderDO.getCommodityId());
        if(Objects.isNull(youyouOrderDO.getCommodityId())){//指定ID购买
            //todo 查询出一个商品并获取商品ID
            youyouOrderDO.setRealCommodityId("1");
            //查询不到则返回
            throw exception(OpenApiCode.ERR_5213);
        }
        if(Objects.isNull(youyouOrderDO.getRealCommodityId())){
            throw exception(OpenApiCode.ERR_5214);
        }
        // TODO 如果数据出错  请检查此处 mapper 是否正确
        YouyouCommodityDO youyouCommodityDO = UUCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());


        //校验商品是否存在
        if (Objects.isNull(youyouCommodityDO)) {
            throw exception(ErrorCodeConstants.UU_GOODS_NOT_FOUND);
        }
        if(CommonStatusEnum.isDisable(youyouCommodityDO.getStatus())){
            throw exception(OpenApiCode.ERR_5299);
        }
//        if(Objects.isNull(youyouCommodityDO.getBindUserId())){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//        if(invOrderDO.getUserId().equals(sellingDO.getUserId()) && invOrderDO.getUserType().equals(sellingDO.getUserType())){
//            throw exception(ErrorCodeConstants.INVORDER_ORDERUSER_EXCEPT);
//        }
//        //库存状态为没有订单
        if(!InvTransferStatusEnum.SELL.getStatus().equals(youyouCommodityDO.getTransferStatus())){
            throw exception(OpenApiCode.ERR_5214);
        }
        //新的采购服务费标准为收取开放平台采购成交订单金额的5%，单笔采购服务费最多收取500元。本次调整自2024年3月11日10时起生效。
        BigDecimal commodityAmount = new BigDecimal(youyouCommodityDO.getCommodityPrice());
        youyouOrderDO.setCommodityAmount(commodityAmount.multiply(new BigDecimal("100")).intValue());
        BigDecimal totalAmount = commodityAmount.multiply(new BigDecimal("105"));
        if(totalAmount.subtract(commodityAmount).intValue()<50000){
            youyouOrderDO.setPayAmount(totalAmount.intValue());
            youyouOrderDO.setServiceFee(totalAmount.subtract(commodityAmount).intValue());
            youyouOrderDO.setServiceFeeRate("5");
            youyouOrderDO.setServiceFee(totalAmount.subtract(commodityAmount).intValue());
        }else{
            youyouOrderDO.setPayAmount(commodityAmount.add(new BigDecimal("50000")).intValue());
            youyouOrderDO.setServiceFeeRate("5");
            youyouOrderDO.setServiceFee(new BigDecimal("50000").intValue());
        }

        //判断用户钱包是否有足够的钱
        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(youyouOrderDO.getBuyUserId(), youyouOrderDO.getBuyUserType());
        if(Objects.isNull(orCreateWallet) || orCreateWallet.getBalance()<youyouOrderDO.getPayAmount()){
            throw exception(OpenApiCode.ERR_5212);
        }

        //检查是否已经下过单
        List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                        .eq(YouyouOrderDO::getRealCommodityId, youyouOrderDO.getRealCommodityId())
                        .in(YouyouOrderDO::getPayStatus, Arrays.asList(PayOrderStatusEnum.WAITING.getStatus(),PayOrderStatusEnum.SUCCESS.getStatus()))
                        .isNull(YouyouOrderDO::getPayRefundId)
        );
        if(youyouOrderDOS.size()>0){
            throw exception(ErrorCodeConstants.UU_GOODS_ORDERED_EXCEPT);
        }
        // 校验订单是否支付
        if (Objects.isNull(youyouOrderDO.getPayAmount()) || youyouOrderDO.getPayAmount()<=0) {
            throw exception(ErrorCodeConstants.UU_GOODS_AMOUNT_EXCEPT);
        }
        // 校验订单是否支付
        if (youyouOrderDO.getPayStatus()) {
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
//        //检查用户steamID是否正确只检查bindUser
//        Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
//                .eq(BindUserDO::getUserId, invOrderDO.getUserId())
//                .eq(BindUserDO::getUserType, invOrderDO.getUserType())
//                .eq(BindUserDO::getSteamId, invOrderDO.getSteamId())
//        ).stream().findFirst();
//        if(!first.isPresent()){
//            throw exception(ErrorCodeConstants.INVORDER_BIND_STEAM_EXCEPT);
//        }
//        BindUserDO bindUserDO = first.get();
//        if(Objects.isNull(bindUserDO.getSteamPassword())){
//            throw exception(ErrorCodeConstants.INVORDER_BIND_STEAM_EXCEPT);
//        }
        return youyouOrderDO;
    }
    @Override
    public YouyouOrderDO getUUOrder(LoginUser loginUser, QueryOrderReqVo queryOrderReqVo) {
        LambdaQueryWrapperX<YouyouOrderDO> uuOrderDOLambdaQueryWrapperX = new LambdaQueryWrapperX<YouyouOrderDO>()
                .eqIfPresent(YouyouOrderDO::getBuyUserId, loginUser.getId())
                .eqIfPresent(YouyouOrderDO::getBuyUserType, loginUser.getUserType());
        if(Objects.nonNull(queryOrderReqVo.getOrderNo())){
            uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getOrderNo, queryOrderReqVo.getOrderNo());
        }else{
            if(Objects.isNull(queryOrderReqVo.getMerchantNo())){
                throw exception(OpenApiCode.JACKSON_EXCEPTION);
            }else{
                uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getMerchantOrderNo, queryOrderReqVo.getMerchantNo());
            }
        }
        if(Objects.nonNull(queryOrderReqVo.getId())){
            uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getId, queryOrderReqVo.getId());
        }
        List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(uuOrderDOLambdaQueryWrapperX);
        if(youyouOrderDOS.isEmpty()){
            throw exception(OpenApiCode.JACKSON_EXCEPTION);
        }else{
            return youyouOrderDOS.get(0);
        }
    }

    @Override
    public OrderInfoResp orderInfo(YouyouOrderDO uuOrder) {
        YouyouCommodityDO youyouCommodity = youyouCommodityService.getYouyouCommodity(Integer.valueOf(uuOrder.getRealCommodityId()));
        Optional<YouyouTemplateDO> first = uuTemplateService.getYouyouTemplatePage(new YouyouTemplatePageReqVO().setTemplateId(youyouCommodity.getTemplateId())).getList().stream().findFirst();
        PayOrderDO payOrder = payOrderService.getOrder(uuOrder.getPayOrderId());
        //买家
        MemberUserRespDTO buyUser = memberUserApi.getUser(uuOrder.getBuyUserId());
        //卖家
        MemberUserRespDTO sellUser = memberUserApi.getUser(uuOrder.getSellUserId());

        YouyouTemplateDO youyouTemplateDO;
        if(first.isPresent()){
            youyouTemplateDO = first.get();
        }else{
            throw new ServiceException(OpenApiCode.CONCAT_ADMIN);
        }

        OrderInfoResp ret = new OrderInfoResp();
        ret.setOrderNo(uuOrder.getOrderNo());
        ret.setCreateOrderTime(uuOrder.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        if(Objects.nonNull(uuOrder.getPayTime())){
            ret.setPaySuccessTime(uuOrder.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        if(Objects.nonNull(payOrder)){
            ret.setPayEndTime(payOrder.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }


        ret.setTradeUrl(uuOrder.getBuyTradeLinks());
        ret.setOrderStatus(uuOrder.getUuOrderStatus());
        if(Objects.nonNull(ret.getOrderStatus())){
            ret.setOrderStatusName(UUOrderStatus.valueOf(ret.getOrderStatus()).getMsg());
        }
        ret.setOrderSubStatus(uuOrder.getUuOrderSubStatus());
        if(Objects.nonNull(ret.getOrderSubStatus())){
            ret.setOrderSubStatusName(UUOrderSubStatus.valueOf(ret.getOrderSubStatus()).getMsg());
        }
        ret.setBuyerUserId(uuOrder.getBuyUserId());
        ret.setBuyerUserName(buyUser.getNickname());
        if(StringUtils.hasText(buyUser.getAvatar())){
            ret.setBuyerUserIcon(buyUser.getAvatar());
        }else{
            ret.setBuyerUserIcon("https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png");
        }



        ret.setSellerUserId(uuOrder.getSellUserId());
        ret.setSellerUserName(sellUser.getNickname());
        if(StringUtils.hasText(sellUser.getAvatar())){
            ret.setSellerUserIcon(sellUser.getAvatar());
        }else{
            ret.setSellerUserIcon("https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png");
        }
        OrderInfoResp.ProductDetailDTO productDetailDTO = new OrderInfoResp.ProductDetailDTO();
        productDetailDTO.setCommodityId(youyouCommodity.getId());
        productDetailDTO.setCommodityName(youyouCommodity.getCommodityName());
        productDetailDTO.setCommodityHashName(youyouTemplateDO.getHashName());
        productDetailDTO.setCommodityTemplateId(youyouCommodity.getTemplateId());
        productDetailDTO.setAbrade(youyouCommodity.getCommodityAbrade());
        productDetailDTO.setPrice(new BigDecimal(youyouCommodity.getCommodityPrice()).multiply(new BigDecimal("100")).intValue());
//                productDetailDTO.setNum(new BigDecimal(youyouCommodity.get()).multiply(new BigDecimal("100")).intValue());
        productDetailDTO.setPaintIndex(youyouCommodity.getCommodityPaintIndex());
        productDetailDTO.setPaintSeed(youyouCommodity.getCommodityPaintSeed());
        productDetailDTO.setHaveNameTag(youyouCommodity.getCommodityHaveNameTag());
//                productDetailDTO.setNameTag(youyouCommodity.getn());
        productDetailDTO.setHaveClothSeal(youyouCommodity.getCommodityHaveBuzhang());
//                productDetailDTO.setDopplerColor(youyouCommodity.getd());
//                productDetailDTO.setd(youyouCommodity.get());
        productDetailDTO.setHaveSticker(String.valueOf(youyouCommodity.getCommodityHaveSticker()));

        productDetailDTO.setTypeId(youyouTemplateDO.getTypeId());
        productDetailDTO.setTypeHashName(youyouTemplateDO.getTypeHashName());
//                productDetailDTO.setRarityName(youyouTemplateDO.get());

        productDetailDTO.setTypeId(youyouTemplateDO.getTypeId());
        productDetailDTO.setTypeHashName(youyouTemplateDO.getTypeHashName());
        productDetailDTO.setTypeName(youyouTemplateDO.getTypeName());
        productDetailDTO.setWeaponName(youyouTemplateDO.getWeaponName());
        productDetailDTO.setWeaponId(youyouTemplateDO.getWeaponId());
        productDetailDTO.setWeaponHashName(youyouTemplateDO.getWeaponHashName());
        productDetailDTO.setCommodityAbrade(youyouCommodity.getCommodityAbrade());

        ret.setCommodityAmount(youyouCommodity.getCommodityPrice());
        if(Objects.nonNull(uuOrder.getPayAmount())){
            ret.setPaymentAmount(new BigDecimal(uuOrder.getPayAmount()).divide(new BigDecimal("100")).toString());
        }
        if(Objects.nonNull(uuOrder.getPayChannelCode())){
            switch (PayChannelEnum.getByCode(uuOrder.getPayChannelCode())){
                case WALLET:
                    ret.setPayMethod(100);
                    break;
                case ALIPAY_PC:
                case ALIPAY_WAP:
                case ALIPAY_APP:
                case ALIPAY_QR:
                case ALIPAY_BAR:
                    ret.setPayMethod(200);
                    break;
                default:
            }

        }
        ret.setProductDetail(productDetailDTO);
        if(Objects.nonNull(uuOrder.getRefundPrice())){
            ret.setReturnAmount(new BigDecimal(uuOrder.getRefundPrice()).divide(new BigDecimal("100")).toString());
            ret.setCancelOrderTime(uuOrder.getRefundTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        return ret;
    }
    //    @Override
//    public PageResult<YouyouOrderDO> getInvOrderPageOrder(YouyouOrderPageReqVO invOrderPageReqVO) {
//
//        PageResult<YouyouOrderDO> invOrderDOPageResult = youyouOrderMapper.selectPage(invOrderPageReqVO);
//        List<YouyouOrderDO> ret=new ArrayList<>();
//        if(invOrderDOPageResult.getTotal()>0){
//            List<Long> collect = invOrderDOPageResult.getList().stream().map(InvOrderDO::getInvDescId).collect(Collectors.toList());
//            Map<Long, InvDescDO> collect1 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
//                    .in(InvDescDO::getId, collect)).stream().collect(Collectors.toMap(InvDescDO::getId, o -> o, (n1, n2) -> n1));
//            for(InvOrderDO item:invOrderDOPageResult.getList()){
//                InvOrderResp invOrderResp=new InvOrderResp();
//                invOrderResp.setInvOrderDO(item);
//                invOrderResp.setInvDescDO(collect1.get(item.getInvDescId()));
//                ret.add(invOrderResp);
//            }
//        }
//
//        return new PageResult<YouyouOrderDO>(ret,invOrderDOPageResult.getTotal());
//    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public void updateInvOrderPaid(Long id, Long payOrderId) {
        // 校验并获得支付订单（可支付
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(id);
        PayOrderRespDTO payOrder = validateInvOrderCanPaid(id, payOrderId);
        // 更新 状态为已支付
        int updateCount = youyouOrderMapper.updateByIdAndPayed(id, false,
                new YouyouOrderDO().setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayOrderStatus(payOrder.getStatus())
                        .setPayChannelCode(payOrder.getChannelCode()));
        if (updateCount == 0) {
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        try{
            YouPingOrder youPingOrder = uploadYY(youyouOrderDO);
            if(youPingOrder.getCode().intValue()==0){
                //获取专家钱包并进行打款
                PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(youyouOrderDO.getSellUserId(), youyouOrderDO.getSellUserType());
                payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(youyouOrderDO.getId()),
                        PayWalletBizTypeEnum.STEAM_CASH, youyouOrderDO.getPayAmount());
                youyouOrderDO.setSellCashStatus(InvSellCashStatusEnum.CASHED.getStatus());
                youyouOrderMapper.updateById(youyouOrderDO);
            }else{
                throw new ServiceException(-1,"发货失败原因"+youPingOrder.getMsg());
            }


        }catch (ServiceException e){
            log.error("发货失败，自动退款单号{}",youyouOrderDO);
            DevAccountUtils.tenantExecute(1L,()->{
                if(Objects.nonNull(youyouOrderDO)){
                    LoginUser loginUser=new LoginUser();
                    loginUser.setId(youyouOrderDO.getBuyUserId());
                    loginUser.setUserType(youyouOrderDO.getBuyUserType());
                    OrderCancelVo orderCancelVo=new OrderCancelVo();
                    orderCancelVo.setOrderNo(youyouOrderDO.getOrderNo());
                    refundInvOrder(loginUser,orderCancelVo, ServletUtils.getClientIP());
                }
                return "";
            });
        }
    }

    /**
     * 上传订单到有品
     * @param youyouOrderDO
     * @return
     */
    private YouPingOrder uploadYY(YouyouOrderDO youyouOrderDO){
        CreateCommodityOrderReqVo createReqVo = new CreateCommodityOrderReqVo();
        createReqVo.setMerchantOrderNo("YY"+youyouOrderDO.getMerchantOrderNo());
        createReqVo.setTradeLinks(youyouOrderDO.getBuyTradeLinks());
        createReqVo.setCommodityId(youyouOrderDO.getCommodityId());
        createReqVo.setCommodityHashName(youyouOrderDO.getCommodityHashName());
        createReqVo.setPurchasePrice(youyouOrderDO.getPurchasePrice());
        createReqVo.setFastShipping(youyouOrderDO.getFastShipping());
        createReqVo.setCommodityId(youyouOrderDO.getCommodityId());
        ApiResult<YouPingOrder> youPingOrderApiResult = uuService.byGoodsIdCreateOrder(createReqVo);
        uuService.checkResponse(youPingOrderApiResult);
        return youPingOrderApiResult.getData();
    }

    /**
     * 校验交易订单满足被支付的条件
     *
     * 1. 交易订单未支付
     * 2. 支付单已支付
     *
     * @param id 交易订单编号
     * @param payOrderId 支付订单编号
     * @return 交易订单
     */
    @Deprecated
    private PayOrderRespDTO validateInvOrderCanPaid(Long id, Long payOrderId) {
        // 1.1 校验订单是否存在
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(id);
        if (youyouOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        if(youyouOrderDO.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_CASHED_CANNOTREFUND);
        }
        // 1.2 校验订单未支付
        if (youyouOrderDO.getPayStatus()) {
            log.error("[validateDemoOrderCanPaid][order({}) 不处于待支付状态，请进行处理！order 数据是：{}]",
                    id, toJsonString(youyouOrderDO));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        // 1.3 校验支付订单匹配
        if (notEqual(youyouOrderDO.getPayOrderId(), payOrderId)) { // 支付单号
            log.error("[validateDemoOrderCanPaid][order({}) 支付单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payOrderId, toJsonString(youyouOrderDO));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR);
        }

        // 2.1 校验支付单是否存在
        PayOrderRespDTO payOrder = payOrderApi.getOrder(payOrderId);
        if (payOrder == null) {
            log.error("[validateDemoOrderCanPaid][order({}) payOrder({}) 不存在，请进行处理！]", id, payOrderId);
            throw exception(PAY_ORDER_NOT_FOUND);
        }
        // 2.2 校验支付单已支付
        if (!PayOrderStatusEnum.isSuccess(payOrder.getStatus())) {
            log.error("[validateDemoOrderCanPaid][order({}) payOrder({}) 未支付，请进行处理！payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(payOrder));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_STATUS_NOT_SUCCESS);
        }
        // 2.3 校验支付金额一致
        if (notEqual(payOrder.getPrice(), youyouOrderDO.getPayAmount())) {
            log.error("[validateDemoOrderCanPaid][order({}) payOrder({}) 支付金额不匹配，请进行处理！order 数据是：{}，payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(youyouOrderDO), toJsonString(payOrder));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_PRICE_NOT_MATCH);
        }
        // 2.4 校验支付订单匹配（二次）
        if (notEqual(payOrder.getMerchantOrderId(), id.toString())) {
            log.error("[validateDemoOrderCanPaid][order({}) 支付单不匹配({})，请进行处理！payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(payOrder));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR);
        }
        return payOrder;
    }

    @Override
    @Deprecated
    public Integer refundInvOrder(LoginUser loginUser, OrderCancelVo orderCancelVo, String userIp) {
        Optional<YouyouOrderDO> first = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                .eq(YouyouOrderDO::getOrderNo, orderCancelVo.getOrderNo())
                .eq(YouyouOrderDO::getBuyUserId, loginUser.getId())
                .eq(YouyouOrderDO::getBuyUserType, loginUser.getUserType())
        ).stream().findFirst();
        // 1. 校验订单是否可以退款
        YouyouOrderDO youyouOrderDO = validateInvOrderCanRefund(first.orElse(null),loginUser);
        //如果有uu的单子，这里则只发起uu的退款，uu退款后再发起我们的退款
        if(Objects.nonNull(youyouOrderDO.getUuOrderNo())){
            //传入uu的单子
            ApiResult<OrderCancelResp> orderCancelRespApiResult = uuService.orderCancel(new OrderCancelVo().setOrderNo(youyouOrderDO.getUuOrderNo()));
            log.info("取消UU订单结果{}{}",orderCancelRespApiResult,youyouOrderDO);
            if(orderCancelRespApiResult.getData().getResult()==1){
                refundAction(youyouOrderDO,loginUser);
                return 1;
            }
            return orderCancelRespApiResult.getData().getResult();
        }else{
            refundAction(youyouOrderDO,loginUser);
            return 1;
        }
    }
    @Override
    public Integer orderCancel(LoginUser loginUser, OrderCancelVo orderCancelVo, String userIp) {
        Optional<YouyouOrderDO> first = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                .eq(YouyouOrderDO::getOrderNo, orderCancelVo.getOrderNo())
                .eq(YouyouOrderDO::getBuyUserId, loginUser.getId())
                .eq(YouyouOrderDO::getBuyUserType, loginUser.getUserType())
        ).stream().findFirst();
        // 1. 校验订单是否可以退款
        YouyouOrderDO youyouOrderDO = validateInvOrderCanRefund(first.orElse(null),loginUser);
        //如果有uu的单子，这里则只发起uu的退款，uu退款后再发起我们的退款
        if(Objects.nonNull(youyouOrderDO.getUuOrderNo())){
            //传入uu的单子
            ApiResult<OrderCancelResp> orderCancelRespApiResult = uuService.orderCancel(new OrderCancelVo().setOrderNo(youyouOrderDO.getUuOrderNo()));
            log.info("取消UU订单结果{}{}",orderCancelRespApiResult,youyouOrderDO);
            if(orderCancelRespApiResult.getData().getResult()==1){
                refundAction(youyouOrderDO,loginUser);
                return 1;
            }
            return orderCancelRespApiResult.getData().getResult();
        }else{
            refundAction(youyouOrderDO,loginUser);
            return 1;
        }
    }
    /**
     * 执行退款
     * 退款不正走此方法，
     * @param youyouOrderDO
     */
    @Deprecated
    private void refundAction(YouyouOrderDO youyouOrderDO,LoginUser loginUser,String reason) {
        throw new ServiceException(-1,"方法停用");
//        validateInvOrderCanRefund(youyouOrderDO,loginUser);
//        // 2.1 生成退款单号
//        // 一般来说，用户发起退款的时候，都会单独插入一个售后维权表，然后使用该表的 id 作为 refundId
//        // 这里我们是个简单的 demo，所以没有售后维权表，直接使用订单 id + "-refund" 来演示
//        String refundId = youyouOrderDO.getId() + "-refund";
//        // 2.2 创建退款单
//        Long payRefundId = payRefundApi.createRefund(new PayRefundCreateReqDTO()
//                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
//                .setMerchantOrderId(String.valueOf(youyouOrderDO.getId())) // 支付单号
//                .setMerchantRefundId(refundId)
//                .setReason(reason).setPrice(youyouOrderDO.getPayAmount()));// 价格信息
//        // 2.3 更新退款单到 demo 订单
//        youyouOrderMapper.updateById(new YouyouOrderDO().setId(youyouOrderDO.getId())
//                .setPayRefundId(payRefundId).setRefundPrice(youyouOrderDO.getPayAmount()));
//        //释放库存
//        YouyouCommodityDO youyouCommodityDO = UUCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());
//        youyouCommodityDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
//        UUCommodityMapper.updateById(youyouCommodityDO);
    }
    @Deprecated
    private void refundAction(YouyouOrderDO youyouOrderDO,LoginUser loginUser) {
        refundAction(youyouOrderDO,loginUser,"用户不想要了主动退款");
    }

    @Deprecated
    private YouyouOrderDO validateInvOrderCanRefund(YouyouOrderDO youyouOrderDO,LoginUser loginUser) {
        // 校验订单是否存在
        if (Objects.isNull(youyouOrderDO)) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        if(youyouOrderDO.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_CASHED_CANNOTREFUND);
        }
        if(!youyouOrderDO.getBuyUserId().equals(loginUser.getId())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_USER_ERROR);
        }
        if(!youyouOrderDO.getBuyUserType().equals(loginUser.getUserType())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_USER_ERROR);
        }
        // 校验订单是否支付
        if (!youyouOrderDO.getPayStatus()) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_NOT_PAID);
        }
        // 校验订单是否已退款
        if (youyouOrderDO.getPayRefundId() != null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUNDED);
        }
        //通过此接口可取消符合取消规则「创单成功后30min后卖家未发送交易报价」的代购订单。
        if(Objects.nonNull(youyouOrderDO.getUuOrderNo())){
            //orderStatus,140的除 1101外其它状态不能取消，不能取消的还有340，280，360
            if(Arrays.asList(UUOrderStatus.CODE140.getCode(),UUOrderStatus.CODE340.getCode(),UUOrderStatus.CODE280.getCode(),UUOrderStatus.CODE360.getCode()).contains(youyouOrderDO.getUuOrderStatus())){
                if(!youyouOrderDO.getUuOrderStatus().equals(UUOrderSubStatus.SUB_CODE1104.getCode())){
                    throw exception(ErrorCodeConstants.UU_GOODS_ORDER_CAN_NOT_CANCEL);
                }
            }
            Duration between = Duration.between(youyouOrderDO.getCreateTime(), LocalDateTime.now());
            if(between.getSeconds()<=30*60){//小于30分钟不能取消
                throw exception(ErrorCodeConstants.UU_GOODS_ORDER_MIN_TIME);
            }
        }
        //检查是否已发货
        if(InvSellCashStatusEnum.CASHED.getStatus().equals(youyouOrderDO.getSellCashStatus())){
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_TRANSFER_CASHED);
        }
        return youyouOrderDO;
    }

    @Override
    @Deprecated
    public void updateInvOrderRefunded(Long id, Long payRefundId) {
        // 1. 校验并获得退款订单（可退款）
        PayRefundRespDTO payRefund = validateInvOrderCanRefunded(id, payRefundId);
        // 2.2 更新退款单到 demo 订单
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(id)
                .setRefundTime(payRefund.getSuccessTime()).setPayOrderStatus(PayOrderStatusEnum.REFUND.getStatus()));
    }

    @Deprecated
    private PayRefundRespDTO validateInvOrderCanRefunded(Long id, Long payRefundId) {
        // 1.1 校验示例订单
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(id);
        if (youyouOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        // 1.2 校验退款订单匹配
        if (Objects.equals(youyouOrderDO.getPayRefundId(), payRefundId)) {
            log.error("[validateDemoOrderCanRefunded][order({}) 退款单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payRefundId, toJsonString(youyouOrderDO));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_ORDER_ID_ERROR);
        }

        // 2.1 校验退款订单
        PayRefundRespDTO payRefund = payRefundApi.getRefund(payRefundId);
        if (payRefund == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_NOT_FOUND);
        }
        // 2.2
        if (!PayRefundStatusEnum.isSuccess(payRefund.getStatus())) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_NOT_SUCCESS);
        }
        // 2.3 校验退款金额一致
        if (notEqual(payRefund.getRefundPrice(), youyouOrderDO.getPayAmount())) {
            log.error("[validateDemoOrderCanRefunded][order({}) payRefund({}) 退款金额不匹配，请进行处理！order 数据是：{}，payRefund 数据是：{}]",
                    id, payRefundId, toJsonString(youyouOrderDO), toJsonString(payRefund));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_PRICE_NOT_MATCH);
        }
        // 2.4 校验退款订单匹配（二次）
        if (notEqual(payRefund.getMerchantOrderId(), id.toString())) {
            log.error("[validateDemoOrderCanRefunded][order({}) 退款单不匹配({})，请进行处理！payRefund 数据是：{}]",
                    id, payRefundId, toJsonString(payRefund));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_ORDER_ID_ERROR);
        }
        return payRefund;
    }

    @Override
    public void processNotify(NotifyReq notifyReq) {
        String callBackInfo = notifyReq.getCallBackInfo();
        NotifyVo notifyVo = JacksonUtils.readValue(callBackInfo, NotifyVo.class);
        log.info("回调接收的数据{}",notifyVo);
        List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapper<YouyouOrderDO>()
                .eq(YouyouOrderDO::getUuOrderNo, notifyVo.getOrderNo()));
        if (!youyouOrderDOS.isEmpty()) {
            YouyouOrderDO youyouOrderDO = youyouOrderDOS.get(0);
            youyouOrderMapper.updateById(new YouyouOrderDO().setId(youyouOrderDO.getId())
                    .setUuOrderType(notifyVo.getOrderType())
                    .setUuOrderSubType(notifyVo.getOrderSubType())
                    .setUuShippingMode(notifyVo.getShippingMode())
                    .setUuTradeOfferId(notifyVo.getTradeOfferId())
                    .setUuTradeOfferLinks(notifyVo.getTradeOfferLinks())
                    .setUuBuyerUserId(notifyVo.getBuyerUserId())
                    .setUuOrderStatus(notifyVo.getOrderStatus())
                    .setUuOrderSubType(notifyVo.getOrderSubType())
                    .setUuFailCode(notifyVo.getFailCode())
                    .setUuFailReason(notifyVo.getFailReason())
                    .setUuMerchantOrderNo(notifyVo.getMerchantOrderNo())
                    .setUuNotifyType(notifyVo.getNotifyType())
                    .setUuNotifyDesc(notifyVo.getNotifyDesc())
            );
            if(notifyVo.getNotifyType()==4){
                //订单被取消了，走退款
                refundAction(youyouOrderDO,new LoginUser().setTenantId(1L).setUserType(youyouOrderDO.getBuyUserType()).setId(youyouOrderDO.getBuyUserId()));
            }
        }

    }
}
