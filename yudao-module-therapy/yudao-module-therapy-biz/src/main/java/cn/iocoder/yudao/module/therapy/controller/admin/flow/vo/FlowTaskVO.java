package cn.iocoder.yudao.module.therapy.controller.admin.flow.vo;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 治疗方案子任务 Request VO")
@Data
public class FlowTaskVO {
    @Schema(description = "治疗方案子任务id,创建时不需要赋值,更新时需要", example = "2")
    private Long id;

    @Schema(description = "前一个任务id,如果是第一个可不传或传0", example = "2",defaultValue = "0")
//    @Min(value = 1,message = "前一个任务id不对")
    private Long beforeId;

    @Schema(description = "任务名称", example = "gfg2nbc",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "任务名称不能为空")
    private String name;

    @Schema(description = "治疗计划id", example = "2",requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1,message = "dayId不对")
    private Long dayId;

    @Schema(description = "子任务类型:[引导语,量表引入...]", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = TaskType.class,message = "子任务类型不对")
    private Integer type;

    @Schema(description = "依赖项",  example = "1,2,3")
    private String dependentItemIds;

    @Schema(description = "是否必做",  example = "true",defaultValue = "false")
    private Boolean required;

    @Schema(description = "备注",  example = "随便")
    private String remark;


    @Schema(description = "设置josn格式",  example = "{.....}")
    private String settings;
}
