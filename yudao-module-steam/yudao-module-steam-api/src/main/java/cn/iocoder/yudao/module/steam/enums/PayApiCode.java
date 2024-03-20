package cn.iocoder.yudao.module.steam.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface PayApiCode {
    ErrorCode PAY_ERROR = new ErrorCode(9001, "支出接口出错");
    ErrorCode PAY_ISV_EXISTS = new ErrorCode(9002, "此用户已经申请请等待审核");
    ErrorCode PAY_SIGN_ERR = new ErrorCode(9002, "签名错误");
}
