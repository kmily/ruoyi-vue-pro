package cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 机构套餐 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationPackageRespVO extends OrganizationPackageBaseVO {

    @Schema(description = "套餐编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17687")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
