package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 考勤打卡 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendanceRespVO extends AttendanceBaseVO {

    @Schema(description = "id", required = true, example = "25123")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
