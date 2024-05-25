package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 治疗问卷题目创建/修改 Request VO")
@Data
public class SurveyQstSaveReqVO {

    /**
     * 问题标题
     */
    @Schema(description = "题目", example = "你喜欢什么水果",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "题目不能为空")
    private String title;


    /**
     * 是否必答题
     */
    @Schema(description = "是否必答题", example = "true",requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean required;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    @Schema(description = "题目类型,枚举:SurveyQuestionType", example = "单选题",requiredMode = Schema.RequiredMode.REQUIRED)
    @InEnum(value = SurveyQuestionType.class, message = "题目类型不正确")
    private Integer qstType;

    /**
     * 题干json化数据
     */
    @Schema(description = "题干,json化字符串", example = "苹果,香蕉...",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "题干不能为空")
    @JsonDeserialize
    private JSONArray qstContext;
}
