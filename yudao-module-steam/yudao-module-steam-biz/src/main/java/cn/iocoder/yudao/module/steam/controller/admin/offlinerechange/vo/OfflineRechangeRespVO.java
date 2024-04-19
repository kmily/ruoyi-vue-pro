package cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 线下人工充值 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OfflineRechangeRespVO {

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "用户ID", example = "17047")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户类型", example = "1")
    @ExcelProperty("用户类型")
    private Integer userType;

    @Schema(description = "充值金额")
    @ExcelProperty("充值金额")
    private Integer amount;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String comment;

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "3219")
    @ExcelProperty("Primary Key")
    private Long id;
    @Schema(description = "手机")
    @ExcelProperty("手机")
    private String mobile;
    @ExcelProperty("手机")
    private Integer rechangeType;
}