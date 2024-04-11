package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
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
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.module.steam.dal.mysql.withdrawal.WithdrawalMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.WithdrawalAuditStatusEnum;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.hutool.core.util.ObjectUtil.notEqual;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.addTime;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.*;

/**
 * 提现
 */
@Service
@Validated
@Slf4j
public class IoGenOrderServiceImpl implements IoGenOrderService {
    private static final Long PAY_WITHDRAWAL_APP_ID = 10L;
    private static final Long WITHDRAWAL_ACCOUNT_ID = 251L;//UU收款账号ID
    private static final UserTypeEnum WITHDRAWAL_ACCOUNT_TYPE = UserTypeEnum.MEMBER;//UU收款账号ID
    @Resource
    private WithdrawalMapper withdrawalMapper;
    @Resource
    private PayChannelService channelService;
    @Resource
    private ConfigService configService;
    @Resource
    private PayOrderApi payOrderApi;
    private PayWalletService payWalletService;
    @Autowired
    public void setPayWalletService(PayWalletService payWalletService) {
        this.payWalletService = payWalletService;
    }

    @Override
    public CreateOrderResult createWithdrawalOrder(LoginUser loginUser, PayWithdrawalOrderCreateReqVO createReqVO) {
        CreateOrderResult createWithdrawalResult=new CreateOrderResult();
        WithdrawalDO withdrawalDO=new WithdrawalDO().setPayStatus(false)
                .setWithdrawalPrice(createReqVO.getAmount()).setPaymentAmount(createReqVO.getAmount())

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
        if (Objects.isNull(withdrawalDO.getWithdrawalPrice()) || withdrawalDO.getWithdrawalPrice()>5000000L) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_AMOUNT_MAX);
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
//            withdrawalDO.setPaymentAmount(withdrawalDO.getWithdrawalPrice());
        }else{
            withdrawalDO.setServiceFeeRate(withdrawalRateConfig.getValue());
            BigDecimal rate = new BigDecimal(withdrawalRateConfig.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            //四舍五入后金额 分
            BigDecimal serviceFee = new BigDecimal(withdrawalDO.getPaymentAmount()).multiply(rate).setScale(0, RoundingMode.HALF_UP);
            withdrawalDO.setServiceFee(serviceFee.intValue());
            withdrawalDO.setWithdrawalPrice(withdrawalDO.getPaymentAmount()-withdrawalDO.getServiceFee());

//            BigDecimal bigDecimal2 = new BigDecimal(withdrawalDO.getWithdrawalPrice());
//            BigDecimal add = bigDecimal2.add(new BigDecimal(withdrawalDO.getServiceFee()));
//            withdrawalDO.setPaymentAmount(add.intValue());
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
}
