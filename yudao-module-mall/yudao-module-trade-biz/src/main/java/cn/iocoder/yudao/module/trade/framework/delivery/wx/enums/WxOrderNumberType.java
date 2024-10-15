package cn.iocoder.yudao.module.trade.framework.delivery.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序发货信息管理服务的订单单号类型枚举
 *<p>
 * 参考文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/order-shipping/order-shipping.html">
 *
 */
@Getter
@AllArgsConstructor
public enum WxOrderNumberType {

    MCH(1,"商户号和商户侧单号"),

    TRANSACTION(2,"微信支付单号");

    private final Integer code;

    private final String desc;

}
