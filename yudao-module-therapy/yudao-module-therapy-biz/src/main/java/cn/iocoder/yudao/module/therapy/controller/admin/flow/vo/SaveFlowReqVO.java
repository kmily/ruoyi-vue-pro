package cn.iocoder.yudao.module.therapy.controller.admin.flow.vo;

import cn.iocoder.boot.module.therapy.enums.FlowType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SaveFlowReqVO {
    @Schema(description = "治疗流程id", example = "7888")
    private Long id;

    @Schema(description = "治疗流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "治疗流程名称不能为空")
    private String name;

//    @Schema(description = "流程状态（0未发布 1已发布）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//    @NotNull(message = "流程状态（0未发布 1已发布）不能为空")
//    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "方案类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = FlowType.class,message = "方案类型不对")
    private Integer type;

//    @Schema(description = "创建人", example = "创建人")
//    private String creatorName;
}
