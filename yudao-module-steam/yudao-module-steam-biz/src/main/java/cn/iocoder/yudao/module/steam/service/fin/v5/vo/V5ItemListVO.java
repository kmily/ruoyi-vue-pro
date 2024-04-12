package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

import java.util.List;

@Data
public class V5ItemListVO {

    private Integer code;
    private String msg;
    private List<V5Item> data;

    @Data
    public static class V5Item {
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
    }
}
