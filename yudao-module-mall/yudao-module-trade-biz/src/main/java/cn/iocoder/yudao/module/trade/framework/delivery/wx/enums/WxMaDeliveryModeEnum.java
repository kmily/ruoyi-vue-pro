package cn.iocoder.yudao.module.trade.framework.delivery.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序发货信息管理服务的发货模式枚举
 *<p>
 * 参考文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/order-shipping/order-shipping.html">
 *
 */
@Getter
@AllArgsConstructor
public enum WxMaDeliveryModeEnum {

    UNIFIED_DELIVERY(1,"统一发货"),

    SPLIT_DELIVERY(2,"分拆发货");

    private final Integer code;

    private final String desc;

}
