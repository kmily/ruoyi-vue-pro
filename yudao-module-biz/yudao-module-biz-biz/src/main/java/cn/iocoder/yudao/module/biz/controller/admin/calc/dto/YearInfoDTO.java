package cn.iocoder.yudao.module.biz.controller.admin.calc.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author sz
 * @Package cn.iocoder.yudao.module.biz.controller.admin.calc.dto
 * @date 2023-07-23
 */
@Data
public class YearInfoDTO {

    public YearInfoDTO() {
    }

    public YearInfoDTO(Date yearStartDate, Date yearEndDate, Integer isFull, Integer days) {
        this.yearStartDate = yearStartDate;
        this.yearEndDate = yearEndDate;
        this.isFull = isFull;
        this.days = days;
    }

    public YearInfoDTO(Date yearStartDate, Date yearEndDate, Integer isFull, Integer days, Integer fullDays) {
        this.yearStartDate = yearStartDate;
        this.yearEndDate = yearEndDate;
        this.isFull = isFull;
        this.days = days;
        this.fullDays = fullDays;
    }

    private Date yearStartDate;
    private Date yearEndDate;
    private Integer isFull;
    private Integer days;
    private Integer fullDays;


}
