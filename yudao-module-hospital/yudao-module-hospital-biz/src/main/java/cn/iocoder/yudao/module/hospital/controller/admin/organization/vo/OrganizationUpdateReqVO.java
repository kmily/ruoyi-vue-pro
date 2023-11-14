package cn.iocoder.yudao.module.hospital.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 组织机构更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationUpdateReqVO extends OrganizationBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28439")
    @NotNull(message = "ID不能为空")
    private Long id;

}
