package cn.iocoder.yudao.module.steam.dal.mysql.inv;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.*;

/**
 * 用户库存储 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InvMapper extends BaseMapperX<InvDO> {

    default PageResult<InvDO> selectPage(InvPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InvDO>()
                .eqIfPresent(InvDO::getClassid, reqVO.getClassid())
                .eqIfPresent(InvDO::getInstanceid, reqVO.getInstanceid())
                .eqIfPresent(InvDO::getAmount, reqVO.getAmount())
                .betweenIfPresent(InvDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InvDO::getSteamId, reqVO.getSteamId())
                .eqIfPresent(InvDO::getAppid, reqVO.getAppid())
                .eqIfPresent(InvDO::getBindUserId, reqVO.getBindUserId())
                .eqIfPresent(InvDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InvDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(InvDO::getUserId, reqVO.getUserId())
                .eqIfPresent(InvDO::getUserType, reqVO.getUserType())
                .eqIfPresent(InvDO::getAssetid, reqVO.getAssetid())
                .eqIfPresent(InvDO::getPrice, reqVO.getPrice())
                .orderByDesc(InvDO::getId));
    }

}