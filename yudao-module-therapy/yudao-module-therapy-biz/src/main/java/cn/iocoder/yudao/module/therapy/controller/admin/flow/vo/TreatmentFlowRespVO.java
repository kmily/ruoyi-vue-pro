package cn.iocoder.yudao.module.therapy.controller.admin.flow.vo;

import cn.iocoder.boot.module.therapy.enums.FlowType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TreatmentFlowRespVO {
    @Schema(description = "治疗流程id", example = "7888")
    private Long id;

    @Schema(description = "治疗流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "治疗流程名称不能为空")
    private String name;

    @Schema(description = "方案状态（1未发布 0已发布）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "方案类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = FlowType.class,message = "方案类型不对")
    private Integer type;

    @Schema(description = "创建人", example = "创建人")
    private String creatorName;

    @Schema(description = "治疗日列表", example = "list")
    private List<FlowPlanReqVO> planList;
}
