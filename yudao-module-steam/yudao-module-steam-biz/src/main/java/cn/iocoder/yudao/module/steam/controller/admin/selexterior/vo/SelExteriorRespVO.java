package cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 外观选择 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SelExteriorRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "000001")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "WearCategory1")
    @ExcelProperty("名字")
    private String internalName;

    @Schema(description = "中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "略有磨损")
    @ExcelProperty("中文名称")
    private String localizedTagName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "字体颜色", example = "#B7625F")
    @ExcelProperty("字体颜色")
    private String color;

}