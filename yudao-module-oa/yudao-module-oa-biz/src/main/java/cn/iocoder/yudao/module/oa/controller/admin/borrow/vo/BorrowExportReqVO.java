package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 借支申请 Excel 导出 Request VO，参数和 BorrowPageReqVO 是一致的")
@Data
public class BorrowExportReqVO {

    @Schema(description = "说明", example = "不对")
    private String borrowReason;

    @Schema(description = "借支总费用")
    private BigDecimal borrowFee;

    @Schema(description = "已还款费用")
    private BigDecimal repaymentFee;

    @Schema(description = "申请单状态", example = "2")
    private Byte status;

    @Schema(description = "审批状态", example = "2")
    private Byte approvalStatus;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
