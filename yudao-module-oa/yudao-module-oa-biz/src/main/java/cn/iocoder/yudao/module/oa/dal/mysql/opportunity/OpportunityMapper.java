package cn.iocoder.yudao.module.oa.dal.mysql.opportunity;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;

/**
 * 商机 Mapper
 *
 * @author 东海
 */
@Mapper
public interface OpportunityMapper extends BaseMapperX<OpportunityDO> {

    default PageResult<OpportunityDO> selectPage(OpportunityPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OpportunityDO>()
                .eqIfPresent(OpportunityDO::getBusinessTitle, reqVO.getBusinessTitle())
                .eqIfPresent(OpportunityDO::getDetail, reqVO.getDetail())
                .betweenIfPresent(OpportunityDO::getReportTime, reqVO.getReportTime())
                .eqIfPresent(OpportunityDO::getFollowUserId, reqVO.getFollowUserId())
                .eqIfPresent(OpportunityDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OpportunityDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(OpportunityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpportunityDO::getId));
    }

    default List<OpportunityDO> selectList(OpportunityExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OpportunityDO>()
                .eqIfPresent(OpportunityDO::getBusinessTitle, reqVO.getBusinessTitle())
                .eqIfPresent(OpportunityDO::getDetail, reqVO.getDetail())
                .betweenIfPresent(OpportunityDO::getReportTime, reqVO.getReportTime())
                .eqIfPresent(OpportunityDO::getFollowUserId, reqVO.getFollowUserId())
                .eqIfPresent(OpportunityDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OpportunityDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(OpportunityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpportunityDO::getId));
    }

}
