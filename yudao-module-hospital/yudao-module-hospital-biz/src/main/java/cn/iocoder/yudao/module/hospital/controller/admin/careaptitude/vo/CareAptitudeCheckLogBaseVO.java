package cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo;

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
 * 医护资质审核记录 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CareAptitudeCheckLogBaseVO {

    @Schema(description = "医护编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25097")
    @NotNull(message = "医护编号不能为空")
    private Long careId;

    @Schema(description = "资质编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14084")
    @NotNull(message = "资质编号不能为空")
    private Long aptitudeId;

    @Schema(description = "审核人", example = "王五")
    private String checkName;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime checkTime;

    @Schema(description = "审核意见")
    private String opinion;

    @Schema(description = "审核状态", example = "2")
    private String checkStatus;

}
