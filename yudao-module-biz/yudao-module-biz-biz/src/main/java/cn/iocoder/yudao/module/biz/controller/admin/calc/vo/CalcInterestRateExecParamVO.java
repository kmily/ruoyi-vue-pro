package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 利率计算-入参
 */
@Data
public class CalcInterestRateExecParamVO {

    /**************************************************************公共参数*************************************************************/
    @Schema(description = "利息金额")
    private BigDecimal leftAmount;
    @Schema(description = "是否考虑闰年，0不考虑，1考虑。默认0")
    private Integer leapYear;
    @Schema(description = "执行序号")
    private String processId;
    /***************************************************************利息***************************************************************/
    @Schema(description = "利息开始日期")
    private Date lxStartDate;
    @Schema(description = "利息结束日期")
    private Date lxEndDate;
    @Schema(description = "利息类型 1约定利率 2中国人民银行同期贷款基准利率与LPR自动分段 3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)")
    private Integer lxRateType;
    @Schema(description = "约定利率-约定周期 1年2月3日")
    private BigDecimal lxFixSectionType;
    @Schema(description = "约定利率-利率")
    private BigDecimal lxFixRate;
    /***************************************************************罚息***************************************************************/
    @Schema(description = "是否考虑罚息，0不考虑，1考虑。默认0")
    private Integer fx;
    @Schema(description = "罚息开始日期")
    private Date fxStartDate;
    @Schema(description = "罚息结束日期")
    private Date fxEndDate;
    @Schema(description = "罚息利息类型  1约定利率 2中国人民银行同期贷款基准利率与LPR自动分段 3全国银行间同业拆借中心公布的贷款市场报价利率(LPR) 备注：2014-08-01后为固定万分之1.75")
    private Integer fxRateType;
    @Schema(description = "罚息-约定利率-约定周期 1年2月3日")
    private BigDecimal fxFixSectionType;
    @Schema(description = "罚息约定利率-利率")
    private BigDecimal fxFixRate;

}
