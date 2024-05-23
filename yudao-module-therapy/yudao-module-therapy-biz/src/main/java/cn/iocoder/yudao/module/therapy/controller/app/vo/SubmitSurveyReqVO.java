package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "C端 - 治疗问卷提交 Request VO")
@Data
public class SubmitSurveyReqVO {

    @Schema(description = "问卷ID",requiredMode = Schema.RequiredMode.REQUIRED,example = "23")
    @Min(value = 1,message = "问卷参数不对")
    private Long surveyId;

    @Schema(description = "问卷答案",requiredMode = Schema.RequiredMode.REQUIRED,example = "[{},{}.....]")
    @NotEmpty(message = "答案列表为不能为空")
    private List<AnAnswerReqVO> qstList;
}
