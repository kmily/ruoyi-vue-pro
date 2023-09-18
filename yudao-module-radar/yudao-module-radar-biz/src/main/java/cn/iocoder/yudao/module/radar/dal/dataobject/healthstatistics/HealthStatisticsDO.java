package cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 睡眠统计记录 DO
 *
 * @author 和尘同光
 */
@TableName("radar_health_statistics")
@KeySequence("radar_health_statistics_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatisticsDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 日期
     */
    private String startTime;
    /**
     * 呼吸最大值
     */
    private Double breathMaxValue;
    /**
     * 呼吸最大值
     */
    private Double breathMinValue;
    /**
     * 呼吸静息
     */
    private Double breathSilent;
    /**
     * 呼吸平均
     */
    private Double breathAverage;
    /**
     * 心率最大值
     */
    private Double heartMaxValue;
    /**
     * 心率最小值
     */
    private Double heartMinValue;
    /**
     * 心率静息
     */
    private Double heartSilent;
    /**
     * 心率平均
     */
    private Double heartAverage;
    /**
     * 开始睡眠时间
     */
    private LocalDateTime sleepStart;
    /**
     * 睡眠结束时间
     */
    private LocalDateTime sleepEnd;
    /**
     * 总睡眠时间
     */
    private Long sleepTotalTime;
    /**
     * 白天睡眠时间
     */
    private Long sleepDayTime;
    /**
     * 睡眠数据 [睡眠时间, 清醒时间, 无人时间]
     */
    private String sleepData;

    /**
     * 日间睡眠开始时间
     */
    private LocalDateTime dayStart;

    /**
     * 日间睡眠结束时间
     */
    private LocalDateTime dayEnd;

}
