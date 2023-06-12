package cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商机 Excel 导出 Request VO，参数和 OpportunityPageReqVO 是一致的")
@Data
public class OpportunityExportReqVO {

    @Schema(description = "商机标题")
    private String businessTitle;

    @Schema(description = "商机详情")
    private String detail;

    @Schema(description = "上报时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] reportTime;

    @Schema(description = "跟进用户id", example = "31058")
    private Long followUserId;

    @Schema(description = "商机状态", example = "1")
    private Byte status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
