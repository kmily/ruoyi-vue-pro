package cn.iocoder.yudao.module.oa.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * promotion 错误码枚举类
 *
 * OA 系统，使用 1-005-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== AUTH 模块 1005000000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1005000000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1005000001, "登录失败，账号被禁用");
    ErrorCode AUTH_LOGIN_CAPTCHA_CODE_ERROR = new ErrorCode(1005000004, "验证码不正确，原因：{}");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1005000005, "未绑定账号，需要进行绑定");
    ErrorCode AUTH_TOKEN_EXPIRED = new ErrorCode(1005000006, "Token 已经过期");
    ErrorCode AUTH_MOBILE_NOT_EXISTS = new ErrorCode(1005000007, "手机号不存在");
    ErrorCode AUTH_WE_CHAT_MINI_APP_PHONE_CODE_ERROR = new ErrorCode(1005000008, "获得手机号失败");


    // ============OA模块 1005001000 =============
    ErrorCode ATTENDANCE_NOT_EXISTS = new ErrorCode(1005001000, "考勤打卡不存在");
    ErrorCode EXPENSES_NOT_EXISTS = new ErrorCode(1005001001, "报销申请不存在");
    ErrorCode EXPENSES_DETAIL_NOT_EXISTS = new ErrorCode(1005001002, "报销明细不存在");
    ErrorCode BORROW_NOT_EXISTS = new ErrorCode(1005001003, "借支申请不存在");
    ErrorCode CONTRACT_NOT_EXISTS = new ErrorCode(1005001004, "合同不存在");
    ErrorCode PROJECT_IMPL_NOT_EXISTS = new ErrorCode(1005001005, "工程实施列表不存在");
    ErrorCode PROJECT_IMPL_LOG_NOT_EXISTS = new ErrorCode(1005001006, "工程日志列表不存在");
    ErrorCode OPPORTUNITY_NOT_EXISTS = new ErrorCode(1005001007, "商机不存在");
    ErrorCode FEEDBACK_NOT_EXISTS = new ErrorCode(1005001008, "产品反馈不存在");
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(1005001009, "客户不存在");
    ErrorCode SIGN_REQ_NOT_EXISTS = new ErrorCode(1005001010, "注册申请不存在");
    ErrorCode PRODUCT_NOT_EXISTS = new ErrorCode(1005001011, "产品不存在");
    ErrorCode PRODUCT_CODE_EXISTS = new ErrorCode(1005001012, "产品编码已存在");

}