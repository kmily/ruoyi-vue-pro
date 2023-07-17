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
import javax.validation.constraints.*;

/**
 * 利率数据 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CalcInterestRateDataBaseVO {

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
