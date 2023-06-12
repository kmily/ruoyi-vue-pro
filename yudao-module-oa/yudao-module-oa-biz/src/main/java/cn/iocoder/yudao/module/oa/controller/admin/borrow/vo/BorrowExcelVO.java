package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 借支申请 Excel VO
 *
 * @author 东海
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
    private Byte status;

    @ExcelProperty("审批状态")
    private Byte approvalStatus;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
