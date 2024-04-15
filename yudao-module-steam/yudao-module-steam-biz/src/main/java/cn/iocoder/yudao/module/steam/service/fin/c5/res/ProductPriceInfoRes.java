package cn.iocoder.yudao.module.steam.service.fin.c5.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductPriceInfoRes implements Serializable {
    private boolean success;
    private List<ProductData> data;
    private Integer errorCode;
    private String errorMsg;
    private String errorData;



    @Data
    public static class ProductData {
        private Integer appId;
        private String itemId;
        private String marketHashName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal autoDeliverPrice;
        private Integer autoDeliverQuantity;
        private BigDecimal manualDeliverPrice;
        private Integer manualQuantity;


    }
}