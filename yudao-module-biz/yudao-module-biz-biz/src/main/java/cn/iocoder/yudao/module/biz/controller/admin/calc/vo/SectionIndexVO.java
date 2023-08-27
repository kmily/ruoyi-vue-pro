package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sz
 * @Package cn.iocoder.yudao.module.biz.controller.admin.calc.vo
 * @date 2023-07-20
 */
@Data
public class SectionIndexVO {

    //区间开始时间
    private String startDate;
    //区间结束时间
    private String endDate;
    //匹配的税率
    private String suiteRate;

    private String suiteDayRate;

    //天数
    private String days;
    //区间金额
    private BigDecimal sectionAmount;


}
