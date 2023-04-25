package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendancePeriodEnum {
    MORNING(0, "上午打卡"),
    AFTERNOON(1, "下午打卡");
    private final Integer status;
    private final String name;
}
