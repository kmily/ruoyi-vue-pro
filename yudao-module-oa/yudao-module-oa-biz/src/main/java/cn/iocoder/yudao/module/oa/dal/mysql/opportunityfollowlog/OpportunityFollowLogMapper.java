package cn.iocoder.yudao.module.oa.dal.mysql.opportunityfollowlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;

/**
 * 商机-跟进日志 Mapper
 *
 * @author 东海
 */
@Mapper
public interface OpportunityFollowLogMapper extends BaseMapperX<OpportunityFollowLogDO> {

    default PageResult<OpportunityFollowLogDO> selectPage(OpportunityFollowLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OpportunityFollowLogDO>()
                .eqIfPresent(OpportunityFollowLogDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(OpportunityFollowLogDO::getLogContent, reqVO.getLogContent())
                .betweenIfPresent(OpportunityFollowLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpportunityFollowLogDO::getId));
    }

    default List<OpportunityFollowLogDO> selectList(OpportunityFollowLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OpportunityFollowLogDO>()
                .eqIfPresent(OpportunityFollowLogDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(OpportunityFollowLogDO::getLogContent, reqVO.getLogContent())
                .betweenIfPresent(OpportunityFollowLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpportunityFollowLogDO::getId));
    }

}
