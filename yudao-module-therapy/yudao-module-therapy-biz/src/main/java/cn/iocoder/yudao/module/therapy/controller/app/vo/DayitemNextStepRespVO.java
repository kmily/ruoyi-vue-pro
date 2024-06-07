package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Schema(description = "治疗子任务交互 - 下一步信息 Response VO")
@Data
@ToString(callSuper = true)
public class DayitemNextStepRespVO {

    @Schema(description = "下一步类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "primary_troubles_qst")
    private String step_type;

    @Schema(description = "步骤ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23")
    private String step_id;

    @Schema(description = "步骤名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "...")
    private String step_name;

    @Schema(description = "步骤数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "...")
    private Map<String, Object> step_data;

    @Schema(description = "步骤状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "COMPLETED")
    private String step_status;

}
