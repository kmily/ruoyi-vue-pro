package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author whycode
 * @title: AppHealthDataResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/817:22
 */

@Schema(description = "用户 APP - 体征数据查询 Response VO")
@Data
@ToString(callSuper = true)
public class AppHealthDataResVO {

    @Schema(description = "呼吸数据", required = true)
    private BreathAndHeartVO breath;

    @Schema(description = "心跳数据", required = true)
    private BreathAndHeartVO heart;

    @Schema(description = "睡眠时间")
    private Long sleep;

}
