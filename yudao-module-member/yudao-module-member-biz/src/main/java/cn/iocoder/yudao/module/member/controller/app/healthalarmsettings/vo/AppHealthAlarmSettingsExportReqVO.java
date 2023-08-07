package cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 体征检测雷达设置 Excel 导出 Request VO，参数和 HealthAlarmSettingsPageReqVO 是一致的")
@Data
public class AppHealthAlarmSettingsExportReqVO {

    @Schema(description = "设备ID", example = "13261")
    private Long deviceId;

    @Schema(description = "通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    @Schema(description = "心率异常告警")
    private Boolean heart;

    @Schema(description = "正常心率范围")
    private String heartRange;

    @Schema(description = "呼吸告警设置")
    private Boolean breathe;

    @Schema(description = "正常心率范围 [16,24]")
    private String breatheRange;

    @Schema(description = "体动检测告警")
    private Boolean move;

    @Schema(description = "坐起告警")
    private Boolean sitUp;

    @Schema(description = "离床告警")
    private Boolean outBed;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
