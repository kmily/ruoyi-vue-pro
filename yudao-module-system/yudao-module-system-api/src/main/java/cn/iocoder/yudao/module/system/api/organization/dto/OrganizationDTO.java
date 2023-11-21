package cn.iocoder.yudao.module.system.api.organization.dto;

import lombok.Data;

/**
 * @author whycode
 * @title: OrganizationDTO y
 * @projectName home-care
 * @description: 机构信息
 * @date 2023/11/1515:42
 */

@Data
public class OrganizationDTO {

    /**
     * 机构编号
     */
    private Long id;

    /**
     * 机构状态
     */
    private String disable;

    /**
     * 机构名称
     */
    private String name;

}
