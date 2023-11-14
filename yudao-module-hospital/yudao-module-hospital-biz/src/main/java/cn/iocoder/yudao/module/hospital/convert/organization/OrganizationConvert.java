package cn.iocoder.yudao.module.hospital.convert.organization;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organization.OrganizationDO;

/**
 * 组织机构 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationConvert {

    OrganizationConvert INSTANCE = Mappers.getMapper(OrganizationConvert.class);

    OrganizationDO convert(OrganizationCreateReqVO bean);

    OrganizationDO convert(OrganizationUpdateReqVO bean);

    OrganizationRespVO convert(OrganizationDO bean);

    List<OrganizationRespVO> convertList(List<OrganizationDO> list);

    PageResult<OrganizationRespVO> convertPage(PageResult<OrganizationDO> page);

    List<OrganizationExcelVO> convertList02(List<OrganizationDO> list);

}
