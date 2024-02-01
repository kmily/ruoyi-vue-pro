package cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 收藏品选择 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SelItemsetRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3534")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "父级编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15350")
    @ExcelProperty("父级编号")
    private Long parentId;

    @Schema(description = "英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("英文名称")
    private String internalName;

    @Schema(description = "中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("中文名称")
    private String localizedTagName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}