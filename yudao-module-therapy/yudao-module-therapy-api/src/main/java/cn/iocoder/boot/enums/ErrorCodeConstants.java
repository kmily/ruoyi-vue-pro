package cn.iocoder.boot.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Therapy 错误码枚举类
 * <p>
 * Therapy 系统，使用 1-040-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 问卷相关  1-040-001-000 ============
    ErrorCode SURVEY_NOT_EXISTS = new ErrorCode(1_040_001_000, "治疗问卷不存在");
    ErrorCode USER_MOBILE_NOT_EXISTS = new ErrorCode(1_004_001_001, "手机号未注册用户");
    ErrorCode USER_MOBILE_USED = new ErrorCode(1_004_001_002, "修改手机失败，该手机号({})已经被使用");
    ErrorCode USER_POINT_NOT_ENOUGH = new ErrorCode(1_004_001_003, "用户积分余额不足");

    // ========== 治疗方案相关 1-040-002-000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_004_003_000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_004_003_001, "登录失败，账号被禁用");
    ErrorCode AUTH_SOCIAL_USER_NOT_FOUND = new ErrorCode(1_004_003_005, "登录失败，解析不到三方登录信息");
    ErrorCode AUTH_MOBILE_USED = new ErrorCode(1_004_003_007, "手机号已经被使用");

    // ========== xxxx相关 1-040-003-000 ==========
    ErrorCode ADDRESS_NOT_EXISTS = new ErrorCode(1_004_004_000, "用户收件地址不存在");


}
