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

    /**
     * 1约定利率
     * 2中国人民银行同期贷款基准利率与LPR自动分段
     * 3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)
     */

    @Schema(description = "利息开始日期")
    private Date lxStartDate;
    @Schema(description = "利息结束日期")
    private Date lxEndDate;
    @Schema(description = "指定利息-利率")
    private BigDecimal lxFixRate;
    @Schema(description = "指定利息-单位")
    private Integer lxFixRateUnit;
    @Schema(description = "利息类型")
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
    @Schema(description = "罚息指定利息-单位")
    private Integer fxFixRateUnit;

}
