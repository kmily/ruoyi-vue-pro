package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 答题业务 Response VO")
@Data
public class SurveyAnswerRespVO {
    /**
     * 一次答题ID
     */
    @Schema(description = "一次答题ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    /**
     * 问卷id
     */
    @Schema(description = "问卷id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long surveyId;

    /**
     * 调查标题
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "PHQ9量表")
    private String title;


    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    @Schema(description = "问卷类型,枚举:SurveyType", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer surveyType;

    /**
     * 答题时间
     */
    @Schema(description = "答题时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-24 00:00:00")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-24 00:00:00")
    private LocalDateTime updateTime;
//    /**
//     * 答题人
//     */
//    @Schema(description = "答题人", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
//    private String creator;

    /**
     * 答题来源
     */
    private Integer source;
}
