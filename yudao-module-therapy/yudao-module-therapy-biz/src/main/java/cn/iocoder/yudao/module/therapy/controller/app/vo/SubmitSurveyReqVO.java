package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "C端 - 治疗问卷提交 Request VO")
@Data
public class SubmitSurveyReqVO {

    @Schema(description = "一次回答问卷的ID,第一次可以传0,但是surveyType必须传值",example = "23")
//    @Min(value = 1,message = "一次提交的id")
    private Long id;

//    @Schema(description = "问卷题目Code,code和id二选一",example = "23")
////    @NotBlank(message = "问卷题目Code不正确")
//    private String surveyCode;

//    @Schema(description = "问卷ID,code和id二选一",example = "23")
////    @Min(value = 1,message = "问卷ID不正确")
    private Long surveyId;
    @Schema(description = "问卷类型,当id为0时此参数必须赋值,",example = "2")
//    @Min(value = 1,message = "问卷ID不正确")
    private Integer surveyType;

//    @Schema(description = "来源",requiredMode = Schema.RequiredMode.REQUIRED,example = "23")
//    @Min(value = 1,message = "来源参数不对")
    private Integer source;

    @Schema(description = "问卷答案",requiredMode = Schema.RequiredMode.REQUIRED,example = "[{},{}.....]")
    @NotEmpty(message = "答案列表为不能为空")
    private List<AnAnswerReqVO> qstList;
}
