package cn.iocoder.yudao.module.steam.service.fin.c5.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class C5OrderInfo implements Serializable {
    private boolean success;
    private OrderInfor data;
    @JsonProperty("errorCode")
    private int errorCode;
    private String errorMsg;
    private Object errorData;

    @Data
    public static class OrderInfor {
        private String orderId;
        private String productId;
        private double price;
        private double buyerFee;
        private String statusName;
        private int status;
        private int deliverType;
        private String receiveSteamId;
        private OpenItemInfo openItemInfo;
        private AssetInfo assetInfo;
        private OfferInfoDTO offerInfoDTO;
        private String failedDesc;
        private long createTime;
    }

    @Data
    public static class OpenItemInfo {
        private String itemId;
        private int appId;
        private String name;
        private String marketHashName;
        private String imageUrl;
    }

    @Data
    public static class AssetInfo {
        private Object classInfoId;
        private Object classId;
        private Object instanceId;
        private Object assetId;
        private Object styleId;
        private Object lastStyle;
        private Object styleProgress;
        private Object wear;
        private Object paintIndex;
        private Object paintSeed;
        private Object levelName;
        private Object levelColor;
        private Object gradient;
        private String fadeColor;
        private Object inspectImageUrl;
        private Object gems;
        private Object stickers;
        private Object styles;
        private Object itemSets;
        private Object ext;
        private Object fraudwarning;
    }

    @Data
    public static class OfferInfoDTO {
        private long transferId;
        private long tradeOfferId;
    }
}