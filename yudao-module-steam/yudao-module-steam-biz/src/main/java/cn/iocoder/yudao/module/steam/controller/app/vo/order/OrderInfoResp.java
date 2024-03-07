package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInfoResp {

    @JsonProperty("id")
    private String id;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("processStatus")
    private Integer processStatus;
    @JsonProperty("orderStatus")
    private Integer orderStatus;
    @JsonProperty("orderSubStatus")
    private Integer orderSubStatus;
    @JsonProperty("orderType")
    private Integer orderType;
    @JsonProperty("orderSubType")
    private Integer orderSubType;
    @JsonProperty("fromSystem")
    private Integer fromSystem;
    @JsonProperty("buyerUserId")
    private Long buyerUserId;
    @JsonProperty("buyerUserName")
    private String buyerUserName;
    @JsonProperty("buyerUserIcon")
    private String buyerUserIcon;
    @JsonProperty("sellerUserId")
    private Long sellerUserId;
    @JsonProperty("sellerUserName")
    private String sellerUserName;
    @JsonProperty("sellerUserIcon")
    private String sellerUserIcon;
    @JsonProperty("createOrderTime")
    private Long createOrderTime;
    @JsonProperty("finishOrderTime")
    private Long finishOrderTime;
    @JsonProperty("paySuccessTime")
    private Long paySuccessTime;
    @JsonProperty("payEndTime")
    private Long payEndTime;
    @JsonProperty("sendOfferEndTime")
    private Long sendOfferEndTime;
    @JsonProperty("recordTime")
    private Long recordTime;
    @JsonProperty("discountAmount")
    private String discountAmount;
    @JsonProperty("totalAmount")
    private String totalAmount;
    @JsonProperty("isCust")
    private Integer isCust;
    @JsonProperty("cancelReason")
    private String cancelReason;
    @JsonProperty("productDetail")
    private ProductDetailDTO productDetail;
    @JsonProperty("orderStatusName")
    private String orderStatusName;
    @JsonProperty("orderStatusDesc")
    private String orderStatusDesc;
    @JsonProperty("orderStatusColor")
    private String orderStatusColor;
    @JsonProperty("orderSubStatusName")
    private String orderSubStatusName;
    @JsonProperty("timeType")
    private Integer timeType;
    @JsonProperty("returnAmount")
    private String returnAmount;
    @JsonProperty("serviceFee")
    private String serviceFee;
    @JsonProperty("commodityAmount")
    private String commodityAmount;
    @JsonProperty("paymentAmount")
    private String paymentAmount;
    @JsonProperty("buyerSteamRegTime")
    private Integer buyerSteamRegTime;
    @JsonProperty("sellerSteamRegTime")
    private Integer sellerSteamRegTime;
    @JsonProperty("cancelOrderTime")
    private Long cancelOrderTime;
    @JsonProperty("payMethod")
    private Integer payMethod;
    @JsonProperty("steamid")
    private String steamid;
    @JsonProperty("sellerSendOfferVisible")
    private Integer sellerSendOfferVisible;
    @JsonProperty("offerType")
    private Integer offerType;
    @JsonProperty("switchSellersSendOffersTime")
    private Long switchSellersSendOffersTime;
    @JsonProperty("receiveOfferSteamId")
    private Long receiveOfferSteamId;
    @JsonProperty("presenterJoinSteamDate")
    private Integer presenterJoinSteamDate;
    @JsonProperty("tradeUrl")
    private String tradeUrl;

    @NoArgsConstructor
    @Data
    public static class ProductDetailDTO {
        @JsonProperty("commodityId")
        private Integer commodityId;
        @JsonProperty("commodityHashName")
        private String commodityHashName;
        @JsonProperty("commodityName")
        private String commodityName;
        @JsonProperty("commodityTemplateId")
        private Integer commodityTemplateId;
        @JsonProperty("assertId")
        private Long assertId;
        @JsonProperty("abrade")
        private String abrade;
        @JsonProperty("actions")
        private String actions;
        @JsonProperty("iconUrl")
        private String iconUrl;
        @JsonProperty("price")
        private Integer price;
        @JsonProperty("num")
        private Integer num;
        @JsonProperty("paintIndex")
        private Integer paintIndex;
        @JsonProperty("paintSeed")
        private Integer paintSeed;
        @JsonProperty("haveNameTag")
        private Integer haveNameTag;
        @JsonProperty("nameTag")
        private String nameTag;
        @JsonProperty("haveClothSeal")
        private Integer haveClothSeal;
        @JsonProperty("isDoppler")
        private Integer isDoppler;
        @JsonProperty("dopplerColor")
        private String dopplerColor;
        @JsonProperty("isFade")
        private Integer isFade;
        @JsonProperty("isHardened")
        private Integer isHardened;
        @JsonProperty("haveSticker")
        private String haveSticker;
        @JsonProperty("stickerRefreshStatus")
        private String stickerRefreshStatus;
        @JsonProperty("isSpecial")
        private Integer isSpecial;
        @JsonProperty("exteriorColor")
        private String exteriorColor;
        @JsonProperty("exteriorHashName")
        private String exteriorHashName;
        @JsonProperty("exteriorId")
        private Integer exteriorId;
        @JsonProperty("exteriorName")
        private String exteriorName;
        @JsonProperty("itemSetColor")
        private String itemSetColor;
        @JsonProperty("itemSetHashName")
        private String itemSetHashName;
        @JsonProperty("itemSetId")
        private Integer itemSetId;
        @JsonProperty("itemSetName")
        private String itemSetName;
        @JsonProperty("qualityColor")
        private String qualityColor;
        @JsonProperty("qualityHashName")
        private String qualityHashName;
        @JsonProperty("qualityId")
        private Integer qualityId;
        @JsonProperty("qualityName")
        private String qualityName;
        @JsonProperty("rarityColor")
        private String rarityColor;
        @JsonProperty("rarityHashName")
        private String rarityHashName;
        @JsonProperty("rarityId")
        private Integer rarityId;
        @JsonProperty("rarityName")
        private String rarityName;
        @JsonProperty("typeHashName")
        private String typeHashName;
        @JsonProperty("typeIcon")
        private String typeIcon;
        @JsonProperty("typeId")
        private Integer typeId;
        @JsonProperty("typeName")
        private String typeName;
        @JsonProperty("weaponHashName")
        private String weaponHashName;
        @JsonProperty("weaponId")
        private Integer weaponId;
        @JsonProperty("weaponName")
        private String weaponName;
        @JsonProperty("commodityAbrade")
        private String commodityAbrade;
    }
}
