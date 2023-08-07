package cn.iocoder.yudao.module.member.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Member 错误码枚举类
 *
 * member 系统，使用 1-004-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 用户相关  1004001000============
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1004001000, "用户不存在");
    ErrorCode USER_PASSWORD_FAILED = new ErrorCode(1004001001, "密码校验失败");

    // ========== AUTH 模块 1004003000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1004003000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1004003001, "登录失败，账号被禁用");
    ErrorCode AUTH_TOKEN_EXPIRED = new ErrorCode(1004003004, "Token 已经过期");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1004003005, "未绑定账号，需要进行绑定");
    ErrorCode AUTH_WEIXIN_MINI_APP_PHONE_CODE_ERROR = new ErrorCode(1004003006, "获得手机号失败");

    // ========== 用户收件地址 1004004000 ==========
    ErrorCode ADDRESS_NOT_EXISTS = new ErrorCode(1004004000, "用户收件地址不存在");

    // ========== 用户家庭 1004005000 ==========
    ErrorCode FAMILY_NOT_EXISTS = new ErrorCode(1004005000, "家庭不存在");


    // ========== 用户房间 1004006000 ==========
    ErrorCode ROOM_NOT_EXISTS = new ErrorCode(1004006000, "房间不存在");
    ErrorCode ROOM_NOT_MATE_FAMILY = new ErrorCode(1004006001, "房间所属家庭与家庭不匹配");

    // ========== 用户房间 1004007000 ==========
    ErrorCode DEVICE_USER_NOT_EXISTS = new ErrorCode(1004007000, "设备绑定信息不存在");
    ErrorCode DEVICE_NOT_EXISTS = new ErrorCode(1004007001, "设备信息不存在");
    ErrorCode FAMILY_DEVICE_NOT_EXISTS = new ErrorCode(1004007002, "家庭不存在雷达");

    // ========== 人员存在感知雷达设置 1004008000 ==========
    ErrorCode EXIST_ALARM_SETTINGS_NOT_EXISTS = new ErrorCode(1004008000, "人员存在感知雷达设置不存在");

    // ========== 跌倒雷达设置 1004009000 ==========
    ErrorCode FALL_ALARM_SETTINGS_NOT_EXISTS = new ErrorCode(1004009000, "跌倒雷达设置不存在");

    // ========== 体征检测雷达设置 1004001000 ==========
    ErrorCode HEALTH_ALARM_SETTINGS_NOT_EXISTS = new ErrorCode(1004001000, "体征检测雷达设置不存在");

   // ========== 人体检测雷达设置 1004001100 ==========
    ErrorCode DETECTION_ALARM_SETTINGS_NOT_EXISTS = new ErrorCode(1004001100, "人体检测雷达设置");

    // ========== 人体检测雷达设置 1004001200 ==========
    ErrorCode HOME_PAGE_NOT_EXISTS = new ErrorCode(1004001200, "首页配置不存在");
    ErrorCode HOME_PAGE_SYSTEM_CANOT_DELETED = new ErrorCode(1004001200, "系统配置不能删除");



}
