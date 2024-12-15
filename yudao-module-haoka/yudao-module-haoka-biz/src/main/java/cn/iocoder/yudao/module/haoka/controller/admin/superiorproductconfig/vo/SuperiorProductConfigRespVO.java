package cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 产品对接上游配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SuperiorProductConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28128")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "上游接口ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31755")
    @ExcelProperty("上游接口ID")
    private Long haokaSuperiorApiId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "320")
    @ExcelProperty("产品ID")
    private Long haokaProductId;

    @Schema(description = "是否已配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否已配置", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean isConfined;

    @Schema(description = "值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("值")
    private String config;

    @Schema(description = "说明")
    @ExcelProperty("说明")
    private String remarks;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}