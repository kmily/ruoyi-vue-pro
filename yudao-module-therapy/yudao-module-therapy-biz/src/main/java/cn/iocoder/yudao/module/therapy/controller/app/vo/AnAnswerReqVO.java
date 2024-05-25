package cn.iocoder.yudao.module.therapy.controller.app.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "C端 - 治疗问卷答案 Request VO")
@Data
public class AnAnswerReqVO {
    /**
     * 题目ID
     */
    @Schema(description = "题目ID",requiredMode = Schema.RequiredMode.REQUIRED,example = "23")
    @Min(value = 1,message = "题目参数不对")
    private Long id;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    @Schema(description = "问题类型",requiredMode = Schema.RequiredMode.REQUIRED,example = "23")
    @InEnum(value = SurveyQuestionType.class,message = "问题类型不对")
    private Integer qstType;

    /**
     * 答案
     */
    @Schema(description = "答案,json格式",requiredMode = Schema.RequiredMode.REQUIRED,example = "{23}")
    @NotNull(groups = JSONArray.class,message = "答案不能为空")
    private JSONArray answer;
}
