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
    PROBLEM_GOAL_MOTIVE(3,"问题、目标与动机", "problem_goal_motive",Arrays.asList(SurveyQuestionType.SPACES)),
    GAD7_SCALE(4, "GAD-7自评量表","gad7_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    ISI_SCALE(5, "失眠严重指数","isi_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    MOOD_SCALE(6, "积极情绪和消极情绪量表","mood_scale",Arrays.asList(SurveyQuestionType.RADIO)),
    SCHEDULE_LIST(7, "行为活动记录表","schedule_list",null),
    MOOD_MARK(8, "情绪评分","mood_mark",null),
    MOOD_DIARY(9, "心情日记","mood_diary",null),
    MOOD_RECOGNITION(10, "情绪识别","mood_recognition",null),
    COGNITION_RECONSTRUCTION(11, "认知重建", "cognition_reconstruction",null),
    AUTO_MINDSET_RECOGNIZE(12, "自动化思维和识别","auto_mindset_recognize",null),
    TWELVE_MIND_DISTORT(13, "12种心理歪曲","twelve_mind_distort",null),
    HAPPY_ACTIVITY(13, "愉悦活动清单","happy_activity",null)
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
