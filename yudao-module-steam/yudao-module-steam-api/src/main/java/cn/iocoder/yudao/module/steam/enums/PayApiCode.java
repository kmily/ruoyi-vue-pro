package cn.iocoder.yudao.module.steam.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface PayApiCode {
    ErrorCode PAY_ERROR = new ErrorCode(4001, "支出接口出错");
}
