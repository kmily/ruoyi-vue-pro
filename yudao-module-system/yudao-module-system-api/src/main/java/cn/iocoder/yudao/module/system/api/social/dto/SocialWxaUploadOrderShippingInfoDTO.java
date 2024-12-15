package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

@Data
public class SocialWxaUploadOrderShippingInfoDTO {
    /**
     * 渠道用户编号
     *
     * 例如说，微信 openid、支付宝账号
     */
    private String channelUserId;
    /**
     * 微信支付渠道订单号
     */
    private String channelOrderNo;
    /**
     * 物流模式
     * 1、实体物流配送采用快递公司进行实体物流配送形式
     * 2、同城配送
     * 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式
     * 4、用户自提
     */
    private Integer logisticsType;
    /**
     * 物流发货单号
     */
    private String logisticsNo;
    /**
     * 物流公司编号
     * 国内常用如下：
     * STO	申通快递
     * ZTO	中通快递
     * SF	顺丰快递
     * YZPY	邮政快递包裹
     * YTO	圆通速递
     * YD	韵达快递
     * JTSD	极兔快递
     * JD	京东物流
     * JDKY	京东快运
     *
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/industry/express/business/express_search.html#%E8%8E%B7%E5%8F%96%E8%BF%90%E5%8A%9Bid%E5%88%97%E8%A1%A8get-delivery-list
     */
    private String expressCompany;
    /**
     * 商品信息，例如：微信红包抱枕*1个，限120个字以内
     */
    private String itemDesc;
    /**
     * 收件人手机号，当发货的物流公司为顺丰时，联系方式为必填
     */
    private String receiverContact;
}
