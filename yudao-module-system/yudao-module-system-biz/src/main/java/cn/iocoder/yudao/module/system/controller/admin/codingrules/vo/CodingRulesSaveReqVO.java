package cn.iocoder.yudao.module.system.controller.admin.codingrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 编号规则表头新增/修改 Request VO")
@Data
public class CodingRulesSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19250")
    private String id;

    @Schema(description = "规则编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "规则编码不能为空")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "描述", example = "随便")
    private String remark;

}
