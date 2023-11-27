package cn.iocoder.yudao.module.member.controller.admin.serverperson.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class ServerPersonAuditVO {

    @Schema(description = "被户人编号")
    @NotNull(message = "被户人编号不能为空")
    private Long id;

    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    @Pattern(message = "状态只能是，", regexp = "OPEN|REFUSED")
    private String status;

    @Schema(description = "审核意见")
    private String opinion;

}
