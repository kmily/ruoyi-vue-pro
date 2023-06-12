package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 借支申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BorrowPageReqVO extends PageParam {

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
