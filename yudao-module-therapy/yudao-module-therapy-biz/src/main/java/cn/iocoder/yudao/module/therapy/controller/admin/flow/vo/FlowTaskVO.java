package cn.iocoder.yudao.module.therapy.controller.admin.flow.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 治疗方案子任务 Request VO")
@Data
public class FlowTaskVO {
    @Schema(description = "治疗方案子任务id", example = "2")
    private Long id;

    @Schema(description = "治疗方案子任务code", example = "gfg2nbc",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "治疗方案子任务code不能为空")
    private String code;

    @Schema(description = "子任务类型:[引导语,量表引入...]", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = SurveyType.class,message = "子任务类型不对")
    private Integer type;

    @Schema(description = "依赖项code",  example = "1,af,f32r")
    @NotBlank
    private String dependentItemIds;

    @Schema(description = "是否必做",  example = "true",defaultValue = "false")
    private Boolean required;

    @Schema(description = "备注",  example = "随便")
    private String remark;

    @Schema(description = "顺序组",  example = "2",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer agroup;
}
