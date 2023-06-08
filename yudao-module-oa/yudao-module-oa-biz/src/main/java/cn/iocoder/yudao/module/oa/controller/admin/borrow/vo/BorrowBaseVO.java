package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* 借支申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BorrowBaseVO {

    @Schema(description = "说明", example = "不喜欢")
    private String borrowReason;

    @Schema(description = "借支总费用", required = true)
    @NotNull(message = "借支总费用不能为空")
    private BigDecimal borrowFee;

    @Schema(description = "已还款费用")
    private BigDecimal repaymentFee;

    @Schema(description = "申请单状态", required = true, example = "2")
    @NotNull(message = "申请单状态不能为空")
    private int status;

    @Schema(description = "审批状态", example = "1")
    private int approvalStatus;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "更新者")
    private String updater;

}
