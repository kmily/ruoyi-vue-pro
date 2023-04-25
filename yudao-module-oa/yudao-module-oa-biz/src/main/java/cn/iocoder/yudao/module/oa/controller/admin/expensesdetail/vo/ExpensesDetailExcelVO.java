package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 报销明细 Excel VO
 *
 * @author 管理员
 */
@Data
public class ExpensesDetailExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty(value = "明细类型", converter = DictConvert.class)
    @DictFormat("oa_expense_travel_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String detailType;

    @ExcelProperty("消费时间")
    private LocalDateTime consumeTime;

    @ExcelProperty("报销费用")
    private BigDecimal detailFee;

    @ExcelProperty("明细备注")
    private String detailRemark;

    @ExcelProperty("报销申请id")
    private Long expensesId;

}
