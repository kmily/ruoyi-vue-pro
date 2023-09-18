package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whycode
 * @title: AppNightSleepVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/110:12
 */

@Schema(description = "用户 APP - 睡眠数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppNightSleepVO {

    @Schema(description = "睡眠总时间 (单位分钟)")
    private Long  totalTime;

    @Schema(description = "日间睡眠时间 (单位分钟)")
    private Long  dayTime;

    @Schema(description = "夜晚睡眠开始时间")
    private LocalDateTime start;

    @Schema(description = "夜晚睡眠结束时间")
    private LocalDateTime end;

    @Schema(description = "日间睡眠开始时间")
    private LocalDateTime dayStart;

    @Schema(description = "日间睡眠结束时间")
    private LocalDateTime dayEnd;


    @Schema(description = "睡眠详情")
    private List<AppSleepOrWeekVO> sleepOrWeekVOS;


}
