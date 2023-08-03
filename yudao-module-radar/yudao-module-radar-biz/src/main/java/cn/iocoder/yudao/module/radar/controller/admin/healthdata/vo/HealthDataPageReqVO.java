package cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 体征数据分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthDataPageReqVO extends PageParam {

    @Schema(description = "设备ID", example = "6351")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "上报时间")
    private Long timeStamp;

    @Schema(description = "消息序号。用于判定数据连续性")
    private Integer seq;

    @Schema(description = "检测范围内有无人 0-无人，1-有人")
    private Integer hasPeople;

    @Schema(description = "检测范围内是否有体动，0-无，1-有")
    private Integer hasMove;

    @Schema(description = "呼吸次数均值")
    private Double breathFreqAverage;

    @Schema(description = "心跳次数均值")
    private Double heartFreqAverage;

    @Schema(description = "呼吸幅度均值")
    private Double breathAmpAverage;

    @Schema(description = "心跳幅度均值")
    private Double heartAmpAverage;

    @Schema(description = "呼吸频率标准差")
    private Double breathFreqStd;

    @Schema(description = "心跳频率标准差")
    private Double heartFreqStd;

    @Schema(description = "呼吸幅度最大值和最小值差")
    private Double breathAmpDiff;

    @Schema(description = "心跳幅度最大值和最小值差")
    private Double heartAmpDiff;

    @Schema(description = "1分钟内，心跳间期的平均值，单位 ms")
    private Long meanibi;

    @Schema(description = "1分钟内，心跳间期差分均方根值")
    private Long rmssd;

    @Schema(description = "1分钟内，心跳间期的标准差。体现了心跳间期整体的波动情况。单位 ms")
    private Long sdrr;

    @Schema(description = " 分钟内，心跳间期差分值超过50ms 的个数，占所有心跳间期个数的比例。体现了连续的心跳间期波动较大的比例")
    private Double pnn50;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
