package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Schema(description = "患者日程签到 Request VO")
@Data
public class SignInReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8684")
    @Min(value = 1,message = "日程id不对")
    private Long id;

    /**
     * 活动前评分
     */
    @Schema(description = "活动前评分，传0~100", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
    @Min(value = 0,message = "活动前评分不对")
    @Max(value = 100,message = "活动前评分不对")
    private Integer beforeScore;
    /**
     * 活动后评分
     */
    @Schema(description = "活动后评分，传0~100", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
    @Min(value = 0,message = "活动后评分不对")
    @Max(value = 100,message = "活动后评分不对")
    private Integer afterScore;

    /**
     * 备注
     */
    @Schema(description = "签到备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
    private String remark;

}
