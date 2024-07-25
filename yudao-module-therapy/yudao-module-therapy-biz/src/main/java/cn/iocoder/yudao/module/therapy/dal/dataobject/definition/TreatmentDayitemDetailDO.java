package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TreatmentDayitemDetailDO {
    // day info
    private Long dayId;

    private boolean hasBreak;

    private int flowDayIndex;

    private String flowDayName;

    // day instance info

    private Long dayInstanceId;

    private Long dayitemId;

    private int dayInstanceStatus;

    // day item info
    private String itemName;

    private int dayitemType;

    // day item instance info

    private Long dayitemInstanceId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int dayitemInstanceStatus;


    public boolean getHasBreak() {
        return hasBreak;
    }
}
