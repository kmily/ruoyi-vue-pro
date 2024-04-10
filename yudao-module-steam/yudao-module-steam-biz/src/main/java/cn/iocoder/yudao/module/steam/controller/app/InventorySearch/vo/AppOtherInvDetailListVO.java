package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class AppOtherInvDetailListVO {
    private boolean success;

    private Data__ data;

    @Data
    public static class Data__{
        // 外观
        private Long total;

        // 外观名称
        private Long pages;

        // 图片
        private Long page;

        // 图片
        private Long limit;

        // 价格和在售数量信息
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        private List<CommodityInfo> list;

        @Data
        public static class CommodityInfo {

            // 自动发货在售数量
            private Long acceptBargain;

            // 自动发货在售最低价
            private Long appId;

            // 自动发货在售数量
            private String appName;

            private AssetInfo assetInfo;

            @Data
            public static class AssetInfo {
                private Long assetId;
                private Long classId;
                private Long classInfoId;
                private String ext;
                private String fadeColor;
                private String fraudwarning;

                @JsonFormat(shape = JsonFormat.Shape.OBJECT)
                private List<Gems> gems;

                @Data
                public static class Gems {
                    private String border;
                    private String enName;
                    private String gemEnType;
                    private String gemType;
                    private Long id;
                    private String image;
                    private Long itemId;
                    private String name;
                    private String type;
                    private Float value;
                }

                private Float gradient;
                private String inspectImageUrl;
                private Long instanceId;
                @JsonFormat(shape = JsonFormat.Shape.OBJECT)
                private List<ItemSets> itemSets;

                @Data
                public static class ItemSets {
                    private String hashName;
                    private String imageUrl;
                    private Long isItemSet;
                    private Long itemId;
                    private String name;
                    private Float price;
                }

                private String lastStyle;
                private String levelColor;
                private String levelName;
                private Long paintIndex;
                private Long paintSeed;
                @JsonFormat(shape = JsonFormat.Shape.OBJECT)
                private List<Stickers> stickers;

                @Data
                public static class Stickers {
                    private String enName;
                    private Long id;
                    private String image;
                    private Long itemId;
                    private String name;
                    private Float price;
                    private Long slot;
                    private Long stickerId;
                    private Long type;
                    private Float wear;
                }

                private Long styleId;
                private Stickers styleProgress;
                @JsonFormat(shape = JsonFormat.Shape.OBJECT)
                private List<Styles> styles;

                @Data
                public static class Styles {
                    private String color;
                    private boolean locked;
                    private String styleEnName;
                    private Long styleId;
                    private String styleName;
                }

                private Float wear;
            }

            private String classInfoId;
            private Float cnyPrice;
            private Long compensateType;
            private Long delivery;
            private String description;
            private Long id;
            private String imageUrl;
            private String inspect3dUrl;
            private Long inspect3dViewable;
            private String inspectImageUrl;
            private String inspectOriginalUrl;
            private String inspectUrl;
            private Long inspectViewable;
            private Long inspectable;
            private Long isCollection;
            private Long itemId;
            @JsonFormat(shape = JsonFormat.Shape.OBJECT)
            private ItemInfo itemInfo;

            @Data
            public static class ItemInfo {
                private String category;
                private String categoryName;
                private String customPlayer;
                private String customPlayerName;
                private String exterior;
                private String exteriorColor;
                private String exteriorName;
                private String hero;
                private String heroAvatar;
                private String heroName;
                private String item;
                private String itemName;
                private String itemSet;
                private String itemSetName;
                private String patchCapsule;
                private String patchCapsuleName;
                private String quality;
                private String qualityColor;
                private String qualityName;
                private String rarity;
                private String rarityColor;
                private String rarityName;
                private String slot;
                private String slotName;
                private String stickerCapsule;
                private String stickerCapsuleName;
                private String type;
                private String typeName;
                private String weapon;
                private String weaponName;
            }

            private String itemName;
            private String marketHashName;
            private Float price;
            @JsonFormat(shape = JsonFormat.Shape.OBJECT)
            private SellerInfo sellerInfo;

            @Data
            public static class SellerInfo {
                private String avatar;
                @JsonFormat(shape = JsonFormat.Shape.OBJECT)
                private DeliveryInfo deliveryInfo;

                @Data
                public static class DeliveryInfo {
                    private Day15 day15;

                    @Data
                    public static class Day15 {
                        private String deliveryAvgTime;
                        private Long deliveryNoneNum;
                        private String deliverySuccessRate;
                    }

                    private Day7 day7;

                    @Data
                    public static class Day7 {
                        private String deliveryAvgTime;
                        private Long deliveryNoneNum;
                        private String deliverySuccessRate;
                    }

                }

                private Long lastActive;
                private String nickname;
                private Long platformId;
                private Long thirdUserId;
                private Long userId;
                private Long verified;


            }

            private Float sellerPrice;
            private Float subsidyPrice;
            private Long systemTime;
            private String token;
            private Long wearRank;

        }

    }

    private Long errorCode;
    private Object errorData;
    private String errorMsg;

}


