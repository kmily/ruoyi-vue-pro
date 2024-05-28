package cn.iocoder.yudao.module.therapy.controller.admin.flow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

@Schema(description = "管理后台 - 治疗计划(治疗日) Request VO")
@Data
public class FlowPlanReqVO {

    @Schema(description = "治疗计划id", example = "7888")
    private Long id;

    @Schema(description = "治疗方案id", example = "7888",requiredMode=Schema.RequiredMode.REQUIRED)
    @Min(value = 1,message = "治疗方案id不对")
    private Long flowId;

    @Schema(description = "治疗方案属于第几天", example = "2",requiredMode=Schema.RequiredMode.REQUIRED)
    private Integer sequence;

    @Schema(description = "治疗日名称", example = "第3天治疗计划")
    private String name;

    @Schema(description = "备注", example = "随便写")
    private String remark;

    @Schema(description = "是否休息日", example = "true",requiredMode=Schema.RequiredMode.REQUIRED)
    private boolean hasBreak;

//    @Schema(description = "任务列表", example = "list",requiredMode=Schema.RequiredMode.REQUIRED)
//    @NotEmpty(message = "任务列表不能为空")
//    private List<FlowTaskVO> taskList;
}
