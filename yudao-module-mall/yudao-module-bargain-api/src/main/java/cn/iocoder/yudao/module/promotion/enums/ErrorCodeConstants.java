package cn.iocoder.yudao.module.promotion.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Bargain 错误码枚举类
 *
 * 砍价 系统，使用 1-030-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 砍价相关 1030001000 ============
    ErrorCode BARGAIN_ACTIVITY_NOT_EXISTS = new ErrorCode(1030001000, "砍价不存在");
}
