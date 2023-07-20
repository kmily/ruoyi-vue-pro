package cn.iocoder.yudao.module.biz.controller.admin.calc.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 利率计算-返回值
 */
@Data
public class CalcInterestRateExecResVO {


    private BigDecimal totalAmount;

    private String processId;

    private List<SectionIndexVO> sectionList;

}
