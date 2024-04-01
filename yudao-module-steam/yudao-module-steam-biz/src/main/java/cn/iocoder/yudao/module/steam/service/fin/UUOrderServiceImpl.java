package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplatePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUBuyGoodsByIdReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodeityService;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyounotify.YouyouNotifyDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.devaccount.DevAccountMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyounotify.YouyouNotifyMapper;
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
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

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
    private static final Long UU_CASH_ACCOUNT_ID = 250L;//UU收款账号ID
    /**
     * 服务费收费帐号
     *
     * 从 [支付管理 -> 应用信息] 里添加
     */
    private static final Long UU_CASH_SERVICE_ID = 251L;//UU收款账号ID
    /**
     * 支付单流水的 no 前缀
     */
    private static final String PAY_NO_PREFIX = "YY";

    private ApiUUCommodeityService apiUUCommodeityService;

    @Autowired
    public void setApiUUCommodeityService(ApiUUCommodeityService apiUUCommodeityService) {
        this.apiUUCommodeityService = apiUUCommodeityService;
    }

    @Resource
    private PayNoRedisDAO noRedisDAO;



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
    private YouyouNotifyMapper youyouNotifyMapper;

    @Resource
    private UUCommodityMapper uUCommodityMapper;

    @Resource
    private UUTemplateService uuTemplateService;

    private YouyouCommodityService youyouCommodityService;
    @Autowired
    public void setYouyouCommodityService(YouyouCommodityService youyouCommodityService) {
        this.youyouCommodityService = youyouCommodityService;
    }
    @Resource
    private DevAccountMapper devAccountMapper;



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
                //卖家信息
                .setSellUserId(UU_CASH_ACCOUNT_ID).setSellUserType(UserTypeEnum.MEMBER.getValue())
                .setSellCashStatus(InvSellCashStatusEnum.INIT.getStatus())
                //服务费账号
                .setServiceFeeUserId(UU_CASH_SERVICE_ID).setServiceFeeUserType(UserTypeEnum.MEMBER.getValue())
                //订单信息
                .setOrderNo(noRedisDAO.generate(PAY_NO_PREFIX)).setMerchantOrderNo(reqVo.getMerchantOrderNo())

                //设置支付信息
                .setPayStatus(false)
                .setPayOrderStatus(PayOrderStatusEnum.WAITING.getStatus())
                //设置退款
//                .setRefundPrice(0)
                //设置商品信息
                .setCommodityId(reqVo.getCommodityId()).setCommodityHashName(reqVo.getCommodityHashName()).setCommodityTemplateId(reqVo.getCommodityTemplateId())
                .setPurchasePrice(reqVo.getPurchasePrice())
                .setPayAmount(bigDecimal.multiply(new BigDecimal("100")).intValue());
        validateInvOrderCanCreate(youyouOrderDO);
        youyouOrderMapper.insert(youyouOrderDO);
        YouyouCommodityDO youyouCommodityDO = uUCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());
        uUCommodityMapper.updateById(new YouyouCommodityDO().setId(youyouCommodityDO.getId()).setTransferStatus(InvTransferStatusEnum.INORDER.getStatus()));
        return youyouOrderDO;
    }

    /**
     * 未支付订单取消支付并释放库存
     * @param invOrderId 订单号
     */
    public void closeUnPayInvOrder(Long invOrderId) {
        YouyouOrderDO uuOrder = getUUOrderById(invOrderId);
        if(uuOrder.getPayStatus()){
            throw new ServiceException(-1,"订单已支付不支持关闭");
        }

        youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setPayStatus(false).setTransferStatus(InvTransferStatusEnum.CLOSE.getStatus())
                    .setPayOrderStatus(PayOrderStatusEnum.CLOSED.getStatus()));
            //释放库存
        YouyouCommodityDO youyouCommodityDO = uUCommodityMapper.selectById(uuOrder.getRealCommodityId());
        uUCommodityMapper.updateById(new YouyouCommodityDO().setId(youyouCommodityDO.getId()).setTransferStatus(InvTransferStatusEnum.SELL.getStatus()));
    }
    /**
     * 释放库存
     * 已经支付的订单退还库存
     * 请不要直接调用
     */
    @Override
    public void releaseInvOrder(Long invOrderId) {
        YouyouOrderDO uuOrder = getUUOrderById(invOrderId);
        if(uuOrder.getSellCashStatus().equals(InvSellCashStatusEnum.CASHED.getStatus())){
            throw new ServiceException(-1,"订单已经支付给卖家，不支持操作");
        }
        if(uuOrder.getSellCashStatus().equals(InvSellCashStatusEnum.DAMAGES.getStatus())){
            throw new ServiceException(-1,"订单已经支付违约金，不支持操作");
        }
        if(!uuOrder.getPayStatus()){
            throw new ServiceException(-1,"不支持未支付的订单关闭");
        }
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setTransferStatus(InvTransferStatusEnum.CLOSE.getStatus()));
        //释放库存
        YouyouCommodityDO youyouCommodityDO = uUCommodityMapper.selectById(uuOrder.getRealCommodityId());
        uUCommodityMapper.updateById(new YouyouCommodityDO().setId(youyouCommodityDO.getId()).setTransferStatus(InvTransferStatusEnum.SELL.getStatus()));
    }


    /**
     * 订单失败后退款
     * 扣除商品的2%后退还买家
     * @param invOrderId InvOrderId
     * @param reason 本次原因
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void damagesCloseInvOrder(Long invOrderId,String reason) {
        YouyouOrderDO uuOrderById = getUUOrderById(invOrderId);
        if(Objects.isNull(uuOrderById)){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        if(!uuOrderById.getPayStatus()){
            throw new ServiceException(-1,"订单未支付不支持退款");
        }
        releaseInvOrder(invOrderId);
        if (PayOrderStatusEnum.isSuccess(uuOrderById.getPayOrderStatus())) {
//            Integer paymentAmount = uuOrderById.getPayAmount();
//            BigDecimal divide = new BigDecimal("2").divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
//            int transferDamagesAmount = divide.multiply(new BigDecimal(paymentAmount.toString())).intValue();
//            int transferRefundAmount = paymentAmount - transferDamagesAmount;
//            //打款违约金-打款到服务费用户,取消订单后卖家应收的违约金由uu代扣，这里只需要将金额扣给平台
//            PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(uuOrderById.getServiceFeeUserId(), uuOrderById.getServiceFeeUserType());
//            PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(uuOrderById.getId()),
//                    PayWalletBizTypeEnum.INV_DAMAGES, transferDamagesAmount);
//            //获取买家钱包并进行退款
//            PayWalletDO orCreateWallet2 = payWalletService.getOrCreateWallet(uuOrderById.getBuyUserId(), uuOrderById.getBuyUserType());
//            PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet2.getId(), String.valueOf(uuOrderById.getId()),
//                    PayWalletBizTypeEnum.STEAM_REFUND, transferRefundAmount);

            //不收违约金
            PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(uuOrderById.getBuyUserId(), uuOrderById.getBuyUserType());
            PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(uuOrderById.getId()),
                    PayWalletBizTypeEnum.INV_SERVICE_FEE_REFUND, uuOrderById.getServiceFee());
            //获取买家钱包并进行退款
//            PayWalletDO orCreateWallet2 = payWalletService.getOrCreateWallet(uuOrderById.getBuyUserId(), uuOrderById.getBuyUserType());
            PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(uuOrderById.getId()),
                    PayWalletBizTypeEnum.STEAM_CASH_REFUND, uuOrderById.getCommodityAmount());

            List<PayWalletTransactionDO> payWalletTransactionDOS = Arrays.asList(payWalletTransactionDO, payWalletTransactionDO1);
            youyouOrderMapper.updateById(new YouyouOrderDO().setId(uuOrderById.getId())
//                    .setTransferDamagesAmount(transferDamagesAmount)
//                    .setTransferRefundAmount(transferRefundAmount)
                    .setTransferRefundAmount(uuOrderById.getPayAmount())//不收手续费
                    .setTransferDamagesTime(LocalDateTime.now())
                    .setTransferDamagesRet(JacksonUtils.writeValueAsString(payWalletTransactionDOS))
                    .setSellCashStatus(InvSellCashStatusEnum.DAMAGES.getStatus())
                    .setCancelReason(reason)
            );
        }
    }

    /**
     * 订单完成后打款
     * @param invOrderId 订单号
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void cashInvOrder(Long invOrderId) {
        YouyouOrderDO uuOrderById = getUUOrderById(invOrderId);
        if(Objects.isNull(uuOrderById)){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        if(!uuOrderById.getPayStatus()){
            throw new ServiceException(-1,"订单未支付不支持打款");
        }
        if (PayOrderStatusEnum.isSuccess(uuOrderById.getPayOrderStatus())) {
            //打款服务费
            PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(uuOrderById.getServiceFeeUserId(), uuOrderById.getServiceFeeUserType());
            PayWalletTransactionDO payWalletTransactionDO = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(uuOrderById.getId()),
                    PayWalletBizTypeEnum.INV_SERVICE_FEE, uuOrderById.getServiceFee());

            youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setServiceFeeRet(JacksonUtils.writeValueAsString(payWalletTransactionDO)));
            //获取卖家家钱包并进行打款
            PayWalletDO orCreateWallet2 = payWalletService.getOrCreateWallet(uuOrderById.getSellUserId(), uuOrderById.getSellUserType());
            PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet2.getId(), String.valueOf(uuOrderById.getId()),
                    PayWalletBizTypeEnum.STEAM_CASH, uuOrderById.getCommodityAmount());
            youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setSellCashRet(JacksonUtils.writeValueAsString(payWalletTransactionDO1)).setSellCashStatus(InvSellCashStatusEnum.CASHED.getStatus()));
        }
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
        //扣除服务费到平台
        PayWalletTransactionDO payWalletTransactionDO = payWalletService.reduceWalletBalance(orCreateWallet.getId(), uuOrder.getId(),
                PayWalletBizTypeEnum.SUB_INV_SERVICE_FEE, uuOrder.getServiceFee());
        //获取卖家家钱包并进行打款
        PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.reduceWalletBalance(orCreateWallet.getId(),uuOrder.getId(),
                PayWalletBizTypeEnum.SUB_STEAM_CASH, uuOrder.getCommodityAmount());
        List<PayWalletTransactionDO> payWalletTransactionDOS = Arrays.asList(payWalletTransactionDO, payWalletTransactionDO1);
        youyouOrderMapper.updateById(new YouyouOrderDO().setId(uuOrder.getId()).setPayPayRet(JacksonUtils.writeValueAsString(payWalletTransactionDOS)).setPayStatus(true)
                .setPayOrderStatus(PayOrderStatusEnum.SUCCESS.getStatus()));
        YouyouOrderDO uuOrder1 = getUUOrder(loginUser, queryOrderReqVo);
        YouPingOrder youPingOrder = uploadYY(uuOrder1);

        youyouOrderMapper.updateById(new YouyouOrderDO().setId(uuOrder1.getId()).
                setUuOrderNo(youPingOrder.getOrderNo())
                .setUuMerchantOrderNo(youPingOrder.getMerchantOrderNo())
                .setUuShippingMode(youPingOrder.getShippingMode())
                .setUuOrderStatus(youPingOrder.getOrderStatus())
                .setUuMerchantOrderNo(youPingOrder.getMerchantOrderNo())
                .setPayOrderStatus(PayOrderStatusEnum.SUCCESS.getStatus())
                .setPayStatus(true)
                .setPayTime(LocalDateTime.now())
                .setTransferStatus(InvTransferStatusEnum.TransferING.getStatus())
                .setTransferText(JacksonUtils.writeValueAsString(youPingOrder))
        );
        return getUUOrder(loginUser, queryOrderReqVo);
    }

    private void validateInvOrderCanCreate(YouyouOrderDO youyouOrderDO) {
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
//        Long aLong = bindUserMapper.selectCount(new LambdaQueryWrapperX<BindUserDO>()
//                .eq(BindUserDO::getUserId, youyouOrderDO.getBuyUserId())
//                .eqIfPresent(BindUserDO::getUserType, youyouOrderDO.getBuyUserType())
//                .eqIfPresent(BindUserDO::getTradeUrl, youyouOrderDO.getBuyTradeLinks())
//        );
//        if(aLong>0){
//            throw exception(OpenApiCode.ERR_5407);
//        }
        if(Objects.isNull(youyouOrderDO.getCommodityHashName()) && Objects.isNull(youyouOrderDO.getCommodityTemplateId()) && Objects.isNull(youyouOrderDO.getCommodityId())){
            throw exception(ErrorCodeConstants.UU_GOODS_ERR);
        }
        youyouOrderDO.setRealCommodityId(youyouOrderDO.getCommodityId());
        if(Objects.isNull(youyouOrderDO.getCommodityId())){//指定ID购买
            Integer onSealGoodsId = apiUUCommodeityService.getOnSealGoodsId(new ApiUUBuyGoodsByIdReqVO()
                    .setTemplateId(youyouOrderDO.getCommodityTemplateId())
                    .setTemplateHashName(youyouOrderDO.getCommodityHashName())
                    .setShippingMode(String.valueOf(youyouOrderDO.getShippingMode()))
                    .setMaxPrice(youyouOrderDO.getPurchasePrice())
            );
            if(Objects.isNull(onSealGoodsId)){
                throw exception(OpenApiCode.ERR_5213);
            }
            youyouOrderDO.setRealCommodityId(String.valueOf(onSealGoodsId));
            //查询不到则返回
        }
        if(Objects.isNull(youyouOrderDO.getRealCommodityId())){
            throw exception(OpenApiCode.ERR_5214);
        }
        YouyouCommodityDO youyouCommodityDO = uUCommodityMapper.selectById(youyouOrderDO.getRealCommodityId());


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
        BigDecimal commodityAmount = new BigDecimal(youyouCommodityDO.getCommodityPrice()).multiply(new BigDecimal("100"));
        youyouOrderDO.setCommodityAmount(commodityAmount.intValue());
        BigDecimal serviceFee = commodityAmount.multiply(new BigDecimal("5")).divide(new BigDecimal("100"));
        if(serviceFee.intValue()<50000){
            youyouOrderDO.setPayAmount(commodityAmount.add(serviceFee).intValue());
            youyouOrderDO.setServiceFee(serviceFee.intValue());
            youyouOrderDO.setServiceFeeRate("5");
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
        List<YouyouOrderDO> expOrder = getExpOrder(youyouOrderDO.getRealCommodityId());
        if(expOrder.size()>0){
            throw exception(OpenApiCode.ERR_5301);
        }
        // 校验订单是否支付
        if (Objects.isNull(youyouOrderDO.getPayAmount()) || youyouOrderDO.getPayAmount()<=0) {
            throw exception(ErrorCodeConstants.UU_GOODS_AMOUNT_EXCEPT);
        }
        // 校验订单是否支付
        if (youyouOrderDO.getPayStatus()) {
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
    }

    public YouyouOrderDO getUUOrderById(Long orderId) {
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectById(orderId);
        if(Objects.isNull(youyouOrderDO)){
            throw exception(OpenApiCode.JACKSON_EXCEPTION);
        }else{
            return youyouOrderDO;
        }
    }
    /**
     * 检查是否有有效订单有魔兽的时候不能再次下单
     * 条件支付订单未关闭且发货状态不是关闭都是有效订单
     *
     * @param commodityId 商品ID
     * @return
     */
    public List<YouyouOrderDO> getExpOrder(String commodityId){
        return youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                .eq(YouyouOrderDO::getCommodityId, commodityId)
                .neIfPresent(YouyouOrderDO::getPayOrderStatus,PayOrderStatusEnum.CLOSED.getStatus())
                .ne(YouyouOrderDO::getTransferStatus, InvTransferStatusEnum.CLOSE.getStatus())
        );
    }
    @Override
    public YouyouOrderDO getUUOrder(LoginUser loginUser, QueryOrderReqVo queryOrderReqVo) {
        LambdaQueryWrapperX<YouyouOrderDO> uuOrderDOLambdaQueryWrapperX = new LambdaQueryWrapperX<YouyouOrderDO>()
                .eqIfPresent(YouyouOrderDO::getBuyUserId, 313)
                .eqIfPresent(YouyouOrderDO::getBuyUserType, loginUser.getUserType());
        if (Objects.isNull(queryOrderReqVo.getOrderNo()) && Objects.isNull(queryOrderReqVo.getMerchantNo()) && Objects.isNull(queryOrderReqVo.getId())){
            throw exception(OpenApiCode.JACKSON_EXCEPTION);
        }
        uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getOrderNo, queryOrderReqVo.getOrderNo());
        uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getMerchantOrderNo, queryOrderReqVo.getMerchantNo());
        uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getId, queryOrderReqVo.getId());
        if(Objects.nonNull(queryOrderReqVo.getId())){
            uuOrderDOLambdaQueryWrapperX.eqIfPresent(YouyouOrderDO::getId, queryOrderReqVo.getId());
        }
        YouyouOrderDO youyouOrderDO = youyouOrderMapper.selectOne(uuOrderDOLambdaQueryWrapperX);
        if(Objects.isNull(youyouOrderDO)){
            throw exception(OpenApiCode.JACKSON_EXCEPTION);
        }else{
            return youyouOrderDO;
        }
    }

    @Override
    public OrderInfoResp orderInfo(YouyouOrderDO uuOrder, OpenApiReqVo<QueryOrderReqVo> openApiReqVo)  {
        ObjectMapper objectMapper = new ObjectMapper();
        if (uuOrder != null && uuOrder.getOrderInformReturnJason() == null){
            openApiReqVo.getData().setOrderNo(uuOrder.getUuOrderNo());
            ApiResult<OrderInfoResp> orderInfoRespApiResult = uuService.orderInfo(openApiReqVo.getData());
            String json = null;
            try {
                json = objectMapper.writeValueAsString(orderInfoRespApiResult.getData());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 转换出错: " + e.getMessage(), e);
            }
            if (json != null){
                YouyouOrderDO youyouOrderDO = new YouyouOrderDO();
                youyouOrderDO.setId(uuOrder.getId());
                youyouOrderDO.setOrderInformReturnJason(json);
                youyouOrderMapper.updateById(youyouOrderDO);
            }
        }
        YouyouCommodityDO youyouCommodity = null;
        if (uuOrder != null) {
            youyouCommodity = youyouCommodityService.getYouyouCommodity(Integer.valueOf(uuOrder.getRealCommodityId()));
        }
        if (youyouCommodity == null){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
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
        ret.setId(uuOrder.getOrderNo());
        ret.setOrderId(uuOrder.getId());
        ret.setOrderNo(uuOrder.getOrderNo());

        OrderInfoResp orderInfoResp = null;
        try {
            orderInfoResp = objectMapper.readValue(uuOrder.getOrderInformReturnJason(), OrderInfoResp.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 转换出错: " + e.getMessage(), e);
        }
        ret.setProcessStatus(orderInfoResp.getProcessStatus());
        ret.setOrderSubStatus(orderInfoResp.getOrderSubStatus());
        ret.setOrderSubStatusName(orderInfoResp.getOrderSubStatusName());
        ret.setOrderType(orderInfoResp.getOrderType());
        ret.setOrderSubType(orderInfoResp.getOrderSubType());
        ret.setTimeType(orderInfoResp.getTimeType());
//        ret.setTime(null);// TODO 待确认
        ret.setReturnAmount(orderInfoResp.getReturnAmount());
        ret.setServiceFee(uuOrder.getServiceFee().toString());
//        ret.setServiceFeeRate(uuOrder.getServiceFeeRate());
        ret.setCommodityAmount(youyouCommodity.getCommodityPrice());
        if(Objects.nonNull(uuOrder.getPayAmount())){
            ret.setPaymentAmount(new BigDecimal(uuOrder.getPayAmount()).divide(new BigDecimal("100")).toString());
        }
        ret.setSellerSteamRegTime(orderInfoResp.getSellerSteamRegTime());
//        ret.setTradeOfferId(null);// TODO 待确认
        ret.setCancelOrderTime(orderInfoResp.getCancelOrderTime());
//        ret.setOfferSendResult(null);// TODO 待确认
        ret.setTradeUrl(uuOrder.getBuyTradeLinks());
        ret.setFinishOrderTime(orderInfoResp.getFinishOrderTime());
        ret.setPaySuccessTime(orderInfoResp.getPaySuccessTime());
        ret.setPayEndTime(orderInfoResp.getPayEndTime());
//        ret.setSendOfferSuccessTime(null);
        ret.setSendOfferEndTime(orderInfoResp.getSendOfferEndTime());
        ret.setOrderStatusColor(orderInfoResp.getOrderStatusColor());
        ret.setOrderSubStatus(orderInfoResp.getOrderSubStatus());

//        ret.setShippingMode(uuOrder.getUuShippingMode());
        ret.setOrderStatus(uuOrder.getUuOrderStatus());
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
        ret.setCreateOrderTime(uuOrder.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        if(Objects.nonNull(uuOrder.getPayTime())){
            ret.setPaySuccessTime(uuOrder.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        if(Objects.nonNull(payOrder)){
            ret.setPayEndTime(payOrder.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }

//        ret.setConfirmOfferEndTime(null);
//        ret.setPendingEndTime(null);
//        ret.setDelayedTransferEndTime(null);
//        ret.setPrice(new BigDecimal(youyouCommodity.getCommodityPrice()).multiply(new BigDecimal("100")).longValue());//TODO 待确认
        ret.setTotalAmount(String.valueOf(uuOrder.getPayAmount()));
        ret.setCancelReason(uuOrder.getCancelReason());
        if(Objects.nonNull(ret.getOrderStatus())){
            ret.setOrderStatusName(UUOrderStatus.valueOf(ret.getOrderStatus()).getMsg());
        }
        if (Objects.nonNull(ret.getOrderStatusName())){
            String updatedOrderStatusName = ret.getOrderStatusName().replace("-s", "time");
            ret.setOrderStatusDesc(updatedOrderStatusName);
        }

        OrderInfoResp.ProductDetailDTO productDetailDTO = new OrderInfoResp.ProductDetailDTO();
        productDetailDTO.setCommodityId(youyouCommodity.getId());
        productDetailDTO.setCommodityName(youyouCommodity.getCommodityName());
        productDetailDTO.setCommodityHashName(youyouTemplateDO.getHashName());

        productDetailDTO.setCommodityTemplateId(youyouCommodity.getTemplateId());
        productDetailDTO.setAssertId(null);// TODO 待确认
        productDetailDTO.setAbrade(youyouCommodity.getCommodityAbrade());

        productDetailDTO.setIsDoppler(youyouCommodity.getTemplateisDoppler());
        productDetailDTO.setDopplerColor(null);// TODO 待确认
        productDetailDTO.setIsFade(youyouCommodity.getTemplateisFade());
//        productDetailDTO.setFadeName(youyouCommodity.getCommodityFade());
        productDetailDTO.setDopplerColor(youyouCommodity.getCommodityDoppler());

        //TODO 以下属性先这样返回， 目前用不上
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

        ret.setProductDetail(productDetailDTO);
        return ret;
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
    @Override
    public Integer orderCancel(LoginUser loginUser, OrderCancelVo orderCancelVo, String userIp,String cancelReason) {
        YouyouOrderDO uuOrder = getUUOrder(loginUser, new QueryOrderReqVo().setOrderNo(orderCancelVo.getOrderNo()));

        // 1. 校验订单是否可以退款
        YouyouOrderDO youyouOrderDO = validateInvOrderCanRefund(uuOrder,loginUser);
        //如果有uu的单子，这里则只发起uu的退款，uu退款后再发起我们的退款
        if(Objects.nonNull(youyouOrderDO.getUuOrderNo())){
            //传入uu的单子
            ApiResult<OrderCancelResp> orderCancelRespApiResult = uuService.orderCancel(new OrderCancelVo().setOrderNo(youyouOrderDO.getUuOrderNo()));
            log.info("取消UU订单结果{}{}",orderCancelRespApiResult,youyouOrderDO);
            if(orderCancelRespApiResult.getData().getResult()==1){
                refundAction(youyouOrderDO,loginUser,cancelReason);
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
    private void refundAction(YouyouOrderDO youyouOrderDO,LoginUser loginUser,String reason) {
        validateInvOrderCanRefund(youyouOrderDO,loginUser);
        damagesCloseInvOrder(youyouOrderDO.getId(),reason);
    }
    private void refundAction(YouyouOrderDO youyouOrderDO,LoginUser loginUser) {
        refundAction(youyouOrderDO,loginUser,"用户不想要了主动退款");
    }

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
        //检查是否已发货
        if(InvSellCashStatusEnum.DAMAGES.getStatus().equals(youyouOrderDO.getSellCashStatus())){
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_TRANSFER_CASHED);
        }
        //检查是否已发货
        if(InvSellCashStatusEnum.CASHED.getStatus().equals(youyouOrderDO.getSellCashStatus())){
            throw exception(ErrorCodeConstants.UU_GOODS_ORDER_TRANSFER_CASHED);
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
        return youyouOrderDO;
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
    @Override
    public void pushRemote(NotifyReq notifyReq) {
        String callBackInfo = notifyReq.getCallBackInfo();
        NotifyVo notifyVo = JacksonUtils.readValue(callBackInfo, NotifyVo.class);

        log.info("回调接收的数据{}",notifyVo);
        List<YouyouOrderDO> youyouOrderDOS = youyouOrderMapper.selectList(new LambdaQueryWrapper<YouyouOrderDO>()
                .eq(YouyouOrderDO::getUuOrderNo, notifyVo.getOrderNo()));
        if (!youyouOrderDOS.isEmpty()) {
            YouyouOrderDO youyouOrderDO = youyouOrderDOS.get(0);
            //获取用户的devaccount
            try{
                DevAccountDO devAccountDO = devAccountMapper.selectOne(new LambdaQueryWrapperX<DevAccountDO>()
                        .eq(DevAccountDO::getUserId, youyouOrderDO.getBuyUserId())
                        .eq(DevAccountDO::getUserType, youyouOrderDO.getBuyUserType())
                );
                if(StringUtils.hasText(devAccountDO.getCallbackUrl()) && StringUtils.hasText(devAccountDO.getCallbackPrivateKey()) && StringUtils.hasText(devAccountDO.getCallbackPublicKey())){
                    HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
                    builder.url(devAccountDO.getCallbackUrl());
                    builder.method(HttpUtil.Method.JSON);
                    Map<String, String> params = new HashMap<>();
                    params.put("messageNo",notifyReq.getMessageNo());
                    //注意接收到的callBackInfo是含有双引号转译符"\" 文档上无法体现只需要在验证签名是直接把callBackInfo值当成字符串即可以
                    params.put("callBackInfo",notifyReq.getCallBackInfo());

                    // 第一步：检查参数是否已经排序
                    String[] keys = params.keySet().toArray(new String[0]);
                    Arrays.sort(keys);
                    // 第二步：把所有参数名和参数值串在一起
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String key : keys) {
                        String value = params.get(key);
                        if (!StringUtils.hasText(value)) {
                            stringBuilder.append(key).append(value);
                        }
                    }
                    log.info("stringBuilder:{}",stringBuilder);
                    String s = RSAUtils.signByPrivateKey(stringBuilder.toString().getBytes(), devAccountDO.getCallbackPrivateKey());
                    notifyReq.setSign(s);
                    builder.postObject(notifyReq);
                    HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
                    YouyouNotifyDO youyouNotifyDO = youyouNotifyMapper.selectOne(new LambdaQueryWrapperX<YouyouNotifyDO>()
                            .eq(YouyouNotifyDO::getMessageNo, notifyReq.getMessageNo()));
                    youyouNotifyMapper.updateById(new YouyouNotifyDO().setId(youyouNotifyDO.getId()).setPushRemote(true)
                            .setPushRemoteUrl(devAccountDO.getCallbackUrl())
                            .setPushRemoteResult(sent.html()));
                }
            }catch (Exception e){
                log.error("消费消息出错{}",e.getMessage());
            }
        }

    }
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void checkTransfer(Long invOrderId) {
        YouyouOrderDO uuOrderById = getUUOrderById(invOrderId);
        if(Objects.isNull(uuOrderById)){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        if(!uuOrderById.getPayStatus()){
            throw new ServiceException(-1,"订单未支付不支持打款");
        }
        if(InvTransferStatusEnum.TransferING.getStatus().equals(uuOrderById.getTransferStatus())){
            //发货完成时
            ApiResult<QueryOrderStatusResp> queryOrderStatusRespApiResult = uuService.orderStatus(new QueryOrderReqVo().setOrderNo(uuOrderById.getUuOrderNo()));
            if(queryOrderStatusRespApiResult.getData().getBigStatus()==340){
                cashInvOrder(invOrderId);
                youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus()));
            }
            //只处理340的单子
//            if(queryOrderStatusRespApiResult.getData().getBigStatus()==360){
//                cashInvOrder(invOrderId);
//            }
            if(queryOrderStatusRespApiResult.getData().getBigStatus()==280){
                damagesCloseInvOrder(invOrderId,queryOrderStatusRespApiResult.getData().getSmallStatusMsg());
                youyouOrderMapper.updateById(new YouyouOrderDO().setId(invOrderId).setTransferStatus(InvTransferStatusEnum.CLOSE.getStatus()));
            }
        }
    }
}
