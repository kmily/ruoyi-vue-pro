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
                .eqIfPresent(YouyouOrderDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(YouyouOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(YouyouOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .betweenIfPresent(YouyouOrderDO::getPayTime, reqVO.getPayTime())
                .eqIfPresent(YouyouOrderDO::getRefundPrice, reqVO.getRefundPrice())
                .eqIfPresent(YouyouOrderDO::getPayRefundId, reqVO.getPayRefundId())
                .betweenIfPresent(YouyouOrderDO::getRefundTime, reqVO.getRefundTime())
                .betweenIfPresent(YouyouOrderDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(YouyouOrderDO::getUserType, reqVO.getUserType())
                .eqIfPresent(YouyouOrderDO::getTransferText, reqVO.getTransferText())
                .eqIfPresent(YouyouOrderDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(YouyouOrderDO::getMerchantOrderNo, reqVO.getMerchantOrderNo())
                .eqIfPresent(YouyouOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(YouyouOrderDO::getShippingMode, reqVO.getShippingMode())
                .eqIfPresent(YouyouOrderDO::getCommodityTemplateId, reqVO.getCommodityTemplateId())
                .likeIfPresent(YouyouOrderDO::getCommodityHashName, reqVO.getCommodityHashName())
                .eqIfPresent(YouyouOrderDO::getCommodityId, reqVO.getCommodityId())
                .eqIfPresent(YouyouOrderDO::getPurchasePrice, reqVO.getPurchasePrice())
                .eqIfPresent(YouyouOrderDO::getRealCommodityId, reqVO.getRealCommodityId())
                .eqIfPresent(YouyouOrderDO::getUuOrderNo, reqVO.getUuOrderNo())
                .eqIfPresent(YouyouOrderDO::getUuMerchantOrderNo, reqVO.getUuMerchantOrderNo())
                .eqIfPresent(YouyouOrderDO::getUuOrderStatus, reqVO.getUuOrderStatus())
                .eqIfPresent(YouyouOrderDO::getSellCashStatus, reqVO.getSellCashStatus())
                .eqIfPresent(YouyouOrderDO::getSellUserId, reqVO.getSellUserId())
                .eqIfPresent(YouyouOrderDO::getSellUserType, reqVO.getSellUserType())
                .orderByDesc(YouyouOrderDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, YouyouOrderDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<YouyouOrderDO>()
                .eq(YouyouOrderDO::getId, id).eq(YouyouOrderDO::getPayStatus, wherePayed));
    }
}