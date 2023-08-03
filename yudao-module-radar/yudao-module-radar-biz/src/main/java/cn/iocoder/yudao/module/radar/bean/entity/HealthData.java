package cn.iocoder.yudao.module.radar.bean.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * @author whycode
 * @title: HealthData
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/215:22
 */

@Data
@ToString
public class HealthData {


    /**
     * 检测范围内有无人 0-无人，1-有人
     */
    @JSONField(name = "HasPeople")
    private Integer hasPeople;
    /**
     * 检测范围内是否有体动，0-无，1-有
     */
    @JSONField(name = "HasMove")
    private Integer hasMove;
    /**
     * 呼吸次数均值
     */
    @JSONField(name = "BreathFreqAverage")
    private Double breathFreqAverage;
    /**
     * 心跳次数均值
     */
    @JSONField(name = "HeartFreqAverage")
    private Double heartFreqAverage;
    /**
     * 呼吸幅度均值
     */
    @JSONField(name = "BreathAmpAverage")
    private Double breathAmpAverage;
    /**
     * 心跳幅度均值
     */
    @JSONField(name = "HeartAmpAverage")
    private Double heartAmpAverage;
    /**
     * 呼吸频率标准差
     */
    @JSONField(name = "BreathFreqStd")
    private Double breathFreqStd;
    /**
     * 心跳频率标准差
     */
    @JSONField(name = "HeartFreqStd")
    private Double heartFreqStd;
    /**
     * 呼吸幅度最大值和最小值差
     */
    @JSONField(name = "BreathAmpDiff")
    private Double breathAmpDiff;
    /**
     * 心跳幅度最大值和最小值差
     */
    @JSONField(name = "HeartAmpDiff")
    private Double heartAmpDiff;
    /**
     * 1分钟内，心跳间期的平均值，单位 ms
     */
    @JSONField(name = "meanIBI")
    private Long meanibi;
    /**
     * 1分钟内，心跳间期差分均方根值
     */
    @JSONField(name = "RMSSD")
    private Long rmssd;
    /**
     * 1分钟内，心跳间期的标准差。体现了心跳间期整体的波动情况。单位 ms
     */
    @JSONField(name = "SDRR")
    private Long sdrr;
    /**
     *  分钟内，心跳间期差分值超过50ms 的个数，占所有心跳间期个数的比例。体现了连续的心跳间期波动较大的比例
     */
    @JSONField(name = "pNN50")
    private Double pnn50;

}
