package cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 医护资质 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AppCareAptitudeBaseVO {

    @Schema(description = "资质编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12965")
    @NotNull(message = "资质编号不能为空")
    private Long aptitudeId;

    @Schema(description = "资质名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String aptitudeName;

    @Schema(description = "医护编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2903")
    @NotNull(message = "医护编号不能为空")
    private Long careId;

    @Schema(description = "证书正面")
    private String imageFront;

    @Schema(description = "证书反面")
    private String imageBack;

    @Schema(description = "最终审核人", example = "张三")
    private String checkName;

    @Schema(description = "最终审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime checkTime;

    @Schema(description = "最终审核状态", example = "1")
    private String checkStatus;

    @Schema(description = "审核意见")
    private String opinion;

}
