package cn.iocoder.yudao.module.steam.dal.mysql.youyouorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.*;

/**
 * steam订单 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface YouyouOrderMapper extends BaseMapperX<YouyouOrderDO> {

    default PageResult<YouyouOrderDO> selectPage(YouyouOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<YouyouOrderDO>()
                .betweenIfPresent(YouyouOrderDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(YouyouOrderDO::getBuyUserId, reqVO.getBuyUserId())
                .eqIfPresent(YouyouOrderDO::getBuyUserType, reqVO.getBuyUserType())
                .eqIfPresent(YouyouOrderDO::getBuyBindUserId, reqVO.getBuyBindUserId())
                .eqIfPresent(YouyouOrderDO::getBuySteamId, reqVO.getBuySteamId())
                .eqIfPresent(YouyouOrderDO::getBuyTradeLinks, reqVO.getBuyTradeLinks())
                .eqIfPresent(YouyouOrderDO::getSellUserId, reqVO.getSellUserId())
                .eqIfPresent(YouyouOrderDO::getSellUserType, reqVO.getSellUserType())
                .eqIfPresent(YouyouOrderDO::getSellBindUserId, reqVO.getSellBindUserId())
                .eqIfPresent(YouyouOrderDO::getSellSteamId, reqVO.getSellSteamId())
                .eqIfPresent(YouyouOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(YouyouOrderDO::getMerchantNo, reqVO.getMerchantNo())
                .eqIfPresent(YouyouOrderDO::getCommodityTemplateId, reqVO.getCommodityTemplateId())
                .eqIfPresent(YouyouOrderDO::getRealCommodityId, reqVO.getRealCommodityId())
                .eqIfPresent(YouyouOrderDO::getCommodityId, reqVO.getCommodityId())
                .eqIfPresent(YouyouOrderDO::getFastShipping, reqVO.getFastShipping())
                .eqIfPresent(YouyouOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(YouyouOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .betweenIfPresent(YouyouOrderDO::getPayTime, reqVO.getPayTime())
                .eqIfPresent(YouyouOrderDO::getPayAmount, reqVO.getPayAmount())
                .eqIfPresent(YouyouOrderDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(YouyouOrderDO::getPayOrderStatus, reqVO.getPayOrderStatus())
                .eqIfPresent(YouyouOrderDO::getServiceFeeUserType, reqVO.getServiceFeeUserType())
                .eqIfPresent(YouyouOrderDO::getServiceFee, reqVO.getServiceFee())
                .eqIfPresent(YouyouOrderDO::getMerchantOrderNo, reqVO.getMerchantOrderNo())
                .likeIfPresent(YouyouOrderDO::getCommodityHashName, reqVO.getCommodityHashName())
                .eqIfPresent(YouyouOrderDO::getTransferDamagesRet, reqVO.getTransferDamagesRet())
                .eqIfPresent(YouyouOrderDO::getSellCashStatus, reqVO.getSellCashStatus())
                .eqIfPresent(YouyouOrderDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(YouyouOrderDO::getTransferRefundAmount, reqVO.getTransferRefundAmount())
                .eqIfPresent(YouyouOrderDO::getServiceFeeRate, reqVO.getServiceFeeRate())
                .eqIfPresent(YouyouOrderDO::getServiceFeeUserId, reqVO.getServiceFeeUserId())
                .betweenIfPresent(YouyouOrderDO::getTransferDamagesTime, reqVO.getTransferDamagesTime())
                .likeIfPresent(YouyouOrderDO::getMarketName, reqVO.getMarketName())
                .eqIfPresent(YouyouOrderDO::getTransferText, reqVO.getTransferText())
                .eqIfPresent(YouyouOrderDO::getPurchasePrice, reqVO.getPurchasePrice())
                .eqIfPresent(YouyouOrderDO::getServiceFeeRet, reqVO.getServiceFeeRet())
                .eqIfPresent(YouyouOrderDO::getShippingMode, reqVO.getShippingMode())
                .eqIfPresent(YouyouOrderDO::getUuMerchantOrderNo, reqVO.getUuMerchantOrderNo())
                .eqIfPresent(YouyouOrderDO::getUuBuyerUserId, reqVO.getUuBuyerUserId())
                .eqIfPresent(YouyouOrderDO::getUuFailCode, reqVO.getUuFailCode())
                .eqIfPresent(YouyouOrderDO::getUuFailReason, reqVO.getUuFailReason())
                .eqIfPresent(YouyouOrderDO::getUuOrderNo, reqVO.getUuOrderNo())
                .eqIfPresent(YouyouOrderDO::getUuNotifyDesc, reqVO.getUuNotifyDesc())
                .eqIfPresent(YouyouOrderDO::getUuNotifyType, reqVO.getUuNotifyType())
                .eqIfPresent(YouyouOrderDO::getUuOrderStatus, reqVO.getUuOrderStatus())
                .eqIfPresent(YouyouOrderDO::getUuOrderSubStatus, reqVO.getUuOrderSubStatus())
                .eqIfPresent(YouyouOrderDO::getUuOrderSubType, reqVO.getUuOrderSubType())
                .eqIfPresent(YouyouOrderDO::getUuOrderType, reqVO.getUuOrderType())
                .eqIfPresent(YouyouOrderDO::getUuShippingMode, reqVO.getUuShippingMode())
                .eqIfPresent(YouyouOrderDO::getUuTradeOfferId, reqVO.getUuTradeOfferId())
                .eqIfPresent(YouyouOrderDO::getUuTradeOfferLinks, reqVO.getUuTradeOfferLinks())
                .orderByDesc(YouyouOrderDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, YouyouOrderDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<YouyouOrderDO>()
                .eq(YouyouOrderDO::getId, id).eq(YouyouOrderDO::getPayStatus, wherePayed));
    }
}