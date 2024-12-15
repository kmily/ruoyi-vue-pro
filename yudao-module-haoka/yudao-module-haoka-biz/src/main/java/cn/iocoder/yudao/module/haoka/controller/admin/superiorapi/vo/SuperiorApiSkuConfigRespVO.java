package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 上游API接口SKU要求配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SuperiorApiSkuConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27058")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13435")
    @ExcelProperty("ID")
    private Long haokaSuperiorApiId;

    @Schema(description = "标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标识")
    private String code;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否必填", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean required;

    @Schema(description = "说明", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("说明")
    private String remarks;

    @Schema(description = "输入类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "输入类型", converter = DictConvert.class)
    @DictFormat("haoka_superior_api_input_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer inputType;

    @Schema(description = "选项(逗号,分割)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("选项(逗号,分割)")
    private String inputSelectValues;

    @Schema(description = "部门ID", example = "3735")
    @ExcelProperty("部门ID")
    private Long deptId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}