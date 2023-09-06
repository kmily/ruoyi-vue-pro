package cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 跌倒雷达设置 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppFallAlarmSettingsRespVO extends AppFallAlarmSettingsBaseVO {

    @Schema(description = "自增编号", required = true, example = "8559")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;
    @Schema(description = "设备编号", required = true)
    private String sn;
}
