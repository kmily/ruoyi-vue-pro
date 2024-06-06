package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Schema(description = "治疗子任务交互 - 提交数据 Request VO")
@Data
@ToString(callSuper = true)
public class DayitemStepSubmitReqVO {


    @Schema(description = "治疗子任务交互 - 提交的步骤数据 Request VO")
    @Data
    @ToString(callSuper = true)
    public static class StepDataVO {
        @Schema(description = "问卷数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private SubmitSurveyReqVO survey;

        @Schema(description = "当前步骤数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Map<String, Object> current;

        @Schema(description = "永久保存数据,贯穿整个子任务", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Map<String, Object> store;
    }

    @Schema(description = "步骤 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23")
    private String step_id;

    @Schema(description = "步骤数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "...")
    private StepDataVO step_data;
}
