package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import cn.iocoder.yudao.module.steam.service.fin.v5.res.V5ProductPriceInfoRes;
import lombok.Data;

import java.util.List;

@Data
public class V5QueryOnSaleInfoDO {

        private Integer code;
        private String msg;
        private List<V5ProductData> data;

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
            private Double minSellPrice;
            private String itemIcon;
            private Double selfPrice;
            private Double playerPrice;

        }

}
