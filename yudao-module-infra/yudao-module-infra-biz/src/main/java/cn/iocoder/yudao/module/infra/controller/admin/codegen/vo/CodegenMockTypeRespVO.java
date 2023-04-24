package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 模拟类型 Response VO")
@Data
public class CodegenMockTypeRespVO {

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "不模拟")
    private String lable;

}
