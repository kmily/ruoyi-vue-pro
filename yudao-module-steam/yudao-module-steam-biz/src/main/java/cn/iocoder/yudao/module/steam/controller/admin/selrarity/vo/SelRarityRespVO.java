package cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 品质选择 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SelRarityRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2155")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "中文名称", example = "赵六")
    @ExcelProperty("中文名称")
    private String localizedTagName;

    @Schema(description = "英文名称", example = "王五")
    @ExcelProperty("英文名称")
    private String internalName;

    @Schema(description = "色彩", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("色彩")
    private String color;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}