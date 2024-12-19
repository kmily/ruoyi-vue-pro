package cn.iocoder.yudao.module.bpm.enums.definition;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * BPM 流程的类型的枚举
 *
 * @author Lesan
 */
@Getter
@AllArgsConstructor
public enum BpmProcessTypeEnum implements IntArrayValuable {

    MAIN(1, "主流程"), // 主流程
    CHILD(2, "子流程"); // 子流程

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(BpmProcessTypeEnum::getType).toArray();

    private final Integer type;
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}