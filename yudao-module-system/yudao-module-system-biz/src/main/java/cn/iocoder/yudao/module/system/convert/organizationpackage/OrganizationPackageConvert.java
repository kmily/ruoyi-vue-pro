package cn.iocoder.yudao.module.system.convert.organizationpackage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;

/**
 * 机构套餐 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationPackageConvert {

    OrganizationPackageConvert INSTANCE = Mappers.getMapper(OrganizationPackageConvert.class);

    OrganizationPackageDO convert(OrganizationPackageCreateReqVO bean);

    OrganizationPackageDO convert(OrganizationPackageUpdateReqVO bean);

    OrganizationPackageRespVO convert(OrganizationPackageDO bean);

    List<OrganizationPackageRespVO> convertList(List<OrganizationPackageDO> list);

    PageResult<OrganizationPackageRespVO> convertPage(PageResult<OrganizationPackageDO> page);

    List<OrganizationPackageExcelVO> convertList02(List<OrganizationPackageDO> list);

}
