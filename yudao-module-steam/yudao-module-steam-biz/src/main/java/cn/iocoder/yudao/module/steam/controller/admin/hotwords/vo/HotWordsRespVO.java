package cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 热词搜索 Response VO")
@Data
@ExcelIgnoreUnannotated
public class HotWordsRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("id")
    private Integer id;

    @Schema(description = "热词", example = "龙狙")
    @ExcelProperty("热词")
    private String hotWords;

    @Schema(description = "实际昵称", example = "123")
    @ExcelProperty("实际昵称")
    private String marketName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}