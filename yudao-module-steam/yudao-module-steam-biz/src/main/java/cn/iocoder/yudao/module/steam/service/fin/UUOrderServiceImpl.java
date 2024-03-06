package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.pay.api.refund.PayRefundApi;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundRespDTO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.refund.PayRefundStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateReqVo;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.PayWithdrawalOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;

import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.withdrawal.WithdrawalMapper;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.YouyouCommodityMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.CreateOrderResult;
import cn.iocoder.yudao.module.steam.service.steam.InvSellCashStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.YouPingOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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



    @Resource
    private InvOrderMapper invOrderMapper;
    @Resource
    private WithdrawalMapper withdrawalMapper;


    @Resource
    private OpenApiService openApiService;



    private PayWalletService payWalletService;
    @Autowired
    public void setPayWalletService(PayWalletService payWalletService) {
        this.payWalletService = payWalletService;
    }

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Resource
    private YouyouCommodityMapper youyouCommodityMapper;
    @Resource
    private SteamService steamService;


    @Resource
    private BindUserMapper bindUserMapper;

    public UUOrderServiceImpl() {
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
    public YouyouOrderDO createInvOrder(LoginUser loginUser, CreateReqVo createReqVO) {

        BigDecimal bigDecimal = new BigDecimal(createReqVO.getPurchasePrice());
        YouyouOrderDO youyouOrderDO=new YouyouOrderDO()
                .setOrderNo(noRedisDAO.generate(PAY_NO_PREFIX))
                .setUserId(loginUser.getId()).setUserType(loginUser.getUserType())
                .setPayOrderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .setMerchantOrderNo(createReqVO.getMerchantOrderNo())
                .setTradeLinks(createReqVO.getTradeLinks())
                .setCommodityId(createReqVO.getCommodityId()).setCommodityHashName(createReqVO.getCommodityHashName()).setCommodityTemplateId(createReqVO.getCommodityTemplateId())
                .setPurchasePrice(createReqVO.getPurchasePrice())
                .setPayAmount(bigDecimal.multiply(new BigDecimal("100")).intValue())
                .setSellCashStatus(InvSellCashStatusEnum.INIT.getStatus()).setSellUserId(UU_CASH_ACCOUNT_ID).setSellUserType(UserTypeEnum.MEMBER.getValue())
                .setPayStatus(false).setRefundPrice(0);
        validateInvOrderCanCreate(youyouOrderDO);
        youyouOrderMapper.insert(youyouOrderDO);
//        // 2.1 创建支付单
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(youyouOrderDO.getId().toString()) // 业务的订单编号
                .setSubject("购买").setBody("出售编号："+createReqVO.getCommodityId()).setPrice(youyouOrderDO.getPayAmount()) // 价格信息
                .setExpireTime(addTime(Duration.ofHours(2L)))); // 支付的过期时间
//        // 2.2 更新支付单到 demo 订单
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(youyouOrderDO.getId())
                .setPayOrderId(payOrderId));
        YouyouCommodityDO youyouCommodityDO = youyouCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());
//        //更新库存的标识
        youyouCommodityDO.setTransferStatus(InvTransferStatusEnum.INORDER.getStatus());
        youyouCommodityMapper.updateById(youyouCommodityDO);
        return youyouOrderDO;
    }
    private YouyouOrderDO validateInvOrderCanCreate(YouyouOrderDO youyouOrderDO) {
        //检测交易链接
        try{
            steamService.checkTradeUrlFormat(youyouOrderDO.getTradeLinks());
        }catch (ServiceException e){
            throw exception(OpenApiCode.ERR_5408);
        }
        //检测交易链接是否是自己
        Long aLong = bindUserMapper.selectCount(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, youyouOrderDO.getUserId())
                .eqIfPresent(BindUserDO::getUserType, youyouOrderDO.getUserType())
                .eqIfPresent(BindUserDO::getTradeUrl, youyouOrderDO.getTradeLinks())
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
        YouyouCommodityDO youyouCommodityDO = youyouCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());


//        SellingDO sellingDO = sellingMapper.selectById(invOrderDO.getSellId());
        //校验商品是否存在
        if (Objects.isNull(youyouCommodityDO)) {
            throw exception(ErrorCodeConstants.UU_GOODS_NOT_FOUND);
        }
//        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//        if(CommonStatusEnum.isDisable(sellingDO.getStatus())){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//        if(Objects.isNull(sellingDO.getBindUserId())){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//        if(invOrderDO.getUserId().equals(sellingDO.getUserId()) && invOrderDO.getUserType().equals(sellingDO.getUserType())){
//            throw exception(ErrorCodeConstants.INVORDER_ORDERUSER_EXCEPT);
//        }
//        //检查是否可交易
//        Optional<InvDescDO> first1 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
//                .eq(InvDescDO::getClassid, sellingDO.getClassid())
//                .eq(InvDescDO::getInstanceid, sellingDO.getInstanceid())
//                .eq(InvDescDO::getAppid, sellingDO.getAppid())
//        ).stream().findFirst();
//        if(!first1.isPresent()){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//        InvDescDO invDescDO = first1.get();
//        if(invDescDO.getTradable().intValue()!=1){
//            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
//        }
//
//        //库存状态为没有订单
        if(!InvTransferStatusEnum.SELL.getStatus().equals(youyouCommodityDO.getTransferStatus())){
            throw exception(ErrorCodeConstants.INVORDER_INV_NOT_FOUND);
        }
//        //使用库存的价格进行替换
        BigDecimal bigDecimal = new BigDecimal(youyouCommodityDO.getCommodityPrice());
        youyouOrderDO.setPayAmount(bigDecimal.multiply(new BigDecimal("100")).intValue());
        //判断用户钱包是否有足够的钱
        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(youyouOrderDO.getUserId(), youyouOrderDO.getUserType());
        if(Objects.isNull(orCreateWallet) || orCreateWallet.getBalance()<youyouOrderDO.getPayAmount()){
            throw exception(OpenApiCode.ERR_5212);
        }
//
//        //检查是否已经下过单
        List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                        .eq(YouyouOrderDO::getRealCommodityId, youyouOrderDO.getRealCommodityId())
//                .eq(InvOrderDO::getPayStatus, 1)
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
    public YouyouOrderDO getInvOrder(Long id) {
        return youyouOrderMapper.selectById(id);
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
    @Transactional(rollbackFor = Exception.class)
    public void updateInvOrderPaid(Long id, Long payOrderId) {
        // 校验并获得支付订单（可支付）
        PayOrderRespDTO payOrder = validateInvOrderCanPaid(id, payOrderId);

        // 更新 状态为已支付
        int updateCount = youyouOrderMapper.updateByIdAndPayed(id, false,
                new YouyouOrderDO().setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayOrderStatus(payOrder.getStatus())
                        .setPayChannelCode(payOrder.getChannelCode()));
        if (updateCount == 0) {
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(id);
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
    }

    /**
     * 上传订单到有品
     * @param youyouOrderDO
     * @return
     */
    private YouPingOrder uploadYY(YouyouOrderDO youyouOrderDO){
        OpenApiReqVo<CreateReqVo> openApiReqVo=new OpenApiReqVo<>();
        CreateReqVo createReqVo = new CreateReqVo();
        createReqVo.setMerchantOrderNo("YY"+youyouOrderDO.getMerchantOrderNo());
        createReqVo.setTradeLinks(youyouOrderDO.getTradeLinks());
        createReqVo.setCommodityId(youyouOrderDO.getCommodityId());
        createReqVo.setCommodityHashName(youyouOrderDO.getCommodityHashName());
        createReqVo.setPurchasePrice(youyouOrderDO.getPurchasePrice());
        createReqVo.setFastShipping(youyouOrderDO.getFastShipping());
        createReqVo.setCommodityId(youyouOrderDO.getCommodityId());
        openApiReqVo.setData(createReqVo);
        YouPingOrder youPingOrder = openApiService.requestUU("https://gw-openapi.youpin898.com/open/v1/api/byGoodsIdCreateOrder", openApiReqVo, YouPingOrder.class);
        return youPingOrder;
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
    public void refundInvOrder(LoginUser loginUser,Long id, String userIp) {
        // 1. 校验订单是否可以退款
        YouyouOrderDO youyouOrderDO = validateInvOrderCanRefund(id,loginUser);

        // 2.1 生成退款单号
        // 一般来说，用户发起退款的时候，都会单独插入一个售后维权表，然后使用该表的 id 作为 refundId
        // 这里我们是个简单的 demo，所以没有售后维权表，直接使用订单 id + "-refund" 来演示
        String refundId = youyouOrderDO.getId() + "-refund";
        // 2.2 创建退款单
        Long payRefundId = payRefundApi.createRefund(new PayRefundCreateReqDTO()
                .setAppId(PAY_APP_ID).setUserIp(getClientIP()) // 支付应用
                .setMerchantOrderId(String.valueOf(youyouOrderDO.getId())) // 支付单号
                .setMerchantRefundId(refundId)
                .setReason("用户不想要了主动退单").setPrice(youyouOrderDO.getPayAmount()));// 价格信息
        // 2.3 更新退款单到 demo 订单
        invOrderMapper.updateById(new InvOrderDO().setId(id)
                .setPayRefundId(payRefundId).setRefundPrice(youyouOrderDO.getPayAmount()));
        //释放库存
        YouyouCommodityDO youyouCommodityDO = youyouCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());
        youyouCommodityDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
        youyouCommodityMapper.updateById(youyouCommodityDO);
    }

    private YouyouOrderDO validateInvOrderCanRefund(Long id,LoginUser loginUser) {
        // 校验订单是否存在
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(id);
        if (youyouOrderDO == null) {
            throw exception(ErrorCodeConstants.INVORDER_ORDER_NOT_FOUND);
        }
        if(youyouOrderDO.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_CASHED_CANNOTREFUND);
        }
        if(!youyouOrderDO.getUserId().equals(loginUser.getId())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_REFUND_USER_ERROR);
        }
        if(!youyouOrderDO.getUserType().equals(loginUser.getUserType())){
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
        //检查是否已发货
        if(InvSellCashStatusEnum.CASHED.getStatus().equals(youyouOrderDO.getSellCashStatus())){
            throw exception(ErrorCodeConstants.INVORDER_ORDER_TRANSFER_ALERY);
        }
        return youyouOrderDO;
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

}
