package cn.iocoder.yudao.module.system.controller.admin.codingrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 编码规则明细新增/修改 Request VO")
@Data
public class CodingRulesDetailsSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10249")
    private String id;

    @Schema(description = "编码规则头id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9725")
    @NotEmpty(message = "编码规则头id不能为空")
    private String ruleId;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "序号不能为空")
    private Integer orderNum;

    @Schema(description = "类型 1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "类型 1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号不能为空")
    private String type;

    @Schema(description = "设置值")
    private String value;

    @Schema(description = "长度")
    private Integer len;

    @Schema(description = "起始值")
    private Integer initial;

    @Schema(description = "步长")
    private Integer stepSize;

    @Schema(description = "补位符")
    private String fillKey;

    @Schema(description = "备注", example = "你说的对")
    private String remark;
}
