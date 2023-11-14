package cn.iocoder.yudao.module.hospital.convert.organizationchecklog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organizationchecklog.OrganizationCheckLogDO;

/**
 * 组织审核记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationCheckLogConvert {

    OrganizationCheckLogConvert INSTANCE = Mappers.getMapper(OrganizationCheckLogConvert.class);

    OrganizationCheckLogDO convert(OrganizationCheckLogCreateReqVO bean);

    OrganizationCheckLogDO convert(OrganizationCheckLogUpdateReqVO bean);

    OrganizationCheckLogRespVO convert(OrganizationCheckLogDO bean);

    List<OrganizationCheckLogRespVO> convertList(List<OrganizationCheckLogDO> list);

    PageResult<OrganizationCheckLogRespVO> convertPage(PageResult<OrganizationCheckLogDO> page);

    List<OrganizationCheckLogExcelVO> convertList02(List<OrganizationCheckLogDO> list);

}
