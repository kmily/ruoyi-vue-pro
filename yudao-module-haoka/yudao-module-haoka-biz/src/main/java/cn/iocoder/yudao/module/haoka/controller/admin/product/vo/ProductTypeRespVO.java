package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 产品类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductTypeRespVO {

    @Schema(description = "产品类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10215")
    @ExcelProperty("产品类型ID")
    private Long id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("产品类型名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}