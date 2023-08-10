package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 睡眠统计记录 Excel VO
 *
 * @author 芋道源码
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

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
