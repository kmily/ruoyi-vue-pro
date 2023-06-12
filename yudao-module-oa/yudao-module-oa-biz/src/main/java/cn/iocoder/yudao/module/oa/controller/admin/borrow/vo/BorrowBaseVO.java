package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 借支申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BorrowBaseVO {

    @Schema(description = "说明", example = "不对")
    private String borrowReason;

    @Schema(description = "借支总费用", required = true)
    @NotNull(message = "借支总费用不能为空")
    private BigDecimal borrowFee;

    @Schema(description = "已还款费用")
    private BigDecimal repaymentFee;

    @Schema(description = "申请单状态", required = true, example = "2")
    @NotNull(message = "申请单状态不能为空")
    private Byte status;

    @Schema(description = "审批状态", example = "2")
    private Byte approvalStatus;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
