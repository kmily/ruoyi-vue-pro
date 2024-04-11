package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import lombok.Data;

import java.util.List;

@Data
public class OtherSellingV5DetailListVDo {
    private Integer code;

    private String msg;

    private List<Data_> data;

    @Data
    public static class Data_ {


        // 自动发货在售最低价
        private Integer id;

        private WeaponInfo weaponInfo;

        @Data
        public static class WeaponInfo {

            // 自动发货在售数量
            private String previewUrl;

            // 品质
            private String itemDesc;

            // 品质颜色
            private String itemName;

            // 品质名称
            private String itemTypeName;

            // 短名称，去掉前缀
            private String itemRarityName;

            // 类型
            private String itemQualityName;

            // 类型名称
            private String fraudWarnings;

            // 自动发货在售最低价
            private String floatValue;

            // 自动发货在售数量
            private String paintSeed;

            // 品质
            private String paintIndex;

            // 品质颜色
            private String phaseName;

            // 品质名称
            private String assetId;

            // 短名称，去掉前缀
            private String assetIds;

            // 品质颜色
            private String itemImg;

            // 品质名称
            private String salePrice;

            // 短名称，去掉前缀
            private String marketHashName;

            private Boolean isPreViewAble;

        }

        private UserInfo userInfo;

        @Data
        public static class UserInfo {
            private Integer id;

            // 类型名称
            private String nickname;

            // 自动发货在售最低价
            private String shopName;

            // 自动发货在售数量
            private String account;

            // 品质
            private String accountType;

            // 品质颜色
            private String profilePhotoUrl;

            // 品质名称
            private String availableBalance;

            // 短名称，去掉前缀
            private String blockedBalance;

            private String withdrewBalance;

            // 自动发货在售数量
            private String bagCoupons;

            // 品质
            private String steamId;

            // 品质颜色
            private String tradeUrl;


            private String openKey;

            // 品质颜色
            private String apiKey;

            // 品质名称
            private String status;

            // 短名称，去掉前缀
            private String promoteCode;

            private String superiorId;

            // 自动发货在售数量
            private String loginSuccessCookie;

            // 品质
            private String registerAt;

            // 品质颜色
            private Boolean isRealName;

            private Boolean isLoginSteam;


            private Boolean isVip;

            // 品质颜色
            private String vipEndTime;

            // 品质名称
            private String userSignType;

            // 短名称，去掉前缀
            private String isDeposit;

            private String steamCreateAt;

            // 自动发货在售数量
            private String steamImg;


        }


        private List<StickersList> stickersList;

        @Data
        public static class StickersList {
            private String name;


            private Float wear;

            // 品质颜色
            private String marketHashName;

            // 品质名称
            private String slot;

            // 短名称，去掉前缀
            private String stickerPattern;

            private Integer stickerId;


        }

        private Boolean isPreViewAble;

        // 自动发货在售数量
        private Boolean isHasItemExterior;

        // 品质
        private Boolean isCollection;

        // 品质颜色
        private String type;

        private Float salePrice;


        private Boolean isDicker;

        // 品质颜色
        private Boolean isCounteroffer;

        // 品质名称
        private Integer isAutoPrice;

        // 短名称，去掉前缀
        private String itemDetailImg;

        private String gradualTag;

        // 自动发货在售数量
        private String hardeningTag;


    }

    private Integer totalCount;

    private Integer sum;

    private Integer num;
}
