package cn.iocoder.yudao.module.radar.dal.dataobject.healthdata;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 体征数据 DO
 *
 * @author 芋道源码
 */
@TableName("radar_health_data")
@KeySequence("radar_health_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthDataDO extends BaseDO {

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
     * 设备编号
     */
    private String deviceCode;
    /**
     * 上报时间
     */
    private Long timeStamp;
    /**
     * 消息序号。用于判定数据连续性
     */
    private Integer seq;
    /**
     * 检测范围内有无人 0-无人，1-有人
     */
    private Integer hasPeople;
    /**
     * 检测范围内是否有体动，0-无，1-有
     */
    private Integer hasMove;
    /**
     * 呼吸次数均值
     */
    private Double breathFreqAverage;
    /**
     * 心跳次数均值
     */
    private Double heartFreqAverage;
    /**
     * 呼吸幅度均值
     */
    private Double breathAmpAverage;
    /**
     * 心跳幅度均值
     */
    private Double heartAmpAverage;
    /**
     * 呼吸频率标准差
     */
    private Double breathFreqStd;
    /**
     * 心跳频率标准差
     */
    private Double heartFreqStd;
    /**
     * 呼吸幅度最大值和最小值差
     */
    private Double breathAmpDiff;
    /**
     * 心跳幅度最大值和最小值差
     */
    private Double heartAmpDiff;
    /**
     * 1分钟内，心跳间期的平均值，单位 ms
     */
    private Long meanibi;
    /**
     * 1分钟内，心跳间期差分均方根值
     */
    private Long rmssd;
    /**
     * 1分钟内，心跳间期的标准差。体现了心跳间期整体的波动情况。单位 ms
     */
    private Long sdrr;
    /**
     *  分钟内，心跳间期差分值超过50ms 的个数，占所有心跳间期个数的比例。体现了连续的心跳间期波动较大的比例
     */
    private Double pnn50;

}
