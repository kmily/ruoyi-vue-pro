package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 睡眠统计记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class HealthStatisticsBaseVO {

    @Schema(description = "设备ID", required = true, example = "9583")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "日期")
    private String startTime;

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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime sleepStart;

    @Schema(description = "睡眠结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime sleepEnd;

    @Schema(description = "总睡眠时间")
    private Long sleepTotalTime;

    @Schema(description = "白天睡眠时间")
    private Long sleepDayTime;

    @Schema(description = "日间睡眠开始时间")
    private LocalDateTime dayStart;

    @Schema(description = "日间睡眠结束时间")
    private LocalDateTime dayEnd;

    @Schema(description = "睡眠数据 [睡眠时间, 清醒时间, 无人时间]")
    private String sleepData;

}
