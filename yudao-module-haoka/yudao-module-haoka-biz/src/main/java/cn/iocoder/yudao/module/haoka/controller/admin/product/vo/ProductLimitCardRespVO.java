package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 产品身份证限制 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductLimitCardRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16180")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "产品限制ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12808")
    @ExcelProperty("产品限制ID")
    private Long haokaProductLimitId;

    @Schema(description = "身份证号前4或6位")
    @ExcelProperty("身份证号前4或6位")
    private Integer cardNum;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}