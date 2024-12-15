package cn.iocoder.yudao.module.trade.framework.delivery.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序发货信息管理服务的发货方式枚举
 *<p>
 * 参考文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/order-shipping/order-shipping.html">
 *
 */
@Getter
@AllArgsConstructor
public enum WxLogisticsTypeEnum {

    EXPRESS(1,"物流配送"),
    INTRA_CITY(2,"同城配送"),
    VIRTUAL(3,"虚拟商品"),
    PICK_UP(4,"用户自提");


    private final Integer code;

    private final String desc;
}
