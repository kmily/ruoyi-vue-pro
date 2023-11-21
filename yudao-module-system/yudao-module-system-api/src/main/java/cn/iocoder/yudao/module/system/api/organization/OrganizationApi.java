package cn.iocoder.yudao.module.system.api.organization;

import cn.iocoder.yudao.module.system.api.organization.dto.OrganizationDTO;

/**
 * @author whycode
 * @title: OrganizationApi
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/1515:41
 */
public interface OrganizationApi {


    /**
     * 查询机构信息
     * @param id 机构编号
     * @return
     */
    OrganizationDTO getOrganization(Long id);

    /**
     * 获取一个 自营机构
     * @return
     */
    OrganizationDTO getSelfOrganization();

}
