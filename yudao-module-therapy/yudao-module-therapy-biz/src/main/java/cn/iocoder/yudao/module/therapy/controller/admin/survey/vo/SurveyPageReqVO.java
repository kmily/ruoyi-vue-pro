package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 治疗问卷列表 Request VO")
@Data
public class SurveyPageReqVO extends PageParam {

    @Schema(description = "创建人", example = "12")
    private String creator;

    /**
     * 调查标题
     */
    @Schema(description = "标题", example = "PHQ9量表")
    private String title;

    /**
     * 标签,逗号分割
     */
    @Schema(description = "标签", example = "心理类")
    private String tag;

    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    @Schema(description = "类型,枚举SurveyType", example = "1")
    private Integer surveyType;



    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
