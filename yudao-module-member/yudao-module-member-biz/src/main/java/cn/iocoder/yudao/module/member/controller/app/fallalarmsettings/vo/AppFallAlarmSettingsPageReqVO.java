package cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 跌倒雷达设置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppFallAlarmSettingsPageReqVO extends PageParam {

    @Schema(description = "设备ID", example = "7237")
    private Long deviceId;

    @Schema(description = "通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    @Schema(description = "进入告警")
    private Boolean enter;

    @Schema(description = "离开告警")
    private Boolean leave;

    @Schema(description = "跌倒告警")
    private Boolean fall;

    @Schema(description = "起身消警")
    private Boolean getUp;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
