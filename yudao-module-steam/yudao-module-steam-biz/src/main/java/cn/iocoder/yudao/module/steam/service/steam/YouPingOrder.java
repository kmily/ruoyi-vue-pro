package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouPingOrder implements Serializable {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("data")
    private DataDTO data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDTO {
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
}
