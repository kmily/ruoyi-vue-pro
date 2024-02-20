package cn.iocoder.yudao.module.steam.dal.mysql.withdrawal;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo.*;

/**
 * 提现 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WithdrawalMapper extends BaseMapperX<WithdrawalDO> {

    default PageResult<WithdrawalDO> selectPage(WithdrawalPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WithdrawalDO>()
                .eqIfPresent(WithdrawalDO::getUserId, reqVO.getUserId())
                .eqIfPresent(WithdrawalDO::getUserType, reqVO.getUserType())
                .eqIfPresent(WithdrawalDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(WithdrawalDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(WithdrawalDO::getPayChannelCode, reqVO.getPayChannelCode())
                .betweenIfPresent(WithdrawalDO::getPayTime, reqVO.getPayTime())
                .eqIfPresent(WithdrawalDO::getPayRefundId, reqVO.getPayRefundId())
                .eqIfPresent(WithdrawalDO::getRefundPrice, reqVO.getRefundPrice())
                .betweenIfPresent(WithdrawalDO::getRefundTime, reqVO.getRefundTime())
                .eqIfPresent(WithdrawalDO::getPrice, reqVO.getPrice())
                .betweenIfPresent(WithdrawalDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WithdrawalDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, WithdrawalDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<WithdrawalDO>()
                .eq(WithdrawalDO::getId, id).eq(WithdrawalDO::getPayStatus, wherePayed));
    }
}