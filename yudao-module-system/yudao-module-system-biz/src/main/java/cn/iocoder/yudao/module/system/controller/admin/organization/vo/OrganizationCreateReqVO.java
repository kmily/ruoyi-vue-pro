package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 组织机构创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationCreateReqVO extends OrganizationBaseVO {


    @Schema(description = "身份证号码")
    @NotNull(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "联系方式")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
    @Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;
}
