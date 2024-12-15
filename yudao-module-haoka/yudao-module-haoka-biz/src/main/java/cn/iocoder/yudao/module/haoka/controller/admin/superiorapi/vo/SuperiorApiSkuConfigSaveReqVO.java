package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 上游API接口SKU要求配置新增/修改 Request VO")
@Data
public class SuperiorApiSkuConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27058")
    private Long id;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13435")
    @NotNull(message = "ID不能为空")
    private Long haokaSuperiorApiId;

    @Schema(description = "标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标识不能为空")
    private String code;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "名字不能为空")
    private String name;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否必填不能为空")
    private Boolean required;

    @Schema(description = "说明", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "说明不能为空")
    private String remarks;

    @Schema(description = "输入类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "输入类型不能为空")
    private Integer inputType;

    @Schema(description = "选项(逗号,分割)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "选项(逗号,分割)不能为空")
    private String inputSelectValues;

    @Schema(description = "部门ID", example = "3735")
    private Long deptId;

}