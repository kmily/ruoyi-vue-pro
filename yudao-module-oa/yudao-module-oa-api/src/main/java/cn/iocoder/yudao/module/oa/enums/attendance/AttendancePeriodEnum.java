package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡时间段枚举
 */
@Getter
@AllArgsConstructor
public enum AttendancePeriodEnum {
    MORNING(0, "上午打卡"),
    AFTERNOON(1, "下午打卡");
    private final int status;
    private final String name;
}
