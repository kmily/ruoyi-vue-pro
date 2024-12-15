package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 产品区域配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductLimitAreaRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12668")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "产品限制ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22058")
    @ExcelProperty("产品限制ID")
    private Long haokaProductLimitId;

    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地区")
    private Integer addressCode;

    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("地区")
    private String addressName;

    @Schema(description = "是否允许")
    @ExcelProperty("是否允许")
    private Boolean allowed;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}