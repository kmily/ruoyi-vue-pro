package cn.iocoder.yudao.module.steam.controller.admin.selquality.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 类别选择 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SelQualityRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "00001")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "英文名", requiredMode = Schema.RequiredMode.REQUIRED, example = "strange")
    @ExcelProperty("英文名")
    private String internalName;

    @Schema(description = "中文", requiredMode = Schema.RequiredMode.REQUIRED, example = "StatTrak™")
    @ExcelProperty("中文")
    private String localizedTagName;

    @Schema(description = "颜色", example = "CF6A32")
    @ExcelProperty("颜色")
    private String color;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}