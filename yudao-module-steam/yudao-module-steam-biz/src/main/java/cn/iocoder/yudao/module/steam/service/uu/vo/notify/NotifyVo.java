package cn.iocoder.yudao.module.steam.service.uu.vo.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 有品回调信息
 */
@NoArgsConstructor
@Data
public class NotifyVo {

    /**
     * 订单号
     */
    @JsonProperty("orderNo")
    private String orderNo;
    /**
     * 预留字段
     */
    @JsonProperty("orderType")
    private Integer orderType;
    /**
     * 预留字段
     */
    @JsonProperty("orderSubType")
    private Integer orderSubType;
    /**
     * 发货模式：0,卖家直发；1,极速发货
     */
    @JsonProperty("shippingMode")
    private Integer shippingMode;
    /**
     * 报价ID
     */
    @JsonProperty("tradeOfferId")
    private Long tradeOfferId;
    /**
     * 报价链接
     */
    @JsonProperty("tradeOfferLinks")
    private String tradeOfferLinks;
    /**
     * 购买用户编号。
     */
    @JsonProperty("buyerUserId")
    private Integer buyerUserId;
    /**
     * 订单大状态
     */
    @JsonProperty("orderStatus")
    private Integer orderStatus;
    /**
     * Integer	订单小状态。
     */
    @JsonProperty("orderSubStatus")
    private Integer orderSubStatus;
    /**
     * 订单失败原因编号。
     */
    @JsonProperty("failCode")
    private Integer failCode;
    /**
     * 订单失败原因提示信息。
     */
    @JsonProperty("failReason")
    private String failReason;
    /**
     * 第三方商户单号。
     */
    @JsonProperty("merchantOrderNo")
    private String merchantOrderNo;
    /**
     * 通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。
     */
    @JsonProperty("notifyType")
    private Integer notifyType;
    /**
     * 通知类型描述(等待发货，等待收货，购买成功，订单取消)。
     */
    @JsonProperty("notifyDesc")
    private String notifyDesc;
}
