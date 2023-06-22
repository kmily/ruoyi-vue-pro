package com.cw.module.trade.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 交易模块 错误码枚举类
 *
 * 交易模块，使用 1-005-000-000 段
 */
public interface ErrorCodeConstants {
    
    ErrorCode ACCOUNT_NOT_EXISTS = new ErrorCode(1005000000, "交易账号不存在");

}
