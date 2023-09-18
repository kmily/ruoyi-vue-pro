package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: AppSleepOrWeekVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1214:19
 */

@Data
public class AppSleepOrWeekVO {

    /**
     * 开始时间
     */
    private LocalDateTime start;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 当前状态 0-无人，1-醒着，2-睡眠
     */
    private Integer status;

    /**
     * 睡眠类型  0-夜间，1-日间
     */
    private Integer type;



}
