package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 上游API接口 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SuperiorApiRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24472")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "是否有选号功能", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否有选号功能", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean enableSelectNum;

    @Schema(description = "异常订单处理方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "异常订单处理方式", converter = DictConvert.class)
    @DictFormat("abnormal_order_handle_method") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer abnormalOrderHandleMethod;

    @Schema(description = "接口状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "接口状态", converter = DictConvert.class)
    @DictFormat("haoka_superior_api_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "发布日期")
    @ExcelProperty("发布日期")
    private LocalDateTime publishTime;

    @Schema(description = "API文档")
    @ExcelProperty("API文档")
    private String apiDoc;

    @Schema(description = "是否已配置开发")
    @ExcelProperty(value = "是否已配置开发", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean isDevConfined;

    @Schema(description = "是否已配置产品")
    @ExcelProperty(value = "是否已配置产品", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean isSkuConfined;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}