package cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 医护审核记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalCareCheckLogPageReqVO extends PageParam {

    @Schema(description = "医护编号", example = "27157")
    private Long careId;

    @Schema(description = "审核人", example = "赵六")
    private String checkName;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkTime;

    @Schema(description = "审核意见")
    private String opinion;

    @Schema(description = "审核状态", example = "2")
    private String checkStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
