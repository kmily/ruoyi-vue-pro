package cn.iocoder.yudao.module.oa.controller.admin.expenses.vo;

import cn.iocoder.yudao.module.oa.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 报销申请 Excel VO
 *
 * @author 管理员
 */
@Data
public class ExpensesExcelVO {

    @ExcelProperty("报销id")
    private Long id;

    @ExcelProperty(value = "报销类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.OA_EXPENSE_TYPE)
    private String expensesType;

    @ExcelProperty("展会名称")
    private String exhibitName;

    @ExcelProperty("展会开始时间")
    private LocalDateTime exhibitBeginDate;

    @ExcelProperty("展会结束时间")
    private LocalDateTime exhibitEndDate;

    @ExcelProperty("展会地点")
    private String exhibitAddress;

    @ExcelProperty("关联的拜访过的客户")
    private Long customerId;

    @ExcelProperty("费用说明")
    private String feeRemark;

    @ExcelProperty("报销总费用")
    private BigDecimal fee;

    @ExcelProperty("申请单状态")
    private Boolean status;

    @ExcelProperty("审批状态")
    private Boolean approvalStatus;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建者")
    private String createBy;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updateBy;

}
