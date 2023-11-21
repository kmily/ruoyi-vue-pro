package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 组织机构更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationUpdateReqVO extends OrganizationBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28439")
    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(description = "身份证号码")
    @NotNull(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "联系方式")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;
}
