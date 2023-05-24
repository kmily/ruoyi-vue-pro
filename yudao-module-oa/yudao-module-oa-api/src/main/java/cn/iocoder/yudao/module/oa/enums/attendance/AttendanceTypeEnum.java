package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡类型
 */
@Getter
@AllArgsConstructor
public enum AttendanceTypeEnum{
    WORK(1, "上班打卡"),
    VISIT_CUSTOMER(2, "拜访客户"),
    LEAVE(3 , "请假"),
    ;

    private final int type;
    private final String name;
}
