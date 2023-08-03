package cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 体征数据 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class HealthDataExcelVO {

    @ExcelProperty("自增编号")
    private Long id;

    @ExcelProperty("设备ID")
    private Long deviceId;

    @ExcelProperty("设备编号")
    private String deviceCode;

    @ExcelProperty("上报时间")
    private Long timeStamp;

    @ExcelProperty("消息序号。用于判定数据连续性")
    private Integer seq;

    @ExcelProperty("检测范围内有无人 0-无人，1-有人")
    private Integer hasPeople;

    @ExcelProperty("检测范围内是否有体动，0-无，1-有")
    private Integer hasMove;

    @ExcelProperty("呼吸次数均值")
    private Double breathFreqAverage;

    @ExcelProperty("心跳次数均值")
    private Double heartFreqAverage;

    @ExcelProperty("呼吸幅度均值")
    private Double breathAmpAverage;

    @ExcelProperty("心跳幅度均值")
    private Double heartAmpAverage;

    @ExcelProperty("呼吸频率标准差")
    private Double breathFreqStd;

    @ExcelProperty("心跳频率标准差")
    private Double heartFreqStd;

    @ExcelProperty("呼吸幅度最大值和最小值差")
    private Double breathAmpDiff;

    @ExcelProperty("心跳幅度最大值和最小值差")
    private Double heartAmpDiff;

    @ExcelProperty("1分钟内，心跳间期的平均值，单位 ms")
    private Long meanibi;

    @ExcelProperty("1分钟内，心跳间期差分均方根值")
    private Long rmssd;

    @ExcelProperty("1分钟内，心跳间期的标准差。体现了心跳间期整体的波动情况。单位 ms")
    private Long sdrr;

    @ExcelProperty(" 分钟内，心跳间期差分值超过50ms 的个数，占所有心跳间期个数的比例。体现了连续的心跳间期波动较大的比例")
    private Double pnn50;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
