package cn.iocoder.yudao.module.haoka.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * haoka 错误码枚举类
 * <p>
 * haoka 系统，使用 1-910-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== Demo 1_910_001_000 ==========
    ErrorCode DEMO_NOT_EXISTS = new ErrorCode(1_910_001_001, "好卡案例不存在");
}
