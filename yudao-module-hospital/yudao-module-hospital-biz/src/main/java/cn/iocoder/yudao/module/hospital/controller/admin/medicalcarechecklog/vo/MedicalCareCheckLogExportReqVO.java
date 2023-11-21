package cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 医护审核记录 Excel 导出 Request VO，参数和 MedicalCareCheckLogPageReqVO 是一致的")
@Data
public class MedicalCareCheckLogExportReqVO {

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
