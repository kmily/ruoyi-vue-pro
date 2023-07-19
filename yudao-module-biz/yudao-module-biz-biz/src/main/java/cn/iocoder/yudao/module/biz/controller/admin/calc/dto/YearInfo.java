package cn.iocoder.yudao.module.biz.controller.admin.calc.dto;

import lombok.Data;

/**
 * @author sz
 * @Package cn.iocoder.yudao.module.biz.controller.admin.calc.dto
 * @date 2023-07-19
 */
@Data
public class YearInfo {

    public YearInfo() {
    }

    public YearInfo(int year, boolean isLeapYear) {
        this.year = year;
        this.isLeapYear = isLeapYear;
    }

    //年度
    private int year;
    //是否闰年
    private boolean isLeapYear;


}
