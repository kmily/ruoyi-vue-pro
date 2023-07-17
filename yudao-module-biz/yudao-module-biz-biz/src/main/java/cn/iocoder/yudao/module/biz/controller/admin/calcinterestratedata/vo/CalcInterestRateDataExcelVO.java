package cn.iocoder.yudao.module.biz.controller.admin.calcinterestratedata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 利率数据 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class CalcInterestRateDataExcelVO {

    @ExcelProperty("主键")
    private Integer id;

    @ExcelProperty("开始日期")
    private LocalDate startDate;

    @ExcelProperty("半年期利率")
    private BigDecimal rateHalfYear;

    @ExcelProperty("一年期利率")
    private BigDecimal rateOneYear;

    @ExcelProperty("三年期利率")
    private BigDecimal rateThreeYear;

    @ExcelProperty("五年期利率")
    private BigDecimal rateFiveYear;

    @ExcelProperty("五年以上利率")
    private BigDecimal rateOverFiveYear;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
