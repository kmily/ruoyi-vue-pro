package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: AppNightSleepVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/110:12
 */

@Data
public class AppNightSleepVO {

    @Schema(description = "时间")
    private LocalDateTime time;

    /**
     * 呼吸次数均值
     */
    private Double breathFreqAverage;
    /**
     * 心跳次数均值
     */
    private Double heartFreqAverage;

    /**
     * 当前状态 0-无人，1-醒着，2-睡眠
     */
    private Integer status;

    /**
     * 睡眠类型  0-夜间，1-日间
     */
    private Integer type;
}
