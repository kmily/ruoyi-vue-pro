package cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 产品对接上游配置新增/修改 Request VO")
@Data
public class SuperiorProductConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21785")
    private Long id;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7050")
    @NotNull(message = "ID不能为空")
    private Long haokaSuperiorApiId;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26555")
    @NotNull(message = "ID不能为空")
    private Long haokaProductId;

    @Schema(description = "是否已配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否已配置不能为空")
    private Boolean isConfined;

    @Schema(description = "值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "值不能为空")
    private String config;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否必填不能为空")
    private Boolean required;

    @Schema(description = "说明", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "说明不能为空")
    private String remarks;

    @Schema(description = "部门ID", example = "30274")
    private Long deptId;

}