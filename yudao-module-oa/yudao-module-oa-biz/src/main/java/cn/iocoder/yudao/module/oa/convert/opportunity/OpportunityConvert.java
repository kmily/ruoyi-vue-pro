package cn.iocoder.yudao.module.oa.convert.opportunity;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;

/**
 * 商机 Convert
 *
 * @author 管理员
 */
@Mapper
public interface OpportunityConvert {

    OpportunityConvert INSTANCE = Mappers.getMapper(OpportunityConvert.class);

    OpportunityDO convert(OpportunityCreateReqVO bean);

    OpportunityDO convert(OpportunityUpdateReqVO bean);

    OpportunityRespVO convert(OpportunityDO bean);

    List<OpportunityRespVO> convertList(List<OpportunityDO> list);

    PageResult<OpportunityRespVO> convertPage(PageResult<OpportunityDO> page);

    List<OpportunityExcelVO> convertList02(List<OpportunityDO> list);

}
