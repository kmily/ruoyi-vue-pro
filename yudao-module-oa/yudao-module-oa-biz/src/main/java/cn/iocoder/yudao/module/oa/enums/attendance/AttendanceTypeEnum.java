package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendanceTypeEnum {
    WORK(0, "上班"),
    VISIT(1, "拜访客户"),
    LEAVE(2, "请假");
    private final Integer status;
    private final String name;
}
