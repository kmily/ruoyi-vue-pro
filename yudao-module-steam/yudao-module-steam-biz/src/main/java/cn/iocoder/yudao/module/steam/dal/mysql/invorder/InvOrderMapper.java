package cn.iocoder.yudao.module.steam.dal.mysql.invorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.*;

/**
 * steam订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InvOrderMapper extends BaseMapperX<InvOrderDO> {

    default PageResult<InvOrderDO> selectPage(InvOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InvOrderDO>()
                .eqIfPresent(InvOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(InvOrderDO::getSteamId, reqVO.getSteamId())
                .eqIfPresent(InvOrderDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(InvOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(InvOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .betweenIfPresent(InvOrderDO::getPayTime, reqVO.getPayTime())
                .eqIfPresent(InvOrderDO::getPayRefundId, reqVO.getPayRefundId())
                .eqIfPresent(InvOrderDO::getRefundAmount, reqVO.getRefundAmount())
                .betweenIfPresent(InvOrderDO::getRefundTime, reqVO.getRefundTime())
                .betweenIfPresent(InvOrderDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InvOrderDO::getPaymentAmount, reqVO.getPaymentAmount())
                .eqIfPresent(InvOrderDO::getUserType, reqVO.getUserType())
                .eqIfPresent(InvOrderDO::getPayOrderStatus, reqVO.getPayOrderStatus())
                .eqIfPresent(InvOrderDO::getServiceFee, reqVO.getServiceFee())
                .eqIfPresent(InvOrderDO::getServiceFeeRate, reqVO.getServiceFeeRate())
                .eqIfPresent(InvOrderDO::getDiscountAmount, reqVO.getDiscountAmount())
                .eqIfPresent(InvOrderDO::getTransferText, reqVO.getTransferText())
                .eqIfPresent(InvOrderDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(InvOrderDO::getSellId, reqVO.getSellId())
                .eqIfPresent(InvOrderDO::getInvDescId, reqVO.getInvDescId())
                .eqIfPresent(InvOrderDO::getInvId, reqVO.getInvId())
                .eqIfPresent(InvOrderDO::getSellUserType, reqVO.getSellUserType())
                .eqIfPresent(InvOrderDO::getSellUserId, reqVO.getSellUserId())
                .eqIfPresent(InvOrderDO::getSellCashStatus, reqVO.getSellCashStatus())
                .eqIfPresent(InvOrderDO::getCommodityAmount, reqVO.getCommodityAmount())
                .eqIfPresent(InvOrderDO::getServiceFeeUserId, reqVO.getServiceFeeUserId())
                .eqIfPresent(InvOrderDO::getServiceFeeUserType, reqVO.getServiceFeeUserType())
                .eqIfPresent(InvOrderDO::getServiceFeeRet, reqVO.getServiceFeeRet())
                .likeIfPresent(InvOrderDO::getPlatformName, reqVO.getPlatformName())
                .eqIfPresent(InvOrderDO::getPlatformCode, reqVO.getPlatformCode())
                .eqIfPresent(InvOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(InvOrderDO::getMerchantNo, reqVO.getMerchantNo())
                .eqIfPresent(InvOrderDO::getTransferRefundAmount, reqVO.getTransferRefundAmount())
                .eqIfPresent(InvOrderDO::getTransferDamagesAmount, reqVO.getTransferDamagesAmount())
                .betweenIfPresent(InvOrderDO::getTransferDamagesTime, reqVO.getTransferDamagesTime())
                .orderByDesc(InvOrderDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, InvOrderDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getId, id).eq(InvOrderDO::getPayStatus, wherePayed));
    }
}