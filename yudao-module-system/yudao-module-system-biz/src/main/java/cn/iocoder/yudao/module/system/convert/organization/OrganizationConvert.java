package cn.iocoder.yudao.module.system.convert.organization;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.organization.dto.OrganizationDTO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationRespVO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    OrganizationDTO convert01(OrganizationDO organization);
}
