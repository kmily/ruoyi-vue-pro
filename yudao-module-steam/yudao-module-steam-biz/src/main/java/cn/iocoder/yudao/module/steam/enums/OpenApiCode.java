package cn.iocoder.yudao.module.steam.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface OpenApiCode {
    ErrorCode OK = new ErrorCode(0,"成功。");
    ErrorCode CHECK_SIGN_ERROR = new ErrorCode(4001, "验签失败。");
    ErrorCode SIGN_ERROR = new ErrorCode(104001, "签名失败。");
    ErrorCode DISABLED = new ErrorCode(4002, "api调用权限被封禁。");
    ErrorCode CLOSE = new ErrorCode(4003, "此接口暂时关闭。");
    ErrorCode TO_MANY = new ErrorCode(4004, "请求过频繁。");
    ErrorCode JACKSON_EXCEPTION = new ErrorCode(4010, "参数错误。");
    ErrorCode ID_ERROR = new ErrorCode(4011, "身份ID不存在。");
    ErrorCode CONCAT_ADMIN = new ErrorCode(5100, "请联系管理员。");
}
