package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 报告状态
 */
@Getter
@AllArgsConstructor
public enum ReprotState implements IntArrayValuable {

    NOT(0, "未生成"),
    DONE(1, "已生成"),

    ;

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;



    public static ReprotState getByType(Integer type) {
        return EnumUtil.getBy(ReprotState.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ReprotState::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
