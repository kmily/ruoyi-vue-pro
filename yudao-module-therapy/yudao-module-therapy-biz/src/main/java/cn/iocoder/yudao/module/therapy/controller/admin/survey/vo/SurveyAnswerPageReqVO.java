package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 答题业务 req VO")
@Data
public class SurveyAnswerPageReqVO extends PageParam {
    /**
     * 患者ID
     */
    @Schema(description = "患者ID",  example = "1")
    private Long userId;

    @Schema(description = "问卷类型",  example = "1")
    private Integer surveyType;
}
