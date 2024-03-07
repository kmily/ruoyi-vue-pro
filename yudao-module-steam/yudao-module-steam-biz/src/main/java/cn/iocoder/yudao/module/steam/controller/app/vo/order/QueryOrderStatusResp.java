package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryOrderStatusResp {
    /**
     * 订单编号
     */
    @JsonProperty("orderNumber")
    private String orderNumber;

    /**
     * 发货模式
     * 0：卖家直发；
     * 1：极速发货
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
     * 订单状态
     */
    @JsonProperty("statusMsg")
    private String statusMsg;

    /**
     * 订单大状态
     */
    @JsonProperty("bigStatus")
    private Integer bigStatus;

    /**
     * 订单大状态提示信息。
     */
    @JsonProperty("bigStatusMsg")
    private String bigStatusMsg;

    /**
     * 订单小状态
     */
    @JsonProperty("smallStatus")
    private Integer smallStatus;

    /**
     * 订单小状态提示信息。
     */
    @JsonProperty("smallStatusMsg")
    private String smallStatusMsg;

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
}
