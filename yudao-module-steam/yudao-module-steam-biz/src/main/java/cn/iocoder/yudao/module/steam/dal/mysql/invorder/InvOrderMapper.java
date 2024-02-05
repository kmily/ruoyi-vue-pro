package cn.iocoder.yudao.module.steam.dal.mysql.invorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.demo.PayDemoOrderDO;
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
                .eqIfPresent(InvOrderDO::getAssetid, reqVO.getAssetid())
                .eqIfPresent(InvOrderDO::getClassid, reqVO.getClassid())
                .eqIfPresent(InvOrderDO::getInstanceid, reqVO.getInstanceid())
                .eqIfPresent(InvOrderDO::getPayStatus, reqVO.getPayStatus())
                .eqIfPresent(InvOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .orderByDesc(InvOrderDO::getId));
    }
    default int updateByIdAndPayed(Long id, boolean wherePayed, InvOrderDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getId, id).eq(InvOrderDO::getPayStatus, wherePayed));
    }
}