package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 治疗问卷创建/修改 Request VO")
@Data
public class SurveySaveReqVO {

    @Schema(description = "问卷编号", example = "1024")
    private Long id;

    @Schema(description = "问卷编码", example = "1024")
    private String code;

    /**
     * 调查标题
     */
    @Schema(description = "问卷名称", example = "身体治疗",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "问卷名称不能为空")
    private String title;

    /**
     * 问题标题
     */
    @Schema(description = "问卷描述", example = "这张问卷主要来了解你最近的心情")
    private String description;

    /**
     * 标签,逗号分割
     */
    @Schema(description = "标签", example = "身体治疗")
    private List<String> tags;

    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    @Schema(description = "问卷类型,枚举:SurveyType", example = "身体治疗",requiredMode = Schema.RequiredMode.REQUIRED)
    @InEnum(value = SurveyType.class, message = "问卷类型不正确")
    private Integer surveyType;


    /**
     * 问题
     */
    @Schema(description = "题目列表", example = "问题1,2,3")
    private List<String> questions;
}
