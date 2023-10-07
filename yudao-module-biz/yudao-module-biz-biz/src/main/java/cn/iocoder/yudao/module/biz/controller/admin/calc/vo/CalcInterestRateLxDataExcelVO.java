package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 */
@Data
public class CalcInterestRateLxDataExcelVO {

    @ExcelProperty("时段")
    private String timeFrame;

    @ExcelProperty("天数")
    private String days;

    @ExcelProperty("基准利率%")
    private String benchmarkRate;

    @ExcelProperty("金额")
    private BigDecimal amount;

}
