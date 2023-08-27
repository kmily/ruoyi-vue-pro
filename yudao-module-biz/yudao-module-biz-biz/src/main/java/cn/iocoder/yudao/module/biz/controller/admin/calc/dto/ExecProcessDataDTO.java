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


    public ExecProcessDataDTO(String id, String processId, Integer rateId, Date dateIndex, Date dateIndexEnd
            , BigDecimal dayRate, BigDecimal dayAmount, Integer days, BigDecimal yearRate
    ) {
        this.id = id;
        this.processId = processId;
        this.rateId = rateId;
        this.dateIndex = dateIndex;
        this.dateIndexEnd = dateIndexEnd;
        this.dayRate = dayRate;
        this.dayAmount = dayAmount;
        this.days = days;
        this.yearRate = yearRate;
    }

    public ExecProcessDataDTO(String id, String processId, Integer rateId, Date dateIndex, Date dateIndexEnd
            , BigDecimal dayRate, BigDecimal dayAmount, Integer days, BigDecimal yearRate, Integer fullDays, Integer isFull
    ) {
        this.id = id;
        this.processId = processId;
        this.rateId = rateId;
        this.dateIndex = dateIndex;
        this.dateIndexEnd = dateIndexEnd;
        this.dayRate = dayRate;
        this.dayAmount = dayAmount;
        this.days = days;
        this.yearRate = yearRate;
        this.fullDays = fullDays;
        this.isFull = isFull;
    }

    public ExecProcessDataDTO(String id, String processId, Integer rateId, Date dateIndex, BigDecimal dayRate, BigDecimal dayAmount, BigDecimal yearRate) {
        this.id = id;
        this.processId = processId;
        this.rateId = rateId;
        this.dateIndex = dateIndex;
        this.dayRate = dayRate;
        this.dayAmount = dayAmount;
        this.yearRate = yearRate;
    }

    private String id;
    private String processId;
    //1利息 2罚息
    private String calcType;
    private Integer rateId;
    private Date dateIndex;
    private Date dateIndexEnd;
    private BigDecimal dayRate;
    private BigDecimal yearRate;
    private BigDecimal dayAmount;
    private Integer days;
    private Integer fullDays;
    private Integer isFull;

}
