package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class V5queryCommodityDetailRespVO {

    private Integer code;

    private String msg;

    private OrderDetailInfo data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderDetailInfo {

        private BuyerInfo buyerInfo;
        private ItemInfo itemInfo;
        @JsonProperty("steamId")
        private String merchantNo;
        @JsonProperty("orderNo")
        private String orderNo;
        @JsonProperty("orderStatus")
        private Integer orderStatus;
        @JsonProperty("orderStatusMsg")
        private String orderStatusMsg;
        @JsonProperty("orderAmount")
        private Integer orderAmount;
        @JsonProperty("serviceFee")
        private Integer serviceFee;
        @JsonProperty("serviceFeeRate")
        private Integer serviceFeeRate;
        @JsonProperty("payAt")
        private String payAt;
        @JsonProperty("devileAt")
        private String devileAt;
        @JsonProperty("createAt")
        private String createAt;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BuyerInfo {
            @JsonProperty("steamId")
            private String steamId;
            @JsonProperty("SteamName")
            private String SteamName;
            @JsonProperty("steamCreateAt")
            private String steamCreateAt;
            @JsonProperty("tradeUrl")
            private String tradeUrl;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ItemInfo {
            @JsonProperty("marketHashName")
            private String marketHashName;
            @JsonProperty("itemName")
            private String itemName;
            @JsonProperty("itemQuality")
            private String itemQuality;
            @JsonProperty("itemQualityName")
            private String itemQualityName;
            @JsonProperty("itemType")
            private String itemType;
            @JsonProperty("itemTypeName")
            private String itemTypeName;
            @JsonProperty("itemRarity")
            private String itemRarity;
            @JsonProperty("itemRarityName")
            private String itemRarityName;
            @JsonProperty("itemExteriorName")
            private String itemExteriorName;
            @JsonProperty("itemExterior")
            private String itemExterior;
            @JsonProperty("floatValue")
            private String floatValue;
        }
    }
}
