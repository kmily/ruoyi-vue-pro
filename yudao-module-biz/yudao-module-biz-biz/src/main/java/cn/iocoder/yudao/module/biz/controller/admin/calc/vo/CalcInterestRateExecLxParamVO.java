package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 利率计算-入参
 */
@Data
public class CalcInterestRateExecLxParamVO {

    /**************************************************************公共参数*************************************************************/
    @Schema(description = "利息金额")
    private BigDecimal leftAmount;
    @Schema(description = "是否考虑闰年，0不考虑，1考虑。默认0")
    private Integer leapYear;
    @Schema(description = "执行序号")
    private String processId;
    /***************************************************************利息***************************************************************/
    @Schema(description = "利息开始日期")
    private Date startDate;
    @Schema(description = "利息结束日期")
    private Date endDate;
    @Schema(description = "利息类型 1约定利率 2中国人民银行同期贷款基准利率与LPR自动分段 3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)")
    private Integer rateType;
    /*************************************************************约定利息*************************************************************/
    @Schema(description = "约定利率-约定周期 1年2月3日")
    private Integer fixSectionType;
    @Schema(description = "约定类型 1指定利率值 2LPR倍数 默认1")
    private Integer fixType;
    @Schema(description = "约定利率-利率值")
    private BigDecimal fixRate;
    @Schema(description = "约定利率-LPR倍数")
    private BigDecimal fixLPRs;

}
