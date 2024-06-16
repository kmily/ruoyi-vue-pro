package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据统计类型
 */
@Getter
@AllArgsConstructor
public enum StatType implements IntArrayValuable {

    schedule(1, "已签到"),
    UNSIGNED(2, "未签到"),
    ;

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;



    public static StatType getByType(Integer type) {
        return EnumUtil.getBy(StatType.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(StatType::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
