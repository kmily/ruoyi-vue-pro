package cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 医护资质审核记录 Excel 导出 Request VO，参数和 CareAptitudeCheckLogPageReqVO 是一致的")
@Data
public class CareAptitudeCheckLogExportReqVO {

    @Schema(description = "医护编号", example = "25097")
    private Long careId;

    @Schema(description = "资质编号", example = "14084")
    private Long aptitudeId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
