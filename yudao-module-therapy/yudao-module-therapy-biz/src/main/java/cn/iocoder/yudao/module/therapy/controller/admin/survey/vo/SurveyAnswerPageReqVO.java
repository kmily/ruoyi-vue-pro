package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 答题业务 req VO")
@Data
public class SurveyAnswerPageReqVO extends PageParam {
    /**
     * 患者ID
     */
    @Schema(description = "患者ID",  example = "1")
    @NotNull
    @Min(0)
    private Long userId;

    @Schema(description = "问卷类型",  example = "1")
    @NotNull
    @InEnum(SurveyType.class )
    private Integer surveyType;
}
