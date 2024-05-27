package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 方案类型
 * 字典：flow_type
 */
@Getter
@AllArgsConstructor
public enum FlowType implements IntArrayValuable {

    LOW_MOOD(1, "情绪低落"),
    ANXIETY(2, "焦虑"),
    LOW_ENERGY(3, "精力下降"),
    ;

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;


    public static FlowType getByType(Integer type) {
        return EnumUtil.getBy(FlowType.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(FlowType::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
