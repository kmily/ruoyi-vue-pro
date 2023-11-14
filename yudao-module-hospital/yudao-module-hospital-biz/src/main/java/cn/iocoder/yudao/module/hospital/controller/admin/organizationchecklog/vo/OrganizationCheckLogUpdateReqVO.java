package cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 组织审核记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationCheckLogUpdateReqVO extends OrganizationCheckLogBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4475")
    @NotNull(message = "ID不能为空")
    private Long id;

}
