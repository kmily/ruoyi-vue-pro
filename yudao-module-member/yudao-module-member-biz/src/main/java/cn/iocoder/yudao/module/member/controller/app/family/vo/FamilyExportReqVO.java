package cn.iocoder.yudao.module.member.controller.app.family.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户家庭 Excel 导出 Request VO，参数和 FamilyPageReqVO 是一致的")
@Data
public class FamilyExportReqVO {

    @Schema(description = "用户ID", example = "2371")
    private Long userId;

    @Schema(description = "家庭名称", example = "赵六")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
