package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouPingOrder implements Serializable {

    @JsonProperty("merchantOrderNo")
    private String merchantOrderNo;
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("payAmount")
    private Double payAmount;
    @JsonProperty("orderStatus")
    private Integer orderStatus;
    @JsonProperty("ShippingMode")
    private Integer shippingMode;

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getShippingMode() {
        return shippingMode;
    }

    public void setShippingMode(Integer shippingMode) {
        this.shippingMode = shippingMode;
    }
}
