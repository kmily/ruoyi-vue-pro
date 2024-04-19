package cn.iocoder.yudao.module.steam.dal.mysql.offlinerechange;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;

/**
 * 线下人工充值 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface OfflineRechangeMapper extends BaseMapperX<OfflineRechangeDO> {

    default PageResult<OfflineRechangeDO> selectPage(OfflineRechangePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OfflineRechangeDO>()
                .betweenIfPresent(OfflineRechangeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OfflineRechangeDO::getUserId, reqVO.getUserId())
                .eqIfPresent(OfflineRechangeDO::getUserType, reqVO.getUserType())
                .eqIfPresent(OfflineRechangeDO::getAmount, reqVO.getAmount())
                .eqIfPresent(OfflineRechangeDO::getComment, reqVO.getComment())
                .orderByDesc(OfflineRechangeDO::getId));
    }

}