package cn.iocoder.yudao.module.therapy.controller.VO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "治疗实例 VO")
@Data
@ToString(callSuper = true)
public class TreatmentInstanceVO {
        @Schema(description = "治疗流程 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long id;
}
