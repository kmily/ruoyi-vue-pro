package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 执行费-入参
 */
@Data
public class CalcInterestRateExecZxfParamVO {



    /**************************************************************公共参数*************************************************************/
    @Schema(description = "未履行债务总额")
    private BigDecimal leftAmount;
    @Schema(description = "执行费类型 1按照标的计算执行费、2按照已执行金额计算执行费（单次） 、3 按照已执行金额计算执行费（多次）")
    private Integer zxfType;

}
