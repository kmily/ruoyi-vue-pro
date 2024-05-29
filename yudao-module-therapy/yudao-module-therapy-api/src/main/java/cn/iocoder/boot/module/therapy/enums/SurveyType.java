package cn.iocoder.boot.module.therapy.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 治疗问卷类型
 */
@Getter
@AllArgsConstructor
public enum SurveyType implements IntArrayValuable {

    SCALE(1, "量表","scale",Arrays.asList(SurveyQuestionType.RADIO)),
    STRATEGY_CARD(2, "对策卡","strategy_card",Arrays.asList(SurveyQuestionType.SPAN)),
    PROBLEM_GOAL_MOTIVE(3,"问题、目标与动机", "problem_goal_motive",Arrays.asList()),
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

    private final List<SurveyQuestionType> qsts;


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
