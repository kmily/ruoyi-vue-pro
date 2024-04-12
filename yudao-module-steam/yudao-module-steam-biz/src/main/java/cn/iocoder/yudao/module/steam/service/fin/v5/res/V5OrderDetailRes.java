package cn.iocoder.yudao.module.steam.service.fin.v5.res;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class V5OrderDetailRes implements Serializable {
    private int code;
    private String msg;
    private Data data;


    @lombok.Data
    public static class Data {
        private String merchantNo;
        private String orderNo;
        private BuyerInfo buyerInfo;
        private ItemInfo itemInfo;
        private int orderStatus;
        private String orderStatusMsg;
        private BigDecimal orderAmount;
        private BigDecimal serviceFee;
        private BigDecimal serviceFeeRate;
        private String payAt;
        private String devileAt;
        private String createAt;



        @lombok.Data
        public static class BuyerInfo {
            private String steamId;
            private String steamName;
            private String steamCreateAt;
            private String tradeUrl;


        }

        @lombok.Data
        public static class ItemInfo {
            private String marketHashName;
            private String itemName;
            private String itemQuality;
            private String itemQualityName;
            private String itemType;
            private String itemTypeName;
            private String itemRarity;
            private String itemRarityName;
            private String itemExteriorName;
            private String itemExterior;
            private String floatValue;

        }
    }
}