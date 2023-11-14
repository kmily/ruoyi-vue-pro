package cn.iocoder.yudao.module.hospital.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author whycode
 * @title: 机构审计请求类
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/149:20
 */

@Schema(description = "后台管理 机构申请既如此")
@Data
@Accessors
public class OrganizationAuditVO {

    @Schema(description = "机构编号")
    @NotNull(message = "机构编号不能为空")
    private Long id;

    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    @Size(min = 0, max = 1, message = "状态只能是0或1")
    private Integer status;

    @Schema(description = "审核意见")
    private String opinion;
}
