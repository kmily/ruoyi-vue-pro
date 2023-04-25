package cn.iocoder.yudao.module.oa.convert.projectimpllog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;

/**
 * 工程日志列表 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ProjectImplLogConvert {

    ProjectImplLogConvert INSTANCE = Mappers.getMapper(ProjectImplLogConvert.class);

    ProjectImplLogDO convert(ProjectImplLogCreateReqVO bean);

    ProjectImplLogDO convert(ProjectImplLogUpdateReqVO bean);

    ProjectImplLogRespVO convert(ProjectImplLogDO bean);

    List<ProjectImplLogRespVO> convertList(List<ProjectImplLogDO> list);

    PageResult<ProjectImplLogRespVO> convertPage(PageResult<ProjectImplLogDO> page);

    List<ProjectImplLogExcelVO> convertList02(List<ProjectImplLogDO> list);

}
