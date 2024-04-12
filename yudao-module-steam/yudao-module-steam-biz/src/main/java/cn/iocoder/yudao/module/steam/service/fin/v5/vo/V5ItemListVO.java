package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

@Data
public class V5ItemListVO {

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
