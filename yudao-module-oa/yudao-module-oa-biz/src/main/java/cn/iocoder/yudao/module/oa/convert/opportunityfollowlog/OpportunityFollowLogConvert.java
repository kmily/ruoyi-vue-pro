package cn.iocoder.yudao.module.oa.convert.opportunityfollowlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;

/**
 * 商机-跟进日志 Convert
 *
 * @author 东海
 */
@Mapper
public interface OpportunityFollowLogConvert {

    OpportunityFollowLogConvert INSTANCE = Mappers.getMapper(OpportunityFollowLogConvert.class);

    OpportunityFollowLogDO convert(OpportunityFollowLogCreateReqVO bean);

    OpportunityFollowLogDO convert(OpportunityFollowLogUpdateReqVO bean);

    OpportunityFollowLogRespVO convert(OpportunityFollowLogDO bean);

    List<OpportunityFollowLogRespVO> convertList(List<OpportunityFollowLogDO> list);

    PageResult<OpportunityFollowLogRespVO> convertPage(PageResult<OpportunityFollowLogDO> page);

    List<OpportunityFollowLogExcelVO> convertList02(List<OpportunityFollowLogDO> list);

}
