package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "数据及报告 - 行为活动计划统计 Response VO")
@Data
@ToString(callSuper = true)
public class ScheduleStateRespVO {

    @Schema(description = "日期,格式yyyy-MM-dd", example = "2024-06-12")
    private String day;
    @Schema(description = "活动量", example = "23")
    private Integer num;
    @Schema(description = "情绪水平", example = "67")
    private Integer score;

}
