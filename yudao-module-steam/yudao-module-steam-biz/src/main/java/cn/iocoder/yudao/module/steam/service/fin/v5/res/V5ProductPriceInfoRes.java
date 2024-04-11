package cn.iocoder.yudao.module.steam.service.fin.v5.res;


import lombok.Data;

import java.util.List;


@Data
public class V5ProductPriceInfoRes {

    // 定义返回数据的结构类
    @Data
    public static class V5ProductPriceInfoResponse {
        private Integer code;
        private String msg;
        private List<V5ProductData> data;

        // 内部类，表示商品品数据
        @Data
        public static class V5ProductData {
            private String marketHashName;
            private String itemName;
            private String shortItemName;
            private String itemType;
            private String itemTypeName;
            private String itemRarity;
            private String itemRarityName;
            private String itemExterior;
            private String itemExteriorName;
            private String itemQuality;
            private String itemQualityName;
            private Integer onSaleStock;
            private Integer minSellPrice;
            private String itemIcon;
            private Integer selfPrice;
            private Integer playerPrice;

        }
    }
}