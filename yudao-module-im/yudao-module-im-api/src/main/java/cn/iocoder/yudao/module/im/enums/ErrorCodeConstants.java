package cn.iocoder.yudao.module.im.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * IM 错误码枚举类
 * <p>
 * im 系统，使用 1-040-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 会话 （1-040-100-000）  ==========
    ErrorCode CONVERSATION_NOT_EXISTS = new ErrorCode(1_040_100_000, "会话不存在");

    // ========== 收件箱 (1-040-200-000) ==========
    ErrorCode INBOX_NOT_EXISTS = new ErrorCode(1_040_200_000, "收件箱不存在");

    // ========== 消息 (1-040-300-000) ==========
    ErrorCode MESSAGE_NOT_EXISTS = new ErrorCode(1_040_300_000, "消息不存在");
}
