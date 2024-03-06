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
    //----支付业务出错
    ErrorCode ERR_5201 = new ErrorCode(5201, "该账户交易功能被封禁,可以联系人工客服");
    ErrorCode ERR_5202 = new ErrorCode(5202, "代购对象由于过去12小时赠送订单存在多次不收货或者拒绝收货行为，系统暂时限制其进行购买，该限制将在%s之后解除。");
    ErrorCode ERR_5212 = new ErrorCode(5212, "余额不足，请确保余额充足再创单");
    ErrorCode ERR_5213 = new ErrorCode(5213, "此限定金额下无商品可供购买");
    ErrorCode ERR_5214 = new ErrorCode(5214, "暂无在售商品，请选购其他商品");
    ErrorCode ERR_5301 = new ErrorCode(5301, "商品状态发生变化，暂无法购买，请选购其他商品");
    ErrorCode ERR_5302 = new ErrorCode(5302, "您不能购买自己的商品，请选购其他商品");
    ErrorCode ERR_5303 = new ErrorCode(5303, "该商品为私密交易商品，请选购其他商品");
    ErrorCode ERR_5304 = new ErrorCode(5303, "该商品只租不售，请选购其他商品");
    ErrorCode ERR_5299 = new ErrorCode(5299, "无法购买");
    ErrorCode ERR_5401 = new ErrorCode(5401, "Steam网络异常");
    ErrorCode ERR_5402 = new ErrorCode(5402, "请稍后再试");
    ErrorCode ERR_5403 = new ErrorCode(5403, "代购对象Steam账号暂无法交易");
    ErrorCode ERR_5404 = new ErrorCode(5404, "代购对象Steam被封禁，无法交易");
    ErrorCode ERR_5405 = new ErrorCode(5405, "代购对象的交易链接已失效");
    ErrorCode ERR_5406 = new ErrorCode(5406, "代购对象Steam被封禁，无法交易");
    ErrorCode ERR_5407 = new ErrorCode(5407, "不可输入本人交易链接");
    ErrorCode ERR_5408 = new ErrorCode(5408, "代购对象的交易链接格式错误");
}
