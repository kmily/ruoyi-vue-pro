package cn.iocoder.yudao.module.therapy.controller.app.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Schema(description = "治疗子任务交互 - 步骤返回数据 Response VO")
@Data
@ToString(callSuper = true)
public class DayitemStepSubmitRespVO {


    @Schema(description = "流程步骤的返回数据")
    @Data
    @ToString(callSuper = true)
    public static class StepRespVO {
        @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "SUCCESS")
        private String status;

        @Schema(description = "信息提示", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "...")
        private String msg;

    }

    @Schema(description = "问卷接口返回的数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Map survey_resp;

    @Schema(description = "流程返回的数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private StepRespVO step_resp;

}
