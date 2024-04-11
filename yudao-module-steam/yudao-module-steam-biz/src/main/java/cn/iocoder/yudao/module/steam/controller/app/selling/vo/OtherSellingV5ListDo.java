package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import lombok.Data;

import java.util.List;

@Data
public class OtherSellingV5ListDo {

    private Integer code;

    private String msg;

    private List<Data_> data;

    @Data
    public static class Data_ {


        // 自动发货在售最低价
        private Integer id;

        // 自动发货在售数量
        private Float consultPrice;

        // 品质
        private Integer onSaleCount;

        // 品质颜色
        private String itemImg;

        // 品质名称
        private String marketHashName;

        // 短名称，去掉前缀
        private String itemName;

        // 类型
        private String itemExterior;

        // 类型名称
        private String itemExteriorName;

        // 自动发货在售最低价
        private String itemRarity;

        // 自动发货在售数量
        private String itemRarityName;

        // 品质
        private String itemQuality;

        // 品质颜色
        private String itemQualityName;

        // 品质名称
        private String askCount;

        // 短名称，去掉前缀
        private Boolean isPreViewAble;

        private String itemTypeName;

        // 类型名称
        private Float minRentPrice;

        // 自动发货在售最低价
        private Float minForegiftPrice;

        // 自动发货在售数量
        private Integer num;

        // 品质
        private Integer onRentCount;

        // 品质颜色
        private Float phaseDiffPrice;

        // 品质名称
        private String phaseDiffPerCent;

        // 短名称，去掉前缀
        private List<StickersList> stickersList;

        @Data
        public static class StickersList{
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

        private String floatValue;

        // 自动发货在售数量
        private String assetId;

        // 品质
        private String marketId;

        // 品质颜色
        private Integer userId;
    }

    private Integer totalCount;

    private Integer sum;

    private Integer num;

}
