package cn.iocoder.boot.module.therapy.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Therapy 错误码枚举类
 * <p>
 * Therapy 系统，使用 1-040-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 问卷相关  1-040-001-000 ============
    ErrorCode SURVEY_NOT_EXISTS = new ErrorCode(1_040_001_000, "治疗问卷不存在");
    ErrorCode SURVEY_EXISTS_UNFINISHED = new ErrorCode(1_040_001_001, "有必答题没有未成");
    ErrorCode SURVEY_QUESTION_NOT_EXISTS = new ErrorCode(1_040_001_002, "问卷中不存这个题目");
    ErrorCode SURVEY_QUESTION_TYPE_CHANGE = new ErrorCode(1_040_001_003, "题目类型发生变更,请重新答题");
    ErrorCode SURVEY_QUESTION_EMPTY = new ErrorCode(1_040_001_004, "问卷题目不能为空,请设置题目后提交");
    ErrorCode QUESTION_NOT_EXISTS_SURVEY = new ErrorCode(1_040_001_004, "提交的问题不属于此问卷");
    ErrorCode SURVEY_QUESTION_QUANTITY_INACCURACY = new ErrorCode(1_040_001_005, "问卷题目数量不对");
    ErrorCode SURVEY_ANSWER_NOT_EXISTS = new ErrorCode(1_040_001_006, "答题实例不存在");

    // ========== 治疗方案相关 1-040-002-000 ==========
    ErrorCode TREATMENT_FLOW_NOT_EXISTS = new ErrorCode(1_040_002_000, "治疗方案不存在");
    ErrorCode TREATMENT_PLAN_NOT_EXISTS = new ErrorCode(1_004_002_001, "治疗计划不存在");
    ErrorCode TREATMENT_PLAN_TASK_NOT_EXISTS = new ErrorCode(1_004_003_002, "治疗计划的任务不存在");
    ErrorCode TREATMENT_PLAN_SEQ_EXISTS = new ErrorCode(1_004_003_003, "已经存在当天的治疗计划");

    ErrorCode MEMBER_GROUP_NOT_SETTINGS = new ErrorCode(1_004_003_004, "请联系医生设置您的分组");


    // ========== 活动计划相关 ==========
    ErrorCode TREATMENT_SCHEDULE_NOT_EXISTS = new ErrorCode(1_004_004_000, "患者日程不存在");
    ErrorCode TREATMENT_SCHEDULE_SIGNED = new ErrorCode(1_004_004_001, "患者已签到不能删除或再次重复签到");
    ErrorCode TREATMENT_SCHEDULE_SIGNED_NOW_NOT_BETWEEN = new ErrorCode(1_004_004_001, "当前时间不在日程时间内,不能签到");

    //治疗流程相关
    ErrorCode TREATMENT_DAYITEM_STEP_PARAMS_ERROR = new ErrorCode(1_050_001_001, "治疗子任务步骤参数错误");
    ErrorCode TREATMENT_REQUIRE_GOAL_AND_MOTIVATION = new ErrorCode(1_050_001_002, "请先完成目标与动机的填写");

    ErrorCode TREATMENT_NO_STRATEGY_GAME_FOUND = new ErrorCode(1_050_001_003, "没有找到对应的策略游戏, TAG={}");
    ErrorCode TREATMENT_NO_CASE_STUDY_FOUND = new ErrorCode(1_050_001_004, "没有找到对应的案例练习, TAG={}");
    ErrorCode TREATMENT_NO_STRATEGY_SURVEY_FOUND = new ErrorCode(1_050_001_005, "没有找到对应的策略问卷, survey_id={}");
    ErrorCode TREATMENT_NO_STRATEGY_CARD_FOUND = new ErrorCode(1_050_001_004, "没有找到策略卡");

}
