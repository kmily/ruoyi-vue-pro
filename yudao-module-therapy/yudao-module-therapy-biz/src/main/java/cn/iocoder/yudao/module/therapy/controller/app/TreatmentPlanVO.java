package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class TreatmentPlanVO {
    @Schema(description = "组")
    @Data
    public  static class GroupData{
        @Schema(description = "index", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private int index;

        @Schema
        private List<TreatmentProgressRespVO.DayInstanceVO> day_instances;  // 数据

        public void addDayInstance(TreatmentProgressRespVO.DayInstanceVO dayInstanceVO){
            day_instances.add(dayInstanceVO);
        }
    }

    @Schema(description = "治疗流程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long treatment_instance_id;


    @Schema(description = "治疗流程总体百分比", requiredMode = Schema.RequiredMode.REQUIRED, example = "75")
    private int progress_percentage;

    @Schema(description = "组",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private List<GroupData> groups;
}
