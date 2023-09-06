package cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 人体检测雷达设置 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDetectionAlarmSettingsRespVO extends AppDetectionAlarmSettingsBaseVO {

    @Schema(description = "自增编号", required = true, example = "1087")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;
    @Schema(description = "设备编号", required = true)
    private String sn;
}
