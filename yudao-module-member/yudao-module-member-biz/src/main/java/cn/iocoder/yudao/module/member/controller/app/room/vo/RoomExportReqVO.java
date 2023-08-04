package cn.iocoder.yudao.module.member.controller.app.room.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户房间 Excel 导出 Request VO，参数和 RoomPageReqVO 是一致的")
@Data
public class RoomExportReqVO {

    @Schema(description = "用户ID", example = "5148")
    private Long userId;

    @Schema(description = "家庭ID", example = "7466")
    private Long familyId;

    @Schema(description = "房间名称", example = "芋艿")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
