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
public enum TaskType implements IntArrayValuable {

    SCALE(1, "量表引入","scale"),
    GUIDE_LANGUAGE(2, "引导语","guide_language"),
    PROBLEM_GOAL_MOTIVE(3,"问题、目标与动机", "problem_goal_motive"),
    CURRENT_MOOD_SCORE(4,"当前情绪评分","current_mood_score"),
    MOOD_RECOGNIZE_NAMED(5,"情绪识别和命名","mood_recognize_named"),
    AUTO_MINDSET_RECOGNIZE(6,"自动化思维和识别","auto_mindset_recognize"),
    TWELVE_MIND_DISTORT(7,"12种心理歪曲","twelve_mind_distort"),
    COGNIZE_REESTABLISH(8,"认知重建","cognize_reestablish"),
    HAPPLY_EXERCISE_LIST(9,"愉悦活动清单","happly_exercise_list"),
    BEHAVIOUR_EXERCISE_PLAN(10,"行为活动计划","behaviour_exercise_plan")
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


    public static TaskType getByType(Integer type) {
        return EnumUtil.getBy(TaskType.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(TaskType::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }


    public String getCode(){
        return this.code;
    }
}
