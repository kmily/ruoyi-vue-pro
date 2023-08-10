package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author whycode
 * @title: HeartVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/817:23
 */

@Schema(description = "用户 APP - 体征数据查询 Response VO")
@Data
@ToString(callSuper = true)
public class BreathAndHeartVO {

    @Schema(description = "当前值")
    private Double current;

    @Schema(description = "平均值")
    private Double average;

    @Schema(description = "最高值")
    private Double highest;

    @Schema(description = "最低值")
    private Double lowest;

    @Schema(description = "数据")
    private List<BreathAndHeartDataVO> timeList;

}
