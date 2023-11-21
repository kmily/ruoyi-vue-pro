package cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 机构套餐更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationPackageUpdateReqVO extends OrganizationPackageBaseVO {

    @Schema(description = "套餐编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17687")
    @NotNull(message = "套餐编号不能为空")
    private Long id;

}
