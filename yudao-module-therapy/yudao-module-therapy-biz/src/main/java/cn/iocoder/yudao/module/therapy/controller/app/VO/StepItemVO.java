package cn.iocoder.yudao.module.therapy.controller.app.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Map;


@Schema(description = "治疗步骤子任务O")
@Data
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StepItemVO {
    @Schema(description = "子任务实例ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED , example = "1")
    private Long id;

    @Schema(description = "子任务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "guide")
    private String item_type;

    @Schema(description = "是否必须完成", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean required;

    @Schema(description = "子任务的设置数据" , requiredMode = Schema.RequiredMode.REQUIRED)
    private Map settings;
}
