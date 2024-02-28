package cn.iocoder.yudao.module.steam.controller.admin.adblock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 广告位 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AdBlockRespVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "17543")
    @ExcelProperty("Primary Key")
    private Long id;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "广告名称", example = "李四")
    @ExcelProperty("广告名称")
    private String adName;

}