package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 睡眠统计记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthStatisticsPageReqVO extends PageParam {

    @Schema(description = "设备ID", example = "9583")
    private Long deviceId;

    @Schema(description = "日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] startTime;

    @Schema(description = "呼吸最大值")
    private Double breathMaxValue;

    @Schema(description = "呼吸最大值")
    private Double breathMinValue;

    @Schema(description = "呼吸静息")
    private Double breathSilent;

    @Schema(description = "呼吸平均")
    private Double breathAverage;

    @Schema(description = "心率最大值")
    private Double heartMaxValue;

    @Schema(description = "心率最小值")
    private Double heartMinValue;

    @Schema(description = "心率静息")
    private Double heartSilent;

    @Schema(description = "心率平均")
    private Double heartAverage;

    @Schema(description = "开始睡眠时间")
    private LocalDateTime sleepStart;

    @Schema(description = "睡眠结束时间")
    private LocalDateTime sleepEnd;

    @Schema(description = "总睡眠时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Long[] sleepTotalTime;

    @Schema(description = "白天睡眠时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Long[] sleepDayTime;

    @Schema(description = "睡眠数据 [睡眠时间, 清醒时间, 无人时间]")
    private String sleepData;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
