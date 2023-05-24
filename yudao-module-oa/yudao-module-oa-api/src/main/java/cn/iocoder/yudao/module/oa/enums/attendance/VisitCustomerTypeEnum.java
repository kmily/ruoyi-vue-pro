package cn.iocoder.yudao.module.oa.enums.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拜访类型的枚举
 */
@Getter
@AllArgsConstructor
public enum VisitCustomerTypeEnum {

    PRESALE(0, "售前拜访"),
    AFTERSALE(1, "售后拜访");
    private final Integer status;
    private final String name;
}
