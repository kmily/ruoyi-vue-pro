package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 借支申请 Excel VO
 *
 * @author 管理员
 */
@Data
public class BorrowExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("说明")
    private String borrowReason;

    @ExcelProperty("借支总费用")
    private BigDecimal borrowFee;

    @ExcelProperty("已还款费用")
    private BigDecimal repaymentFee;

    @ExcelProperty("申请单状态")
    private int status;

    @ExcelProperty("审批状态")
    private int approvalStatus;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建者")
    private String creator;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updater;

}
