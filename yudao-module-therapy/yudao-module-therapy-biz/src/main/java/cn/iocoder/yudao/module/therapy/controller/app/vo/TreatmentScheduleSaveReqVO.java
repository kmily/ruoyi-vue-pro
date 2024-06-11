package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 患者日程新增/修改 Request VO")
@Data
public class TreatmentScheduleSaveReqVO {
//    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8684")
//    private Long id;
//
//    @Schema(description = "患者id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14131")
//    @NotNull(message = "患者id不能为空")
//    private Long userId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "有多大的信心，例如:89%就传89")
    @Min(value = 1,message = "有多大的信心参数不对")
    @Max(value = 100,message = "有多大的信心不能超过100%")
    private Integer estimateCompletedRate;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;

    @Schema(description = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

//    @Schema(description = "状态")
//    private Integer stage;

//    @Schema(description = "备注", example = "你猜")
//    private String remark;

    @Schema(description = "未完成原因")
    private String unfinishedCause;

    @Schema(description = "克服办法")
    private String solution;

//    @Schema(description = "活动前评分")
//    private Integer beforeScore;
//
//    @Schema(description = "活动后评分")
//    private Integer afterScore;
}
