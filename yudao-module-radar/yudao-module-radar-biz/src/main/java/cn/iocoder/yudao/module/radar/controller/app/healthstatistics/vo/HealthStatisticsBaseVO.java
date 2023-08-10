package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 睡眠统计记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class HealthStatisticsBaseVO {

    @Schema(description = "设备ID", required = true, example = "5957")
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

}
