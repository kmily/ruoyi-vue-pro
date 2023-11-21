package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 组织机构 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationRespVO extends OrganizationBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28439")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "身份证号码")
    @NotNull(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "联系方式")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;
}
