package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡类型
 */
@Getter
@AllArgsConstructor
public enum AttendanceTypeEnum{
    WORK(0, "上班打卡"),
    VISIT_CUSTOMER(1, "拜访客户"),
    LEAVE(2 , "请假"),
    ;

    private final int type;
    private final String name;
}
