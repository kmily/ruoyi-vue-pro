package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: AppHealthDataLogItemVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1514:07
 */
@Schema(description = "用户 APP - 睡眠数据 Request VO")
@Data
@ToString(callSuper = true)
public class AppHealthDataLogItemVO {

    @Schema(description = "上报时间")
    private LocalDateTime time;

    @Schema(description = "是否有人0无人，1有人")
    private Integer hasPeople;

    @Schema(description = "呼吸数据", required = true)
    private Long breath;

    @Schema(description = "心跳数据", required = true)
    private Long heart;

}
