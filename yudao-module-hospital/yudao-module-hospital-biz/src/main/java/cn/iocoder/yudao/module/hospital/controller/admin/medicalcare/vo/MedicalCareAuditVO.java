package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author whycode
 * @title: MedicalCareAuditVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2014:15
 */

@Schema(description = "后台管理 医护审批请求")
@Data
@Accessors
public class MedicalCareAuditVO {

    @Schema(description = "机构编号")
    @NotNull(message = "机构编号不能为空")
    private Long id;

    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    @Pattern(message = "状态只能是，", regexp = "OPEN|REFUSED")
    private String status;

    @Schema(description = "审核意见")
    private String opinion;

}