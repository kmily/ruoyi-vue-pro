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
                .eqIfPresent(InvOrderDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(InvOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(InvOrderDO::getUserType, reqVO.getUserType())
                .eqIfPresent(InvOrderDO::getSteamId, reqVO.getSteamId())
                .eqIfPresent(InvOrderDO::getTransferText, reqVO.getTransferText())
                .eqIfPresent(InvOrderDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(InvOrderDO::getPayOrderStatus, reqVO.getPayOrderStatus())
                .eqIfPresent(InvOrderDO::getSellId, reqVO.getSellId())
                .eqIfPresent(InvOrderDO::getSellUserType, reqVO.getSellUserType())
                .eqIfPresent(InvOrderDO::getSellUserId, reqVO.getSellUserId())
                .eqIfPresent(InvOrderDO::getSellCashStatus, reqVO.getSellCashStatus())
                .orderByDesc(InvOrderDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, InvOrderDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getId, id).eq(InvOrderDO::getPayStatus, wherePayed));
    }
}