package cn.iocoder.yudao.module.therapy.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 治疗进度 Response VO")
@Data
public class TreatmentProgressRespVO {

    @Schema(description = "管理后台 - 治疗进度每日进度 Request VO")
    @Data
    public static class DayitemInstanceVO {
        @Schema(description = "流程子任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long dayitem_instance_id;

        @Schema(description = "子任务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "goal_and_plan")
        private Long item_type;

        @Schema(description = "子任务名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "初步评估")
        private String item_name;

        @Schema(description = "子任务状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "COMPLETED")
        private String status;

        @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-23 12:23:34")
        private String create_time;

        @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-23 12:23:34")
        private String update_time;
    }

    @Schema(description = "管理后台 - 治疗进度每日进度 Request VO")
    @Data
    public static class DayInstanceVO{
        @Schema(description = "流程日实例ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long day_instance_id;

        @Schema(description = "是否休息日", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        private boolean is_break;

        @Schema(description = "当天任务完成状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "COMPLETED")
        private String status;

        @Schema(description = "每日任务完成示例数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private List<DayitemInstanceVO> dayitem_instances;  // 数据

        @Schema(description = "流程日序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private int flow_day_index;
    }

    @Schema(description = "治疗流程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long treatment_instance_id;


    @Schema(description = "治疗流程总体百分比", requiredMode = Schema.RequiredMode.REQUIRED, example = "75")
    private Long progress_percentage;


    @Schema
    private List<DayInstanceVO> day_instances;  // 数据

}
