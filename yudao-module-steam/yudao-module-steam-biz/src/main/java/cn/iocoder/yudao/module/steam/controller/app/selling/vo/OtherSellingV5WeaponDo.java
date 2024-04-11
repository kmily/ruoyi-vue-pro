package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import lombok.Data;

import java.util.List;

@Data
public class OtherSellingV5WeaponDo {
    private Integer code;

    private String msg;

    private Data_ data;

    @Data
    public static class Data_ {
        private Boolean isHasPhase;

        // 自动发货在售数量
        private String phaseList;

        // 品质
        private List<CommonItemList> commonItemList;

        @Data
        public static class CommonItemList{
            private String name;

            // 自动发货在售数量
            private String marketHashName;

            private Float consultPrice;

            // 自动发货在售数量
            private String type;


        }
        private ItemInfo itemInfo;

        @Data
        public static class ItemInfo {

            // 自动发货在售数量
            private String marketHashName;

            // 品质
            private String shortHashName;

            // 品质颜色
            private String steamPreviewUrl;

            // 品质名称
            private String comeForm;

            // 短名称，去掉前缀
            private String comeFromTag;

            // 类型
            private String comeFormName;

            // 类型名称
            private Float consultPrice;

            // 自动发货在售最低价
            private Float minSalePrice;

            // 自动发货在售数量
            private Float maxSalePrice;

            // 品质
            private Float steamConsultPrice;

            // 品质颜色
            private String changeMarketHashName;

            // 品质名称
            private Float changeConsultPrice;

            // 短名称，去掉前缀
            private Integer onSaleCount;

            // 品质颜色
            private Integer onRentCount;

            // 品质名称
            private Integer askCount;

            // 短名称，去掉前缀
            private String itemName;

            private String itemPhase;

            private String itemDesc;

            // 短名称，去掉前缀
            private String itemImg;

            // 品质颜色
            private String itemType;

            // 品质名称
            private String itemTypeName;

            // 短名称，去掉前缀
            private String itemWeapon;

            private String itemWeaponName;

            private String itemRarity;

            // 品质名称
            private String itemRarityName;

            // 短名称，去掉前缀
            private String itemExterior;

            private String itemExteriorName;

            private String itemQuality;

            // 短名称，去掉前缀
            private String itemQualityName;

            // 品质颜色
            private String shortItemName;

            // 品质名称
            private String steamPriceRMB;

            // 短名称，去掉前缀
            private String steamPriceUS;

        }
        private List<String> collect;

        private List<StatTrakItemList> statTrakItemList;

        @Data
        public static class StatTrakItemList{
            private String name;

            // 自动发货在售数量
            private String marketHashName;

            private Float consultPrice;

            // 自动发货在售数量
            private String type;


        }
        private Boolean isSticker0;

        // 自动发货在售数量
        private List<StickerList> stickerList;
        @Data
        public static class StickerList{
            private String name;

            // 自动发货在售数量
            private String marketHashName;

            private Float consultPrice;

            // 自动发货在售数量
            private String type;


        }
        // 品质
        private Boolean isOther;

        // 品质颜色
        private String type;


        private List<String> itemQualityNameList;
    }


}
