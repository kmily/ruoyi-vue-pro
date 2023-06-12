package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商机-跟进日志 Excel 导出 Request VO，参数和 OpportunityFollowLogPageReqVO 是一致的")
@Data
public class OpportunityFollowLogExportReqVO {

    @Schema(description = "商机id", example = "22904")
    private Long businessId;

    @Schema(description = "跟进日志内容")
    private String logContent;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
