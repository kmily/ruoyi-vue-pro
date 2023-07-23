package cn.iocoder.yudao.module.biz.controller.admin.calc.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author sz
 * @Package cn.iocoder.yudao.module.biz.controller.admin.calc.dto
 * @date 2023-07-23
 */
@Data
public class MonthInfoDTO {


    public MonthInfoDTO(Date monthStartDate, Date monthEndDate, Integer isFull, Integer days) {
        this.monthStartDate = monthStartDate;
        this.monthEndDate = monthEndDate;
        this.isFull = isFull;
        this.days = days;
    }

    private Date monthStartDate;
    private Date monthEndDate;
    private Integer isFull;
    private Integer days;


}
