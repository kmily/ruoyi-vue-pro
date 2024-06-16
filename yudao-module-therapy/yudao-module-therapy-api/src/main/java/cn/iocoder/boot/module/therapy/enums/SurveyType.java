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

    PHQ9_SCALE(1, "PHQ-9健康问卷","phq9_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    STRATEGY_CARD(2, "对策卡","strategy_card",Arrays.asList(SurveyQuestionType.SPAN)),
    PROBLEM_GOAL_MOTIVE(3,"目标与动机", "problem_goal_motive",Arrays.asList(SurveyQuestionType.SPACES)),
    GAD7_SCALE(4, "GAD-7自评量表","gad7_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    ISI_SCALE(5, "失眠严重指数","isi_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    MOOD_SCALE(6, "积极情绪和消极情绪量表","mood_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    SCHEDULE_LIST(7, "行为活动记录表","schedule_list",null),
    MOOD_MARK(8, "情绪评分","mood_mark",null),
    MOOD_DIARY(9, "心情日记","mood_diary",null),
    MOOD_RECOGNITION(10, "情绪识别","mood_recognition",null),
    AUTO_THOUGHT_RECOGNITION(11, "自动化思维识别","auto_thought_recognition",null),
    COGNIZE_REBUILD(12, "认知重建","cognize_rebuild",null),
    STRATEGY_GAMES(13, "对策游戏","strategy_games",Arrays.asList(SurveyQuestionType.SPAN)),
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
