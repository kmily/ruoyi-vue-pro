package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 利率计算-参数
 */
@Data
public class CalcInterestRateExecParamVO {

    @Schema(description = "计算器类型，1利息，2罚息，2执行费")
    private Integer calcType;
    @Schema(description = "利息金额")
    private BigDecimal lxAmount;
    @Schema(description = "是否考虑闰年，0不考虑，1考虑。默认0")
    private Integer leapYear;
    @Schema(description = "利息开始日期")
    private Date lxStartDate;
    @Schema(description = "利息结束日期")
    private Date lxEndDate;
    @Schema(description = "指定利息-利率")
    private BigDecimal lxFixRate;
    @Schema(description = "利息类型 1约定利率 2中国人民银行同期贷款基准利率与LPR自动分段 3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)")
    private Integer lxRateType;

    /***************************************************************罚息***************************************************************/
    @Schema(description = "罚息开始日期")
    private Date fxStartDate;
    @Schema(description = "罚息结束日期")
    private Date fxEndDate;
    @Schema(description = "罚息利息类型")
    private Integer fxRateType;
    @Schema(description = "罚息指定利息-利率")
    private BigDecimal fxFixRate;

}
