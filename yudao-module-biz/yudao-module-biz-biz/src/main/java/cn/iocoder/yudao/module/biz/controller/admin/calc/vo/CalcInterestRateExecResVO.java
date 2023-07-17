package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 利率计算-返回值
 */
@Data
public class CalcInterestRateExecResVO {

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "半年期利率")
    private BigDecimal rateHalfYear;

    @Schema(description = "一年期利率")
    private BigDecimal rateOneYear;

    @Schema(description = "三年期利率")
    private BigDecimal rateThreeYear;

    @Schema(description = "五年期利率")
    private BigDecimal rateFiveYear;

    @Schema(description = "五年以上利率")
    private BigDecimal rateOverFiveYear;

}
