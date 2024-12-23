package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 上游API接口开发配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SuperiorApiDevConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4692")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "627")
    @ExcelProperty("ID")
    private Long haokaSuperiorApiId;

    @Schema(description = "标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标识")
    private String code;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("值")
    private String value;

    @Schema(description = "说明")
    @ExcelProperty("说明")
    private String remarks;

    @Schema(description = "输入类型", example = "2")
    @ExcelProperty("输入类型")
    private Integer inputType;

    @Schema(description = "选项(逗号,分割)")
    @ExcelProperty("选项(逗号,分割)")
    private String inputSelectValues;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}