package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.pay.api.refund.PayRefundApi;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundRespDTO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.refund.PayRefundStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
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
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.InvSellCashStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.TransferMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    @Autowired
    private SteamService steamService;

    @Resource
    private InvDescMapper invDescMapper;
    @Autowired
    private PayWalletService payWalletService;


    public PaySteamOrderServiceImpl() {
    }

    @Override
    public CreateOrderResult createWithdrawalOrder(LoginUser loginUser, PayWithdrawalOrderCreateReqVO createReqVO) {
        CreateOrderResult createWithdrawalResult=new CreateOrderResult();
        WithdrawalDO withdrawalDO=new WithdrawalDO().setPayStatus(false)
                .setPrice(createReqVO.getAmount()).setUserId(loginUser.getId()).setUserType(loginUser.getUserType())
                .setRefundPrice(0).setWithdrawalInfo(createReqVO.getWithdrawalInfo());
        validateWithdrawalCanCreate(withdrawalDO);
        withdrawalMapper.insert(withdrawalDO);
        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_WITHDRAWAL_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(withdrawalDO.getId().toString()) // 业务的订单编号
                .setSubject("订单提现单号"+withdrawalDO.getId()).setBody("用户ID"+loginUser.getId()+"，提现金额"+withdrawalDO.getWithdrawalInfo()+"分").setPrice(withdrawalDO.getPrice()) // 价格信息
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
//        if (invOrderDO == null) {
//            throw exception(DEMO_ORDER_NOT_FOUND);
//        }
        if (Objects.isNull(withdrawalDO.getUserId()) || withdrawalDO.getUserId()<=0) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_USER_EXCEPT);
        }
        if (Objects.isNull(withdrawalDO.getUserType()) || withdrawalDO.getUserType()<=0) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_USER_EXCEPT);
        }
        // 校验订单是否支付
        if (withdrawalDO.getPrice()<0) {
            throw exception(ErrorCodeConstants.WITHDRAWAL_AMOUNT_EXCEPT);
        }
        return withdrawalDO;
    }
    @Override
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
            log.error("[validateDemoOrderCanPaid][order({}) 不处于待支付状态，请进行处理！order 数据是：{}]",
                    id, toJsonString(withdrawalDO));
            throw exception(DEMO_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
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
        if (notEqual(payOrder.getPrice(), withdrawalDO.getPrice())) {
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
    public CreateOrderResult createInvOrder(LoginUser loginUser, PaySteamOrderCreateReqVO createReqVO) {
        CreateOrderResult createOrderResult=new CreateOrderResult();
        SellingDO sellingDO = sellingMapper.selectById(createReqVO.getSellId());
        InvDescDO invDescDO = invDescMapper.selectById(sellingDO.getInvDescId());
        // 1.1 获得商品
        InvOrderDO invOrderDO = new InvOrderDO().setSellId(createReqVO.getSellId()).setSteamId(createReqVO.getSteamId())
                .setPrice(0).setSteamId(createReqVO.getSteamId())
                .setPayOrderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .setTransferText(new TransferMsg()).setInvDescId(sellingDO.getInvDescId()).setInvId(sellingDO.getInvId())
                //专家信息
                .setSellCashStatus(InvSellCashStatusEnum.INIT.getStatus()).setSellUserId(sellingDO.getUserId()).setSellUserType(sellingDO.getUserType())
                .setPayStatus(false).setRefundPrice(0).setUserId(loginUser.getId()).setUserType(loginUser.getUserType());
        validateInvOrderCanCreate(invOrderDO);
        invOrderMapper.insert(invOrderDO);

        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(invOrderDO.getId().toString()) // 业务的订单编号
                .setSubject("购买"+invDescDO.getMarketName()).setBody("出售编号："+sellingDO.getId()).setPrice(invOrderDO.getPrice()) // 价格信息
                .setExpireTime(addTime(Duration.ofHours(2L)))); // 支付的过期时间
        // 2.2 更新支付单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(invOrderDO.getId())
                .setPayOrderId(payOrderId));
        //更新库存的标识
        sellingDO.setTransferStatus(InvTransferStatusEnum.INORDER.getStatus());
        sellingMapper.updateById(sellingDO);
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
        if(invDescDO.getTradable().intValue()!=1){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }

        //库存状态为没有订单
        if(!InvTransferStatusEnum.SELL.getStatus().equals(sellingDO.getTransferStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
        //使用库存的价格进行替换
        invOrderDO.setPrice(sellingDO.getPrice());


        //检查是否已经下过单
        List<InvOrderDO> invOrderDOS = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getSellId, sellingDO.getId())
//                .eq(InvOrderDO::getPayStatus, 1)
                .isNull(InvOrderDO::getPayRefundId)
        );
        if(invOrderDOS.size()>0){
            throw exception(ErrorCodeConstants.INVORDER_ORDERED_EXCEPT);
        }
        // 校验订单是否支付
        if (Objects.isNull(invOrderDO.getPrice()) || invOrderDO.getPrice()<=0) {
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

        return new PageResult<InvOrderResp>(ret,invOrderDOPageResult.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInvOrderPaid(Long id, Long payOrderId) {
        // 校验并获得支付订单（可支付）
        PayOrderRespDTO payOrder = validateInvOrderCanPaid(id, payOrderId);

        // 更新 PayDemoOrderDO 状态为已支付
        int updateCount = invOrderMapper.updateByIdAndPayed(id, false,
                new InvOrderDO().setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayOrderStatus(payOrder.getStatus())
                        .setPayChannelCode(payOrder.getChannelCode()));
        if (updateCount == 0) {
            throw exception(DEMO_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        //获取专家钱包并进行打款
        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(invOrderDO.getSellUserId(), invOrderDO.getSellUserType());
        payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(invOrderDO.getId()),
                PayWalletBizTypeEnum.STEAM_CASH, invOrderDO.getPrice());
        invOrderDO.setSellCashStatus(InvSellCashStatusEnum.CASHED.getStatus());
        invOrderMapper.updateById(invOrderDO);
        try{
            steamService.tradeAsset(invOrderDO);
        }catch (Exception e){
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
        if (notEqual(payOrder.getPrice(), invOrderDO.getPrice())) {
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
                .setReason("想退钱").setPrice(invOrderDO.getPrice()));// 价格信息
        // 2.3 更新退款单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(id)
                .setPayRefundId(payRefundId).setRefundPrice(invOrderDO.getPrice()));
        //释放库存
        SellingDO sellingDO = sellingMapper.selectById(invOrderDO.getSellId());
        sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
        sellingMapper.updateById(sellingDO);
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

    private PayRefundRespDTO validateInvOrderCanRefunded(Long id, Long payRefundId) {
        // 1.1 校验示例订单
        InvOrderDO invOrderDO = invOrderMapper.selectById(id);
        if (invOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        // 1.2 校验退款订单匹配
        if (Objects.equals(invOrderDO.getPayRefundId(), payRefundId)) {
            log.error("[validateDemoOrderCanRefunded][order({}) 退款单不匹配({})，请进行处理！order 数据是：{}]",
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
        if (notEqual(payRefund.getRefundPrice(), invOrderDO.getPrice())) {
            log.error("[validateDemoOrderCanRefunded][order({}) payRefund({}) 退款金额不匹配，请进行处理！order 数据是：{}，payRefund 数据是：{}]",
                    id, payRefundId, toJsonString(invOrderDO), toJsonString(payRefund));
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

}
