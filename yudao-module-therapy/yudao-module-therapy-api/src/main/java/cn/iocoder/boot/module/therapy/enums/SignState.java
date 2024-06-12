package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 日程签到状态
 */
@Getter
@AllArgsConstructor
public enum SignState implements IntArrayValuable {

    SIGNED(1, "已签到"),
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



    public static SignState getByType(Integer type) {
        return EnumUtil.getBy(SignState.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SignState::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
