package cn.iocoder.yudao.module.biz.controller.admin.calcinterestratedata.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 利率数据分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CalcInterestRateDataPageReqVO extends PageParam {

    @Schema(description = "开始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] startDate;

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
