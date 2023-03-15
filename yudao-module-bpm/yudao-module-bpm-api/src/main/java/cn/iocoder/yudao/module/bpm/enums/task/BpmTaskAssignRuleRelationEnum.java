package cn.iocoder.yudao.module.bpm.enums.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务分配规则条件关系
 */
@Getter
@AllArgsConstructor
public enum BpmTaskAssignRuleRelationEnum {
    OR(0, "或"),
    AND(1, "且"),
    NOT(2, "去除");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String desc;
}
