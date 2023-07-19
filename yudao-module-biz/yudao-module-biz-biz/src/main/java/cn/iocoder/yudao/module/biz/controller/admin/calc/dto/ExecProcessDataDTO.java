package cn.iocoder.yudao.module.biz.controller.admin.calc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sz
 * @Package cn.iocoder.yudao.module.biz.controller.admin.calc.dto
 * @date 2023-07-19
 */
@Data
public class ExecProcessDataDTO {

    public ExecProcessDataDTO() {
    }

    public ExecProcessDataDTO(String id, String processId, Integer rateId, Date dateIndex, BigDecimal dayRate, BigDecimal dayAmount) {
        this.id = id;
        this.processId = processId;
        this.rateId = rateId;
        this.dateIndex = dateIndex;
        this.dayRate = dayRate;
        this.dayAmount = dayAmount;
    }

    private String id;
    private String processId;
    private Integer rateId;
    private Date dateIndex;
    private BigDecimal dayRate;
    private BigDecimal dayAmount;

}
