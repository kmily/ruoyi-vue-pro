package cn.iocoder.yudao.module.steam.service.fin.io661;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5ItemListVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5page;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import cn.iocoder.yudao.module.steam.service.steam.*;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

//@Service
@Slf4j
public class Io661ApiThreeOrderServiceImpl implements ApiThreeOrderService {
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private ApiOrderMapper apiOrderMapper;
    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;
    @Resource
    private ConfigService configService;
    @Resource
    private BindUserService bindUserService;

    private SteamService steamService;

    @Autowired
    public void setSteamService(SteamService steamService) {
        this.steamService = steamService;
    }

    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.IO661;
    }

    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        ApiCommodityRespVo apiCommodityRespVo=new ApiCommodityRespVo();
        List<SellingDO> sellingDOS = sellingMapper.selectList(new Page<>(1, 1), new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .le(SellingDO::getPrice, createReqVO.getPurchasePrice())
                .orderByAsc(SellingDO::getPrice)
        );
        if(sellingDOS.size()>0){
            SellingDO sellingDO = sellingDOS.get(0);

            apiCommodityRespVo.setIsSuccess(true);
            apiCommodityRespVo.setPlatCode(getPlatCode());
            apiCommodityRespVo.setPrice(sellingDO.getPrice());
        }else{
            apiCommodityRespVo.setIsSuccess(false);
            apiCommodityRespVo.setErrorCode(OpenApiCode.NO_DATA);
        }
        return apiCommodityRespVo;
    }

    @Override
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO, Long orderId) {
        ApiBuyItemRespVo.ApiBuyItemRespVoBuilder builder = ApiBuyItemRespVo.builder();
        Optional<SellingDO> first = sellingMapper.selectList(new Page<>(1, 1), new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .le(SellingDO::getPrice, createReqVO.getPurchasePrice())
                .orderByAsc(SellingDO::getPrice)
        ).stream().findFirst();
        if(first.isPresent()){
            return ApiBuyItemRespVo.builder().isSuccess(false).errorCode(OpenApiCode.ERR_5301).build();
        }

        SellingDO sellingDO = first.get();
        List<ApiOrderExtDO> expOrder = getExpOrder(sellingDO.getId());
        if(expOrder.size()>0){
            throw exception(ErrorCodeConstants.INVORDER_ORDERED_EXCEPT);
        }
        ApiOrderDO masterOrder = getMasterOrder(orderId);

        builder.orderNo(masterOrder.getOrderNo());
        builder.tradeLink(masterOrder.getBuyTradeLinks());
        ApiOrderExtDO apiOrderExtDO = new ApiOrderExtDO();
        apiOrderExtDO.setCreator(String.valueOf(loginUser.getId()));
        apiOrderExtDO.setPlatCode(PlatCodeEnum.IO661.getCode());
        apiOrderExtDO.setOrderNo(masterOrder.getOrderNo());
        apiOrderExtDO.setTradeOfferLinks(createReqVO.getTradeLinks());
        apiOrderExtDO.setOrderId(orderId);
        apiOrderExtDO.setOrderInfo(null);
        apiOrderExtDO.setOrderStatus(1);
        apiOrderExtDO.setOrderSubStatus("");
        apiOrderExtDO.setCommodityInfo(sellingDO.getId().toString());
        apiOrderExtDO.setOrderSubStatus(InvTransferStatusEnum.INORDER.getStatus().toString());
        apiOrderExtMapper.insert(apiOrderExtDO);
        try {
            tradeAsset(orderId, apiOrderExtDO.getId());
            ApiOrderExtDO orderExt = getOrderExt(apiOrderExtDO.getId());
            builder.isSuccess(true);
            builder.tradeOfferId(orderExt.getTradeOfferId().toString());
            return builder.build();
        }catch (Exception e){
            apiOrderExtDO.setOrderStatus(3);
            apiOrderExtDO.setOrderSubStatus("发送失败"+e.getMessage());
            builder.isSuccess(false);
            builder.errorCode(OpenApiCode.ERR_5301);
            return builder.build();
        }
    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public String queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public ApiOrderCancelRespVo releaseIvn(LoginUser loginUser, String orderNo, Long orderId) {
        return ApiThreeOrderService.super.releaseIvn(loginUser, orderNo, orderId);
    }

    @Override
    public ApiProcessNotifyResp processNotify(String jsonData, String msgNo) {
        return null;
    }

    @Override
    public V5ItemListVO getItemList(V5page v5page) {
        return null;
    }

    private ApiOrderDO getMasterOrder(Long orderId){
        return apiOrderMapper.selectById(orderId);
    }
    private ApiOrderExtDO getOrderExt(Long orderExtId){
        return apiOrderExtMapper.selectById(orderExtId);
    }
    /**
     * 检查是否有有效订单
     */
    public List<ApiOrderExtDO> getExpOrder(Long sellId){
        return apiOrderExtMapper.selectList(new LambdaQueryWrapperX<ApiOrderExtDO>()
                .eq(ApiOrderExtDO::getCommodityInfo, sellId.toString())
                .ne(ApiOrderExtDO::getOrderSubStatus, InvTransferStatusEnum.CLOSE.getStatus())
                .ne(ApiOrderExtDO::getPlatCode, PlatCodeEnum.IO661.getCode())
        );
    }
    public void tradeAsset(Long id,Long orderExtId) {
        ApiOrderDO masterOrder = getMasterOrder(id);
        ApiOrderExtDO orderExt = getOrderExt(orderExtId);
        SellingDO sellingDO = sellingMapper.selectById(Long.parseLong(orderExt.getCommodityInfo()));
        TransferMsg transferMsg=new TransferMsg();
        try{
            if (CommonStatusEnum.isDisable(sellingDO.getStatus())) {
                throw new ServiceException(-1,"库存已经更新无法进行发货。");
            }
            if (PayOrderStatusEnum.isClosed(sellingDO.getStatus())) {
                throw new ServiceException(-1,"已关闭无法进行发货。");
            }
            if (PayOrderStatusEnum.REFUND.getStatus().equals(masterOrder.getPayOrderStatus())) {
                throw new ServiceException(-1,"已退款无法进行发货。");
            }
//            BindUserDO bindUserDO = bindUserMapper.selectById(invOrder.getBuyBindUserId());
//            if(Objects.isNull(bindUserDO)){
//                throw new ServiceException(-1,"收货方绑定关系失败无法发货。");
//            }
            //收货方tradeUrl
            String tradeUrl = masterOrder.getBuyTradeLinks();
//        SteamWeb steamWeb=new SteamWeb(configService);
//        steamWeb.login(bindUserDO.getSteamPassword(),bindUserDO.getMaFile());
//        steamWeb.initTradeUrl();
//        if(!steamWeb.getTreadUrl().isPresent()){
//
//        }
//        steamWeb.checkTradeUrl(steamWeb.getTreadUrl().get());
            BindUserDO bindUserDO1 = bindUserService.getBindUser(sellingDO.getBindUserId());
            if(Objects.isNull(bindUserDO1)){
                throw new ServiceException(-1,"发货方绑定关系失败无法发货。");
            }
            //发货
            Optional<BindIpaddressDO> bindUserIp = steamService.getBindUserIp(bindUserDO1);
            SteamWeb steamWeb=new SteamWeb(configService,bindUserIp);
            if(steamWeb.checkLogin(bindUserDO1)){
                if(steamWeb.getWebApiKey().isPresent()){
                    bindUserDO1.setApiKey(steamWeb.getWebApiKey().get());
                }
                bindUserService.changeBindUserCookie(new BindUserDO().setId(bindUserDO1.getId()).setLoginCookie(steamWeb.getCookieString()).setApiKey(bindUserDO1.getApiKey()));
            }
            SteamInvDto steamInvDto=new SteamInvDto();
            steamInvDto.setAmount(sellingDO.getAmount());
            steamInvDto.setAssetid(sellingDO.getAssetid());
            steamInvDto.setClassid(sellingDO.getClassid());
            steamInvDto.setContextid(sellingDO.getContextid());
            steamInvDto.setInstanceid(sellingDO.getInstanceid());
            steamInvDto.setAppid(sellingDO.getAppid());
            try{
                SteamTradeOfferResult trade = steamWeb.trade(steamInvDto, tradeUrl,"io661 订单号:"+masterOrder.getOrderNo()+"商户号:"+masterOrder.getMerchantNo());
                transferMsg.setTradeofferid(trade.getTradeofferid());
                orderExt.setTradeOfferId(Long.parseLong(trade.getTradeofferid()));
                log.info("发货信息{}",trade);
                Optional<MobileConfList.ConfDTO> confDTO = steamWeb.confirmOfferList(trade.getTradeofferid());
                if (confDTO.isPresent()) {
//                confirmOffer(confDTO.get(), ConfirmAction.CANCEL);
                    steamWeb.confirmOffer(confDTO.get(), ConfirmAction.ALLOW);
                    orderExt.setOrderSubStatus(InvTransferStatusEnum.TransferING.getStatus().toString());
                    sellingDO.setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus());
                }else {
                    log.warn("交易单据未进行手机自动确认{}",trade.getTradeofferid());
                }
            }catch (Exception e){
                log.info("交易单据未进行手机自动确认{}",e.getMessage());
                transferMsg.setErrMsg("交易单据未进行手机自动确认"+e.getMessage());
                orderExt.setOrderSubStatus(InvTransferStatusEnum.TransferERROR.getStatus().toString());
                orderExt.setOrderSubStatus(InvTransferStatusEnum.TransferERROR.getStatus().toString());
                sellingDO.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
            }
//            invOrder.setTransferText(transferMsg);
            apiOrderExtMapper.updateById(orderExt);
            sellingMapper.updateById(sellingDO);
        }catch (ServiceException  e){
            log.error("发送失败原因{}",e.getMessage());
            transferMsg.setMsg(e.getMessage());
            orderExt.setOrderSubStatus(InvTransferStatusEnum.TransferERROR.getStatus().toString());
            sellingDO.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
            orderExt.setOrderSubStatusErrText("发货失败原因"+e.getMessage());
            apiOrderExtMapper.updateById(orderExt);
            sellingMapper.updateById(sellingDO);
            throw e;
        }
//        if(Objects.nonNull(invOrder.getServiceFee()) && invOrder.getServiceFee()>0){
//            setPayInvOrderServiceFee(invOrder);
//        }

    }
}
