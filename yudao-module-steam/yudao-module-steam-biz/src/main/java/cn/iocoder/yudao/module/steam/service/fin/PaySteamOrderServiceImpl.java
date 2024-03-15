package cn.iocoder.yudao.module.steam.service.fin;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.framework.pay.core.client.dto.transfer.PayTransferRespDTO;
import cn.iocoder.yudao.framework.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.pay.core.enums.transfer.PayTransferTypeEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.pay.api.refund.PayRefundApi;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundRespDTO;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.refund.PayRefundStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.InvOrderResp;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.withdrawal.WithdrawalMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatFormEnum;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.steam.*;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.notEqual;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;

/**
 * 示例订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class PaySteamOrderServiceImpl implements PaySteamOrderService {

    /**
     * 接入的实力应用编号
     *
     * 从 [支付管理 -> 应用信息] 里添加
     */
    private static final Long PAY_APP_ID = 9L;
    private static final Long PAY_WITHDRAWAL_APP_ID = 10L;
    private static final Long WITHDRAWAL_ACCOUNT_ID = 251L;//UU收款账号ID
    private static final UserTypeEnum WITHDRAWAL_ACCOUNT_TYPE = UserTypeEnum.MEMBER;//UU收款账号ID

    private final static String INV_ORDER_PREFIX="INV";


    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private PayRefundApi payRefundApi;


    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private InvOrderMapper invOrderMapper;
    @Resource
    private WithdrawalMapper withdrawalMapper;

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private InvDescMapper invDescMapper;

    private PayWalletService payWalletService;
    @Autowired
    public void setPayWalletService(PayWalletService payWalletService) {
        this.payWalletService = payWalletService;
    }

    @Resource
    private ConfigService configService;

    @Resource
    private PayChannelService channelService;


    private PayOrderService payOrderService;
    @Autowired
    public void setPayOrderService(PayOrderService payOrderService) {
        this.payOrderService = payOrderService;
    }

    @Resource
    private InvPreviewExtService invPreviewExtService;

    @Resource
    private PayNoRedisDAO noRedisDAO;


    public PaySteamOrderServiceImpl() {
    }

    @Override
    public CreateOrderResult createWithdrawalOrder(LoginUser loginUser, PayWithdrawalOrderCreateReqVO createReqVO) {
        CreateOrderResult createWithdrawalResult=new CreateOrderResult();
        WithdrawalDO withdrawalDO=new WithdrawalDO().setPayStatus(false)
                .setWithdrawalPrice(createReqVO.getAmount()).setPaymentAmount(0)

                .setServiceFee(0).setServiceFeeRate("0")
                .setUserId(loginUser.getId()).setUserType(loginUser.getUserType())
                .setRefundPrice(0).setWithdrawalInfo(createReqVO.getWithdrawalInfo())
                .setAuditMsg("").setAuditStatus(0).setAuditUserId(0L).setAuditUserType(UserTypeEnum.MEMBER.getValue())
                .setServiceFeeUserId(WITHDRAWAL_ACCOUNT_ID).setServiceFeeUserType(WITHDRAWAL_ACCOUNT_TYPE.getValue());
        validateWithdrawalCanCreate(withdrawalDO);
        withdrawalMapper.insert(withdrawalDO);
        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_WITHDRAWAL_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(withdrawalDO.getId().toString()) // 业务的订单编号
                .setSubject("订单提现单号"+withdrawalDO.getId()).setBody("用户ID"+loginUser.getId()+"，提现金额"+withdrawalDO.getWithdrawalInfo()+"分").setPrice(withdrawalDO.getPaymentAmount()) // 价格信息
                .setExpireTime(addTime(Duration.ofHours(2L)))); // 支付的过期时间
        // 2.2 更新支付单到 demo 订单
        withdrawalMapper.updateById(new WithdrawalDO().setId(withdrawalDO.getId())
                .setPayOrderId(payOrderId));
        // 返回
        createWithdrawalResult.setPayOrderId(payOrderId);
        createWithdrawalResult.setBizOrderId(withdrawalDO.getId());
        return createWithdrawalResult;
    }

    private WithdrawalDO validateWithdrawalCanCreate(WithdrawalDO withdrawalDO) {
        // 校验订单是否存在
        if (Objects.isNull(withdrawalDO)) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_USER_EXCEPT);
        }
        if (withdrawalDO.getUserId().equals(WITHDRAWAL_ACCOUNT_ID) && withdrawalDO.getUserType().equals(WITHDRAWAL_ACCOUNT_TYPE.getValue())) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_CANNOTDO);
        }
        if (Objects.isNull(withdrawalDO.getUserType()) || withdrawalDO.getUserType()<=0) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_USER_EXCEPT);
        }
        if (Objects.isNull(withdrawalDO.getWithdrawalPrice()) || withdrawalDO.getWithdrawalPrice()<1000L) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_AMOUNT_MIN);
        }
        // 校验订单是否支付
        if (withdrawalDO.getWithdrawalPrice()<0) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_AMOUNT_EXCEPT);
        }

        //计算金额
        ConfigDO withdrawalRateConfig = configService.getConfigByKey("steam.pay.withdrawalRate");

        if(Objects.isNull(withdrawalRateConfig)){
            withdrawalDO.setServiceFeeRate("0");
            withdrawalDO.setServiceFee(0);
            withdrawalDO.setPaymentAmount(withdrawalDO.getWithdrawalPrice());
        }else{
            withdrawalDO.setServiceFeeRate(withdrawalRateConfig.getValue());
            BigDecimal rate = new BigDecimal(withdrawalRateConfig.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            //四舍五入后金额 分
            BigDecimal serviceFee = new BigDecimal(withdrawalDO.getWithdrawalPrice()).multiply(rate).setScale(0, RoundingMode.HALF_UP);
            withdrawalDO.setServiceFee(serviceFee.intValue());
            BigDecimal bigDecimal2 = new BigDecimal(withdrawalDO.getWithdrawalPrice());
            BigDecimal add = bigDecimal2.add(new BigDecimal(withdrawalDO.getServiceFee()));
            withdrawalDO.setPaymentAmount(add.intValue());
        }
        return withdrawalDO;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithdrawalOrderPaid(Long id, Long payOrderId) {
        // 校验并获得支付订单（可支付）
        PayOrderRespDTO payOrder = validateWithdrawalOrderCanPaid(id, payOrderId);

        // 更新 PayDemoOrderDO 状态为已支付
        int updateCount = withdrawalMapper.updateByIdAndPayed(id, false,
                new WithdrawalDO().setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayChannelCode(payOrder.getChannelCode()));
        if (updateCount == 0) {
            throw exception(DEMO_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        //处理帐务
        WithdrawalDO withdrawalDO = withdrawalMapper.selectById(id);
        PayChannelDO channel = channelService.validPayChannel(PAY_WITHDRAWAL_APP_ID, PayChannelEnum.ALIPAY_PC.getCode());
        PayClient client = channelService.getPayClient(channel.getId());
        PayTransferUnifiedReqDTO reqDTO=new PayTransferUnifiedReqDTO();
        reqDTO.setType(PayTransferTypeEnum.ALIPAY_BALANCE.getType());
        reqDTO.setPrice(withdrawalDO.getWithdrawalPrice());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setOutTransferNo("TT"+withdrawalDO.getId());
        reqDTO.setSubject("提现单号"+withdrawalDO.getId());
        reqDTO.setUserName(withdrawalDO.getWithdrawalInfo().getUserName());
        reqDTO.setAlipayLogonId(withdrawalDO.getWithdrawalInfo().getAlipayLogonId());
        PayTransferRespDTO payTransferRespDTO = client.unifiedTransfer(reqDTO);
        log.info("支付宝打款结果{}",payTransferRespDTO);
        withdrawalMapper.updateById(new WithdrawalDO().setId(withdrawalDO.getId()).setPayTransferRet(JacksonUtils.writeValueAsString(payTransferRespDTO)));
        //打款手续费用
        setPayWithdrawalServiceFee(withdrawalDO);
    }
    @Async
    public void setPayWithdrawalServiceFee(WithdrawalDO withdrawalDO) {

        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(withdrawalDO.getServiceFeeUserId(), withdrawalDO.getServiceFeeUserType());
        PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(withdrawalDO.getId()),
                PayWalletBizTypeEnum.SERVICE_FEE, withdrawalDO.getServiceFee());
        withdrawalMapper.updateById(new WithdrawalDO().setId(withdrawalDO.getId()).setServiceFeeRet(JacksonUtils.writeValueAsString(payWalletTransactionDO)));

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
    private PayOrderRespDTO validateWithdrawalOrderCanPaid(Long id, Long payOrderId) {
        // 1.1 校验订单是否存在
        WithdrawalDO withdrawalDO = withdrawalMapper.selectById(id);
        if (withdrawalDO == null) {
            throw exception(DEMO_ORDER_NOT_FOUND);
        }
        // 1.2 校验订单未支付
        if (withdrawalDO.getPayStatus()) {
            log.error("[validateWithdrawalOrderCanPaid][order({}) 不处于待支付状态，请进行处理！order 数据是：{}]",
                    id, toJsonString(withdrawalDO));
            throw exception(DEMO_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        if (WithdrawalAuditStatusEnum.AUDIT.getStatus().equals(withdrawalDO.getAuditStatus())) {
            log.error("[validateWithdrawalOrderCanPaid][order({}) 审核中！order 数据是：{}]",
                    id, toJsonString(withdrawalDO));
            throw exception(ErrorCodeConstants.WITHDRAWAL_AUDIT);
        }
        if (WithdrawalAuditStatusEnum.AUDIT_FAIL.getStatus().equals(withdrawalDO.getAuditStatus())) {
            log.error("[validateWithdrawalOrderCanPaid][order({}) 审核未通过！order 数据是：{}]",
                    id, toJsonString(withdrawalDO));
            throw exception(ErrorCodeConstants.WITHDRAWAL_AUDIT_FAIL);
        }
        // 1.3 校验支付订单匹配
        if (notEqual(withdrawalDO.getPayOrderId(), payOrderId)) { // 支付单号
            log.error("[validateDemoOrderCanPaid][order({}) 支付单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payOrderId, toJsonString(withdrawalDO));
            throw exception(DEMO_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR);
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
            throw exception(DEMO_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_STATUS_NOT_SUCCESS);
        }
        // 2.3 校验支付金额一致
        if (notEqual(payOrder.getPrice(), withdrawalDO.getPaymentAmount())) {
            log.error("[validateDemoOrderCanPaid][order({}) payOrder({}) 支付金额不匹配，请进行处理！order 数据是：{}，payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(withdrawalDO), toJsonString(payOrder));
            throw exception(DEMO_ORDER_UPDATE_PAID_FAIL_PAY_PRICE_NOT_MATCH);
        }
        // 2.4 校验支付订单匹配（二次）
        if (notEqual(payOrder.getMerchantOrderId(), id.toString())) {
            log.error("[validateDemoOrderCanPaid][order({}) 支付单不匹配({})，请进行处理！payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(payOrder));
            throw exception(DEMO_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR);
        }
        return payOrder;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResult createInvOrder(LoginUser loginUser, @Valid PaySteamOrderCreateReqVO reqVo) {
        CreateOrderResult createOrderResult=new CreateOrderResult();
        SellingDO sellingDO = sellingMapper.selectById(reqVo.getSellId());
        InvDescDO invDescDO = invDescMapper.selectById(sellingDO.getInvDescId());
        // 1.1 获得商品
        InvOrderDO invOrderDO = new InvOrderDO().setOrderNo(noRedisDAO.generate(INV_ORDER_PREFIX)).setMerchantNo(reqVo.getMerchantNo())
                .setSellId(reqVo.getSellId()).setSteamId(reqVo.getSteamId())
                .setCommodityAmount(0).setDiscountAmount(0).setServiceFeeRate("0").setServiceFee(0)
                .setPaymentAmount(0)
                .setServiceFeeUserId(WITHDRAWAL_ACCOUNT_ID).setServiceFeeUserType(WITHDRAWAL_ACCOUNT_TYPE.getValue())
                .setPlatformName(reqVo.getPlatform().getName()).setPlatformCode(reqVo.getPlatform().getCode())
                .setSteamId(reqVo.getSteamId())
                .setPayOrderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .setTransferText(new TransferMsg()).setTransferStatus(InvTransferStatusEnum.SELL.getStatus()).setInvDescId(sellingDO.getInvDescId()).setInvId(sellingDO.getInvId())
                //卖家信息
                .setSellCashStatus(InvSellCashStatusEnum.INIT.getStatus()).setSellUserId(sellingDO.getUserId()).setSellUserType(sellingDO.getUserType())
                .setPayStatus(false).setRefundAmount(0).setUserId(loginUser.getId()).setUserType(loginUser.getUserType());
        validateInvOrderCanCreate(invOrderDO);
        invOrderMapper.insert(invOrderDO);

        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(invOrderDO.getId().toString()) // 业务的订单编号
                .setSubject("购买"+invDescDO.getMarketName()).setBody("出售编号："+sellingDO.getId()).setPrice(invOrderDO.getPaymentAmount()) // 价格信息
                .setExpireTime(addTime(Duration.ofHours(2L)))); // 支付的过期时间
        // 2.2 更新支付单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(invOrderDO.getId())
                .setPayOrderId(payOrderId));
        //更新库存的标识
        sellingDO.setTransferStatus(InvTransferStatusEnum.INORDER.getStatus());
        sellingMapper.updateById(sellingDO);
        invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
        // 返回
        createOrderResult.setBizOrderId(invOrderDO.getId());
        createOrderResult.setPayOrderId(payOrderId);
        return createOrderResult;
    }
    private InvOrderDO validateInvOrderCanCreate(InvOrderDO invOrderDO) {
        SellingDO sellingDO = sellingMapper.selectById(invOrderDO.getSellId());
//        //校验订单是否存在
        if (sellingDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(Objects.isNull(sellingDO.getBindUserId())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(invOrderDO.getUserId().equals(sellingDO.getUserId()) && invOrderDO.getUserType().equals(sellingDO.getUserType())){
            throw exception(ErrorCodeConstants.INVORDER_ORDERUSER_EXCEPT);
        }
        //检查是否可交易
        Optional<InvDescDO> first1 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .eq(InvDescDO::getClassid, sellingDO.getClassid())
                .eq(InvDescDO::getInstanceid, sellingDO.getInstanceid())
                .eq(InvDescDO::getAppid, sellingDO.getAppid())
        ).stream().findFirst();
        if(!first1.isPresent()){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        InvDescDO invDescDO = first1.get();
        if(invDescDO.getTradable() !=1){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }

        //库存状态为没有订单
        if(!InvTransferStatusEnum.SELL.getStatus().equals(sellingDO.getTransferStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        //使用库存的价格进行替换
        //各价格计算
        invOrderDO.setCommodityAmount(sellingDO.getPrice());
        ConfigDO serviceFeeLimit = configService.getConfigByKey("steam.inv.serviceFeeLimit");
        ConfigDO serviceFeeRateConfigByKey = configService.getConfigByKey("steam.inv.serviceFeeRate");
        if(PlatFormEnum.WEB.getCode().equals(invOrderDO.getPlatformCode()) || Objects.isNull(serviceFeeRateConfigByKey)){
            invOrderDO.setServiceFeeRate("0");
            invOrderDO.setServiceFee(0);
            invOrderDO.setPaymentAmount(invOrderDO.getCommodityAmount());
        }else{
            invOrderDO.setServiceFeeRate(serviceFeeRateConfigByKey.getValue());
            BigDecimal rate = new BigDecimal(serviceFeeRateConfigByKey.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            //四舍五入后金额
            BigDecimal serviceFee = new BigDecimal(invOrderDO.getCommodityAmount()).multiply(rate).setScale(0, RoundingMode.HALF_UP);


            if(Objects.nonNull(serviceFeeLimit.getValue())){
                int compareResult = serviceFee.compareTo(new BigDecimal(serviceFeeLimit.getValue()));
                if (compareResult < 0) { // 如果bigDemical1小于bigDemical2
                    invOrderDO.setServiceFee(serviceFee.intValue());
                } else if (compareResult == 0) { // 如果两者相等
                    invOrderDO.setServiceFee(serviceFee.intValue());
                } else { // 如果bigDemical1大于bigDemical2
                    invOrderDO.setServiceFee(new BigDecimal(serviceFeeLimit.getValue()).intValue());
                }
            }else{
                invOrderDO.setServiceFee(serviceFee.intValue());
            }
            BigDecimal bigDecimal2 = new BigDecimal(invOrderDO.getCommodityAmount());
            BigDecimal add = bigDecimal2.add(new BigDecimal(invOrderDO.getServiceFee()));
            invOrderDO.setPaymentAmount(add.intValue());
        }




        //检查是否已经下过单
        List<InvOrderDO> invOrderDOS = getExpOrder(sellingDO.getId());
        if(invOrderDOS.size()>0){
            throw exception(ErrorCodeConstants.INVORDER_ORDERED_EXCEPT);
        }
        // 校验订单是否支付
        if (Objects.isNull(invOrderDO.getPaymentAmount()) || invOrderDO.getPaymentAmount()<=0) {
            throw exception(ErrorCodeConstants.INVORDER_AMOUNT_EXCEPT);
        }
        // 校验订单是否支付
        if (invOrderDO.getPayStatus()) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        //检查用户steamID是否正确只检查bindUser
        Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, invOrderDO.getUserId())
                .eq(BindUserDO::getUserType, invOrderDO.getUserType())
                .eq(BindUserDO::getSteamId, invOrderDO.getSteamId())
        ).stream().findFirst();
        if(!first.isPresent()){
            throw exception(ErrorCodeConstants.INVORDER_BIND_STEAM_EXCEPT);
        }
        BindUserDO bindUserDO = first.get();
        if(Objects.isNull(bindUserDO.getSteamPassword())){
            throw exception(ErrorCodeConstants.INVORDER_BIND_STEAM_EXCEPT);
        }
        return invOrderDO;
    }

    /**
     * 检查是否有有效订单
     */
    public List<InvOrderDO> getExpOrder(Long sellId){
        return invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getSellId, sellId)
                .ne(InvOrderDO::getTransferStatus, InvTransferStatusEnum.CLOSE.getStatus())
                .isNull(InvOrderDO::getPayRefundId)
        );
    }

    @Override
    public InvOrderDO getInvOrder(Long id) {
        return invOrderMapper.selectById(id);
    }

    @Override
    public PageResult<InvOrderResp> getInvOrderPageOrder(InvOrderPageReqVO invOrderPageReqVO) {

        PageResult<InvOrderDO> invOrderDOPageResult = invOrderMapper.selectPage(invOrderPageReqVO);
        List<InvOrderResp> ret=new ArrayList<>();
        if(invOrderDOPageResult.getTotal()>0){
            List<Long> collect = invOrderDOPageResult.getList().stream().map(InvOrderDO::getInvDescId).collect(Collectors.toList());
            Map<Long, InvDescDO> collect1 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .in(InvDescDO::getId, collect)).stream().collect(Collectors.toMap(InvDescDO::getId, o -> o, (n1, n2) -> n1));
            for(InvOrderDO item:invOrderDOPageResult.getList()){
                InvOrderResp invOrderResp=new InvOrderResp();
                invOrderResp.setInvOrderDO(item);
                invOrderResp.setInvDescDO(collect1.get(item.getInvDescId()));
                ret.add(invOrderResp);
            }
        }

        return new PageResult<>(ret, invOrderDOPageResult.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInvOrderPaid(Long id, Long payOrderId) {
        // 校验并获得支付订单（可支付）
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        PayOrderRespDTO payOrder = validateInvOrderCanPaid(id, payOrderId);

        // 更新 PayDemoOrderDO 状态为已支付
        int updateCount = invOrderMapper.updateByIdAndPayed(id, false,
                new InvOrderDO().setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayOrderStatus(payOrder.getStatus())
                        .setPayChannelCode(payOrder.getChannelCode()));
        if (updateCount == 0) {
            throw exception(DEMO_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
//        InvOrderDO invOrderDO = invOrderMapper.selectById(id);

        try{
            PaySteamOrderServiceImpl bean = SpringUtil.getBean(this.getClass());
            bean.tradeAsset(id);
        }catch (Exception e){
            DevAccountUtils.tenantExecute(1L,()->{
                if(Objects.nonNull(invOrderDO)){
                    LoginUser loginUser=new LoginUser();
                    loginUser.setId(invOrderDO.getUserId());
                    loginUser.setUserType(invOrderDO.getUserType());
                    refundInvOrder(loginUser,invOrderDO.getId(), ServletUtils.getClientIP());
                }
                return "";
            });
            log.info("发货异常{}",e);
        }

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
    private PayOrderRespDTO validateInvOrderCanPaid(Long id, Long payOrderId) {
        // 1.1 校验订单是否存在
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        if (invOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        //检测库存状态
        SellingDO sellingDO = sellingMapper.selectById(invOrderDO.getSellId());
        if (sellingDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(Objects.isNull(sellingDO.getBindUserId())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        if(invOrderDO.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_CASHED_CANNOTREFUND);
        }
        // 1.2 校验订单未支付
        if (invOrderDO.getPayStatus()) {
            log.error("[validateDemoOrderCanPaid][order({}) 不处于待支付状态，请进行处理！order 数据是：{}]",
                    id, toJsonString(invOrderDO));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        // 1.3 校验支付订单匹配
        if (notEqual(invOrderDO.getPayOrderId(), payOrderId)) { // 支付单号
            log.error("[validateDemoOrderCanPaid][order({}) 支付单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payOrderId, toJsonString(invOrderDO));
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
        if (notEqual(payOrder.getPrice(), invOrderDO.getPaymentAmount())) {
            log.error("[validateDemoOrderCanPaid][order({}) payOrder({}) 支付金额不匹配，请进行处理！order 数据是：{}，payOrder 数据是：{}]",
                    id, payOrderId, toJsonString(invOrderDO), toJsonString(payOrder));
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
    @Transactional(rollbackFor = Exception.class)
    public void refundInvOrder(LoginUser loginUser,Long id, String userIp) {
        // 1. 校验订单是否可以退款
        InvOrderDO invOrderDO = validateInvOrderCanRefund(id,loginUser);

        // 2.1 生成退款单号
        // 一般来说，用户发起退款的时候，都会单独插入一个售后维权表，然后使用该表的 id 作为 refundId
        // 这里我们是个简单的 demo，所以没有售后维权表，直接使用订单 id + "-refund" 来演示
        String refundId = invOrderDO.getId() + "-refund";
        // 2.2 创建退款单
        Long payRefundId = payRefundApi.createRefund(new PayRefundCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(String.valueOf(invOrderDO.getId())) // 支付单号
                .setMerchantRefundId(refundId)
                .setReason("想退钱").setPrice(invOrderDO.getPaymentAmount()));// 价格信息
        // 2.3 更新退款单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(id)
                .setPayRefundId(payRefundId).setRefundAmount(invOrderDO.getCommodityAmount()));
        //释放库存
        closeInvOrder(id);
    }

    private InvOrderDO validateInvOrderCanRefund(Long id,LoginUser loginUser) {
        // 校验订单是否存在
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        if (invOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        if(invOrderDO.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_CASHED_CANNOTREFUND);
        }
        if(!invOrderDO.getUserId().equals(loginUser.getId())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_USER_ERROR);
        }
        if(!invOrderDO.getUserType().equals(loginUser.getUserType())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_USER_ERROR);
        }
        // 校验订单是否支付
        if (!invOrderDO.getPayStatus()) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_NOT_PAID);
        }
        // 校验订单是否已退款
        if (invOrderDO.getPayRefundId() != null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUNDED);
        }
        //检查是否已发货
        TransferMsg transferText = invOrderDO.getTransferText();
        if(Objects.isNull(transferText) && Objects.isNull(transferText.getTradeofferid())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_TRANSFER_ALERY);
        }
        return invOrderDO;
    }

    @Override
    public void updateInvOrderRefunded(Long id, Long payRefundId) {
        // 1. 校验并获得退款订单（可退款）
        PayRefundRespDTO payRefund = validateInvOrderCanRefunded(id, payRefundId);
        // 2.2 更新退款单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(id)
                .setRefundTime(payRefund.getSuccessTime()).setPayOrderStatus(PayOrderStatusEnum.REFUND.getStatus()));
    }
    @Override
    public void closeUnPayInvOrder(Long invOrderId) {
        InvOrderDO invOrder = getInvOrder(invOrderId);
        if(invOrder.getPayStatus()){
            throw new ServiceException(-1,"订单已支付不支持关闭");
        }
        PayOrderDO order = payOrderService.getOrder(invOrder.getPayOrderId());
        if (PayOrderStatusEnum.isClosed(order.getStatus())) {
            invOrderMapper.updateById(new InvOrderDO().setId(invOrderId).setPayStatus(false).setTransferStatus(InvTransferStatusEnum.CLOSE.getStatus())
                    .setPayOrderStatus(order.getStatus()));
            //释放库存
            SellingDO sellingDO = sellingMapper.selectById(invOrder.getSellId());
            sellingMapper.updateById(new SellingDO().setId(sellingDO.getId()).setTransferStatus(InvTransferStatusEnum.SELL.getStatus()));
            invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
        }
    }
    @Override
    public void closeInvOrder(Long invOrderId) {
        InvOrderDO invOrder = getInvOrder(invOrderId);
        if(invOrder.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw new ServiceException(-1,"订单已经支付给卖家，不支持操作");
        }
        if(invOrder.getSellCashStatus().equals(InvSellCashStatusEnum.DAMAGES.getStatus())){
            throw new ServiceException(-1,"订单已经支付违约金，不支持操作");
        }
        if(!invOrder.getPayStatus()){
            throw new ServiceException(-1,"不支付未支付的订单关闭");
        }
        invOrderMapper.updateById(new InvOrderDO().setId(invOrderId).setTransferStatus(InvTransferStatusEnum.CLOSE.getStatus()));
            //释放库存
        SellingDO sellingDO = sellingMapper.selectById(invOrder.getSellId());
        sellingMapper.updateById(new SellingDO().setId(sellingDO.getId()).setTransferStatus(InvTransferStatusEnum.SELL.getStatus()));
        invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
    }

    /**
     * 扣除商品的2%后退还买家
     * @param invOrderId InvOrderId
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void damagesCloseInvOrder(Long invOrderId) {
        InvOrderDO invOrder = getInvOrder(invOrderId);
        if(Objects.isNull(invOrder)){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        if(!invOrder.getPayStatus()){
            throw new ServiceException(-1,"订单未支付不支持退款");
        }
        closeInvOrder(invOrderId);
        PayOrderDO order = payOrderService.getOrder(invOrder.getPayOrderId());
        if (PayOrderStatusEnum.isSuccess(order.getStatus())) {
            Integer commodityAmount = invOrder.getCommodityAmount();
            BigDecimal divide = new BigDecimal("2").divide(new BigDecimal("100"), 2,RoundingMode.HALF_UP);
            int transferDamagesAmount = divide.multiply(new BigDecimal(commodityAmount.toString())).intValue();
            int transferRefundAmount = commodityAmount - transferDamagesAmount;
//            invOrder.setTransferDamagesAmount(transferDamagesAmount);
//            invOrder.setTransferRefundAmount(transferRefundAmount);
//            invOrder.setTransferDamagesTime(LocalDateTime.now());
            //打款违约金-打款到服务费用户
            PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(invOrder.getServiceFeeUserId(), invOrder.getServiceFeeUserType());
            PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(invOrder.getId()),
                    PayWalletBizTypeEnum.INV_DAMAGES, transferDamagesAmount);
            //获取买家钱包并进行退款
            PayWalletDO orCreateWallet2 = payWalletService.getOrCreateWallet(invOrder.getUserId(), invOrder.getUserType());
            PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet2.getId(), String.valueOf(invOrder.getId()),
                    PayWalletBizTypeEnum.STEAM_REFUND, transferRefundAmount);

            List<PayWalletTransactionDO> payWalletTransactionDOS = Arrays.asList(payWalletTransactionDO, payWalletTransactionDO1);
            invOrderMapper.updateById(new InvOrderDO().setId(invOrder.getId())
                    .setTransferDamagesAmount(transferDamagesAmount)
                    .setTransferRefundAmount(transferRefundAmount)
                    .setTransferDamagesTime(LocalDateTime.now())
                    .setTransferDamagesRet(JacksonUtils.writeValueAsString(payWalletTransactionDOS))
                    .setSellCashStatus(InvSellCashStatusEnum.DAMAGES.getStatus())
            );
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void cashInvOrder(Long invOrderId) {
        InvOrderDO invOrder = getInvOrder(invOrderId);
        if(Objects.isNull(invOrder)){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        if(!invOrder.getPayStatus()){
            throw new ServiceException(-1,"订单未支付不支持打款");
        }
        closeInvOrder(invOrderId);
        PayOrderDO order = payOrderService.getOrder(invOrder.getPayOrderId());
        if (PayOrderStatusEnum.isSuccess(order.getStatus())) {
            //打款服务费
            PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(invOrder.getServiceFeeUserId(), invOrder.getServiceFeeUserType());
            PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(invOrder.getId()),
                    PayWalletBizTypeEnum.INV_SERVICE_FEE, invOrder.getServiceFee());

            invOrderMapper.updateById(new InvOrderDO().setId(invOrder.getId()).setServiceFeeRet(JacksonUtils.writeValueAsString(payWalletTransactionDO)));
            //获取卖家家钱包并进行打款
            PayWalletDO orCreateWallet2 = payWalletService.getOrCreateWallet(invOrder.getSellUserId(), invOrder.getSellUserType());
            PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet2.getId(), String.valueOf(invOrder.getId()),
                    PayWalletBizTypeEnum.STEAM_CASH, invOrder.getCommodityAmount());
            invOrderMapper.updateById(new InvOrderDO().setId(invOrder.getId()).setSellCashRet(JacksonUtils.writeValueAsString(payWalletTransactionDO1)).setSellCashStatus(InvSellCashStatusEnum.CASHED.getStatus()));
        }
    }

    /**
     *
     * @param id 交易订单号 invOrderId
     */
    @Override
//    @Async
    public void tradeAsset(Long id) {
        InvOrderDO invOrder = getInvOrder(id);
        SellingDO sellingDO = sellingMapper.selectById(invOrder.getSellId());
        TransferMsg transferMsg=new TransferMsg();
        try{
            if (CommonStatusEnum.isDisable(sellingDO.getStatus())) {
                throw new ServiceException(-1,"库存已经更新无法进行发货。");
            }
            if (PayOrderStatusEnum.isClosed(sellingDO.getStatus())) {
                throw new ServiceException(-1,"已关闭无法进行发货。");
            }
            if (PayOrderStatusEnum.REFUND.getStatus().equals(sellingDO.getStatus())) {
                throw new ServiceException(-1,"已退款无法进行发货。");
            }
            Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                    .eq(BindUserDO::getUserId, invOrder.getUserId())
                    .eq(BindUserDO::getUserType, invOrder.getUserType())
                    .eq(BindUserDO::getSteamId, invOrder.getSteamId())
            ).stream().findFirst();
            if(!first.isPresent()){
                throw new ServiceException(-1,"收货方绑定关系失败无法发货。");
            }
            BindUserDO bindUserDO = first.get();
            //收货方tradeUrl
            String tradeUrl = bindUserDO.getTradeUrl();
//        SteamWeb steamWeb=new SteamWeb(configService);
//        steamWeb.login(bindUserDO.getSteamPassword(),bindUserDO.getMaFile());
//        steamWeb.initTradeUrl();
//        if(!steamWeb.getTreadUrl().isPresent()){
//
//        }
//        steamWeb.checkTradeUrl(steamWeb.getTreadUrl().get());
            BindUserDO bindUserDO1 = bindUserMapper.selectById(sellingDO.getBindUserId());
            //发货
            SteamWeb steamWeb=new SteamWeb(configService);
            steamWeb.login(bindUserDO1.getSteamPassword(),bindUserDO1.getMaFile());
            SteamInvDto steamInvDto=new SteamInvDto();
            steamInvDto.setAmount(sellingDO.getAmount());
            steamInvDto.setAssetid(sellingDO.getAssetid());
            steamInvDto.setClassid(sellingDO.getClassid());
            steamInvDto.setContextid(sellingDO.getContextid());
            steamInvDto.setInstanceid(sellingDO.getInstanceid());
            steamInvDto.setAppid(sellingDO.getAppid());
            SteamTradeOfferResult trade = steamWeb.trade(steamInvDto, tradeUrl);
            log.info("发货信息{}",trade);
            transferMsg.setTradeofferid(trade.getTradeofferid());
            invOrder.setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus());
            sellingDO.setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus());
            invOrder.setTransferText(transferMsg);
            invOrderMapper.updateById(invOrder);
            sellingMapper.updateById(sellingDO);
        }catch (ServiceException  e){
            log.error("发送失败原因{}",e.getMessage());
            transferMsg.setMsg(e.getMessage());
            invOrder.setTransferStatus(-1);
            invOrder.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
            sellingDO.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
            invOrder.setTransferText(transferMsg);
            invOrderMapper.updateById(invOrder);
            sellingMapper.updateById(sellingDO);
            throw e;
        }
//        if(Objects.nonNull(invOrder.getServiceFee()) && invOrder.getServiceFee()>0){
//            setPayInvOrderServiceFee(invOrder);
//        }

    }

    private PayRefundRespDTO validateInvOrderCanRefunded(Long id, Long payRefundId) {
        // 1.1 校验示例订单
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        if (invOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        // 1.2 校验退款订单匹配
        if (Objects.equals(invOrderDO.getPayRefundId(), payRefundId)) {
            log.error("[validateInvOrderCanRefunded][order({}) 退款单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payRefundId, toJsonString(invOrderDO));
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
        if (notEqual(payRefund.getRefundPrice(), invOrderDO.getPaymentAmount())) {
            log.error("[validateInvOrderCanRefunded][order({}) payRefund({}) 退款金额不匹配，请进行处理！order 数据是：{}，payRefund 数据是：{}]",
                    id, payRefundId, toJsonString(invOrderDO), toJsonString(payRefund));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_PRICE_NOT_MATCH);
        }
        // 2.4 校验退款订单匹配（二次）
        if (notEqual(payRefund.getMerchantOrderId(), id.toString())) {
            log.error("[validateInvOrderCanRefunded][order({}) 退款单不匹配({})，请进行处理！payRefund 数据是：{}]",
                    id, payRefundId, toJsonString(payRefund));
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_FAIL_REFUND_ORDER_ID_ERROR);
        }
        return payRefund;
    }

}
