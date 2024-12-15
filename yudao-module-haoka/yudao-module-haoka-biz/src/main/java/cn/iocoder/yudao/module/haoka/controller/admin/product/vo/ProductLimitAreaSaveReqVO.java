package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 产品区域配置新增/修改 Request VO")
@Data
public class ProductLimitAreaSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12668")
    private Long id;

    @Schema(description = "产品限制ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22058")
    @NotNull(message = "产品限制ID不能为空")
    private Long haokaProductLimitId;

    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "地区不能为空")
    private Integer addressCode;

    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "地区不能为空")
    private String addressName;

    @Schema(description = "是否允许")
    private Boolean allowed;

}