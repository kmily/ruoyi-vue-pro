package com.cw.module.trade.dal.mysql.followrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.cw.module.trade.controller.admin.followrecord.vo.*;

/**
 * 账号跟随记录 Mapper
 *
 * @author chengjiale
 */
@Mapper
public interface FollowRecordMapper extends BaseMapperX<FollowRecordDO> {

    default PageResult<FollowRecordDO> selectPage(FollowRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FollowRecordDO>()
                .betweenIfPresent(FollowRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(FollowRecordDO::getAccountNotifyId, reqVO.getAccountNotifyId())
                .eqIfPresent(FollowRecordDO::getFollowAccount, reqVO.getFollowAccount())
                .eqIfPresent(FollowRecordDO::getOperateAccount, reqVO.getOperateAccount())
                .betweenIfPresent(FollowRecordDO::getOperateTime, reqVO.getOperateTime())
                .eqIfPresent(FollowRecordDO::getOperateInfo, reqVO.getOperateInfo())
                .eqIfPresent(FollowRecordDO::getOperateSuccess, reqVO.getOperateSuccess())
                .eqIfPresent(FollowRecordDO::getOperateResult, reqVO.getOperateResult())
                .eqIfPresent(FollowRecordDO::getOperateDesc, reqVO.getOperateDesc())
                .orderByDesc(FollowRecordDO::getId));
    }

    default List<FollowRecordDO> selectList(FollowRecordExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FollowRecordDO>()
                .betweenIfPresent(FollowRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(FollowRecordDO::getAccountNotifyId, reqVO.getAccountNotifyId())
                .eqIfPresent(FollowRecordDO::getFollowAccount, reqVO.getFollowAccount())
                .eqIfPresent(FollowRecordDO::getOperateAccount, reqVO.getOperateAccount())
                .betweenIfPresent(FollowRecordDO::getOperateTime, reqVO.getOperateTime())
                .eqIfPresent(FollowRecordDO::getOperateInfo, reqVO.getOperateInfo())
                .eqIfPresent(FollowRecordDO::getOperateSuccess, reqVO.getOperateSuccess())
                .eqIfPresent(FollowRecordDO::getOperateResult, reqVO.getOperateResult())
                .eqIfPresent(FollowRecordDO::getOperateDesc, reqVO.getOperateDesc())
                .orderByDesc(FollowRecordDO::getId));
    }

}
