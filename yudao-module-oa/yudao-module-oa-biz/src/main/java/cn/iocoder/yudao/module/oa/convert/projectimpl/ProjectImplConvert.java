package cn.iocoder.yudao.module.oa.convert.projectimpl;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;

/**
 * 工程实施列 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ProjectImplConvert {

    ProjectImplConvert INSTANCE = Mappers.getMapper(ProjectImplConvert.class);

    ProjectImplDO convert(ProjectImplCreateReqVO bean);

    ProjectImplDO convert(ProjectImplUpdateReqVO bean);

    ProjectImplRespVO convert(ProjectImplDO bean);

    List<ProjectImplRespVO> convertList(List<ProjectImplDO> list);

    PageResult<ProjectImplRespVO> convertPage(PageResult<ProjectImplDO> page);

    List<ProjectImplExcelVO> convertList02(List<ProjectImplDO> list);

}
