package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 治疗问卷列表 Response VO")
@Data
public class SurveyRespVO {
    /**
     * 调查ID
     */
    @Schema(description = "问卷id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    /**
     * 调查标题
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "PHQ9量表")
    private String title;

    /**
     * 标签,逗号分割
     */
    @Schema(description = "标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "心理治疗")
    private String tags;

    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    @Schema(description = "类型,枚举:SurveyType", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer surveyType;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-24 00:00:00")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-24 00:00:00")
    private LocalDateTime updateTime;
    /**
     * 创建者
     */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String creator;
}
