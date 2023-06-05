package com.cw.module.trade.dal.mysql.syncrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.cw.module.trade.dal.dataobject.syncrecord.SyncRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.cw.module.trade.controller.admin.syncrecord.vo.*;

/**
 * 账号同步记录 Mapper
 *
 * @author chengjiale
 */
@Mapper
public interface SyncRecordMapper extends BaseMapperX<SyncRecordDO> {

    default PageResult<SyncRecordDO> selectPage(SyncRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SyncRecordDO>()
                .betweenIfPresent(SyncRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SyncRecordDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(SyncRecordDO::getType, reqVO.getType())
                .eqIfPresent(SyncRecordDO::getThirdData, reqVO.getThirdData())
                .orderByDesc(SyncRecordDO::getId));
    }

    default List<SyncRecordDO> selectList(SyncRecordExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SyncRecordDO>()
                .betweenIfPresent(SyncRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SyncRecordDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(SyncRecordDO::getType, reqVO.getType())
                .eqIfPresent(SyncRecordDO::getThirdData, reqVO.getThirdData())
                .orderByDesc(SyncRecordDO::getId));
    }

}
