package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 治疗问卷创建/修改 Request VO")
@Data
public class SurveySaveReqVO {

    @Schema(description = "问卷编号", example = "1024")
    private Long id;

    /**
     * 调查标题
     */
    @Schema(description = "问卷名称", example = "身体治疗",requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    /**
     * 标签,逗号分割
     */
    @Schema(description = "标签", example = "身体治疗")
    private String tags;

    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    @Schema(description = "问卷类型,枚举:SurveyType", example = "身体治疗",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer surveyType;

    /**
     * 问题
     */
    @Schema(description = "题目列表", example = "问题1,2,3",requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SurveyQstSaveReqVO> questions;
}
