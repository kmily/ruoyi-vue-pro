package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 问题类型
 */
@Getter
@AllArgsConstructor
public enum SurveyQuestionType implements IntArrayValuable {

    RADIO(1, "单选题"),
    CHECKBOX(2, "多选题"),
    FILLBLANK(3,"填空题"),
    INTRODUCTION(4,"引导语"),
    SCALE(5,"量表"),
    ;

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;


    public static SurveyQuestionType getByType(Integer type) {
        return EnumUtil.getBy(SurveyQuestionType.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SurveyQuestionType::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
