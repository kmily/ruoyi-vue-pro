package cn.iocoder.yudao.framework.pay.core.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.pay.core.client.PayClientConfig;
import cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipayPayClientConfig;
import cn.iocoder.yudao.framework.pay.core.client.impl.wx.WXPayClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道的编码的枚举
 * 枚举值
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    WX_JSAPI("wx_jsapi", "微信 JSAPI 支付(适用于 公众号)", WXPayClientConfig.class),// WX_JSAPI 调起js支付前端支付调用文档（无后台接口交互）：  https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_4.shtml
    WX_H5("wx_h5", "微信 H5 支付", WXPayClientConfig.class),// WX_H5 调起H5支付前端支付调用文档（无后台接口交互）： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_3_4.shtml

    WX_APP("wx_app", "微信 App 支付", WXPayClientConfig.class),// WX_APP调起支付前端支付调用文档（无后台接口交互）：  https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_4.shtml
    WX_NATIVE("wx_native", "微信 native 支付", WXPayClientConfig.class),

    ALIPAY_PC("alipay_pc", "支付宝 PC 网站支付", AlipayPayClientConfig.class),
    ALIPAY_WAP("alipay_wap", "支付宝 Wap 网站支付", AlipayPayClientConfig.class),
    ALIPAY_APP("alipay_app", "支付宝App 支付", AlipayPayClientConfig.class),
    ALIPAY_QR("alipay_qr", "支付宝扫码支付", AlipayPayClientConfig.class);

    /**
     * 编码
     * <p>
     * 参考 https://www.pingxx.com/api/支付渠道属性值.html
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    /**
     * 配置类
     */
    private final Class<? extends PayClientConfig> configClass;

    /**
     * 微信支付
     */
    public static final String WECHAT = "WECHAT";

    /**
     * 支付宝支付
     */
    public static final String ALIPAY = "ALIPAY";

    public static PayChannelEnum getByCode(String code) {
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code), values());
    }

}
