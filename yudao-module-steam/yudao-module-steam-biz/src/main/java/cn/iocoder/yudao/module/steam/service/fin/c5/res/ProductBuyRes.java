package cn.iocoder.yudao.module.steam.service.fin.c5.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductBuyRes implements Serializable {

    private Inform data;
    private int errorCode;
    private Object errorData;
    private String errorMsg;
    private boolean success;
    @Data
    public static class Inform {
        @JsonProperty("buyPrice")
        private BigDecimal buyPrice;

        @JsonProperty("delivery")
        private Integer delivery;

        @JsonProperty("offerId")
        private String offerId;

        @JsonProperty("orderId")
        private String orderId;

    }
}