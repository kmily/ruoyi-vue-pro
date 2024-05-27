package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 治疗问卷类型
 */
@Getter
@AllArgsConstructor
public enum SurveyType implements IntArrayValuable {

    SCALE(1, "量表","scale"),
    INTRODUCTION(2, "引导语",""),
    ;

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;

    private final String code;


    public static SurveyType getByType(Integer type) {
        return EnumUtil.getBy(SurveyType.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SurveyType::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
