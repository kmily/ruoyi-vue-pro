package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 睡眠统计记录 Excel VO
 *
 * @author 和尘同光
 */
@Data
public class HealthStatisticsExcelVO {

    @ExcelProperty("自增编号")
    private Long id;

    @ExcelProperty("设备ID")
    private Long deviceId;

    @ExcelProperty("日期")
    private String startTime;

    @ExcelProperty("呼吸最大值")
    private Double breathMaxValue;

    @ExcelProperty("呼吸最大值")
    private Double breathMinValue;

    @ExcelProperty("呼吸静息")
    private Double breathSilent;

    @ExcelProperty("呼吸平均")
    private Double breathAverage;

    @ExcelProperty("心率最大值")
    private Double heartMaxValue;

    @ExcelProperty("心率最小值")
    private Double heartMinValue;

    @ExcelProperty("心率静息")
    private Double heartSilent;

    @ExcelProperty("心率平均")
    private Double heartAverage;

    @ExcelProperty("开始睡眠时间")
    private LocalDateTime sleepStart;

    @ExcelProperty("睡眠结束时间")
    private LocalDateTime sleepEnd;

    @ExcelProperty("总睡眠时间")
    private Long sleepTotalTime;

    @ExcelProperty("白天睡眠时间")
    private Long sleepDayTime;

    @ExcelProperty("睡眠数据 [睡眠时间, 清醒时间, 无人时间]")
    private String sleepData;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
