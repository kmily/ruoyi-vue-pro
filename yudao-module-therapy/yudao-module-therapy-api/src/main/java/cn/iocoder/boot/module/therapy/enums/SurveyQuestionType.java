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

    RADIO(1,"radio", "单选题"),
    CHECKBOX(2,"checkbox", "多选题"),
    TREE(3,"tree", "树"),
    SPAN(4,"span", "文本框"),
    SPACES(5,"spaces", "占位题"),
    DIALOG(6,"from", "对话框"),
    ;

    /**
     * 业务类型
     */
    private final int type;

    private final String code;
    /**
     * 标题
     */
    private final String title;


    public static SurveyQuestionType getByType(Integer type) {
        return EnumUtil.getBy(SurveyQuestionType.class,
                e -> Objects.equals(type, e.getType()));
    }

    public static SurveyQuestionType getByCode(String code) {
        return EnumUtil.getBy(SurveyQuestionType.class,
                e -> Objects.equals(code, e.getCode()));
    }

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SurveyQuestionType::getType).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
