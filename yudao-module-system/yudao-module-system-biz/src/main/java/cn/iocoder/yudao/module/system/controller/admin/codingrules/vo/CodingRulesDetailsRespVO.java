package cn.iocoder.yudao.module.system.controller.admin.codingrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 编码规则明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CodingRulesDetailsRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10249")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "编码规则头id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9725")
    @ExcelProperty("编码规则头id")
    private String ruleId;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("序号")
    private Integer orderNum;

    @Schema(description = "类型 1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("类型 1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号")
    private String type;

    @Schema(description = "设置值")
    @ExcelProperty("设置值")
    private String value;

    @Schema(description = "长度")
    @ExcelProperty("长度")
    private Integer len;

    @Schema(description = "起始值")
    @ExcelProperty("起始值")
    private Integer initial;

    @Schema(description = "步长")
    @ExcelProperty("步长")
    private Integer stepSize;

    @Schema(description = "补位符")
    @ExcelProperty("补位符")
    private String fillKey;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
