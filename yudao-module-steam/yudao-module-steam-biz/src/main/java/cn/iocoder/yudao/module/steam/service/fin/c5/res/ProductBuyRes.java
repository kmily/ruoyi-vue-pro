package cn.iocoder.yudao.module.steam.service.fin.c5.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductBuyRes {

    private Data data;
    private int errorCode;
    private Object errorData;
    private String errorMsg;
    private boolean success;
    @lombok.Data
    public static class Data {
        @JsonProperty("buyPrice")
        private double buyPrice;

        @JsonProperty("delivery")
        private double delivery;

        @JsonProperty("offerId")
        private String offerId;

        @JsonProperty("orderId")
        private int orderId;

    }
}