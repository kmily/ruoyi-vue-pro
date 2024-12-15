package cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 产品对接上游配置新增/修改 Request VO")
@Data
public class SuperiorProductConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28128")
    private Long id;

    @Schema(description = "上游接口ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31755")
    @NotNull(message = "上游接口ID不能为空")
    private Long haokaSuperiorApiId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "320")
    @NotNull(message = "产品ID不能为空")
    private Long haokaProductId;

    @Schema(description = "是否已配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否已配置不能为空")
    private Boolean isConfined;

    @Schema(description = "值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "值不能为空")
    private String config;

    @Schema(description = "说明")
    private String remarks;

}