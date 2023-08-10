package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 睡眠统计记录 Excel 导出 Request VO，参数和 HealthStatisticsPageReqVO 是一致的")
@Data
public class HealthStatisticsExportReqVO {

    @Schema(description = "设备ID", example = "5957")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
