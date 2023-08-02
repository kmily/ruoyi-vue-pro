package cn.iocoder.yudao.module.promotion.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Bargain 错误码枚举类
 * <p>
 * Bargain 系统，使用 1-089-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 砍价活动相关  ============
    ErrorCode BARGAIN_ACTIVITY_NOT_EXISTS = new ErrorCode(1089001001, "砍价活动不存在");

}
