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

    // ========== 治疗方案相关 1-040-002-000 ==========
    ErrorCode TREATMENT_FLOW_NOT_EXISTS = new ErrorCode(1_040_002_000, "治疗方案不存在");
    ErrorCode TREATMENT_PLAN_NOT_EXISTS = new ErrorCode(1_004_002_001, "治疗计划不存在");
    ErrorCode TREATMENT_PLAN_TASK_NOT_EXISTS = new ErrorCode(1_004_003_002, "治疗计划的任务不存在");
    ErrorCode TREATMENT_PLAN_SEQ_EXISTS = new ErrorCode(1_004_003_003, "已经存在当天的治疗计划");

    // ========== xxxx相关 1-040-003-000 ==========
    ErrorCode ADDRESS_NOT_EXISTS = new ErrorCode(1_004_004_000, "用户收件地址不存在");


}
