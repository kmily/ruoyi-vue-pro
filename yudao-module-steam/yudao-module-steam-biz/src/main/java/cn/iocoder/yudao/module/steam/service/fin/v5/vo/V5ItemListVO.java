package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class V5ItemListVO {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("totalCount")
    private Integer totalCount;
    @JsonProperty("data")
    private List<DataDTO> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDTO {
        @JsonProperty("marketHashName")
        private String marketHashName;
        @JsonProperty("itemName")
        private String itemName;
        @JsonProperty("shortItemName")
        private String shortItemName;
        @JsonProperty("itemType")
        private String itemType;
        @JsonProperty("itemTypeName")
        private String itemTypeName;
        @JsonProperty("itemRarity")
        private String itemRarity;
        @JsonProperty("itemRarityName")
        private String itemRarityName;
        @JsonProperty("itemExterior")
        private String itemExterior;
        @JsonProperty("itemExteriorName")
        private String itemExteriorName;
        @JsonProperty("itemQuality")
        private String itemQuality;
        @JsonProperty("itemQualityName")
        private String itemQualityName;
        @JsonProperty("onSaleStock")
        private Integer onSaleStock;
        @JsonProperty("minSellPrice")
        private Double minSellPrice;
        @JsonProperty("itemIcon")
        private String itemIcon;

        public String getMarketHashName() {
            return marketHashName;
        }

        public void setMarketHashName(String marketHashName) {
            this.marketHashName = marketHashName;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getShortItemName() {
            return shortItemName;
        }

        public void setShortItemName(String shortItemName) {
            this.shortItemName = shortItemName;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getItemTypeName() {
            return itemTypeName;
        }

        public void setItemTypeName(String itemTypeName) {
            this.itemTypeName = itemTypeName;
        }

        public String getItemRarity() {
            return itemRarity;
        }

        public void setItemRarity(String itemRarity) {
            this.itemRarity = itemRarity;
        }

        public String getItemRarityName() {
            return itemRarityName;
        }

        public void setItemRarityName(String itemRarityName) {
            this.itemRarityName = itemRarityName;
        }

        public String getItemExterior() {
            return itemExterior;
        }

        public void setItemExterior(String itemExterior) {
            this.itemExterior = itemExterior;
        }

        public String getItemExteriorName() {
            return itemExteriorName;
        }

        public void setItemExteriorName(String itemExteriorName) {
            this.itemExteriorName = itemExteriorName;
        }

        public String getItemQuality() {
            return itemQuality;
        }

        public void setItemQuality(String itemQuality) {
            this.itemQuality = itemQuality;
        }

        public String getItemQualityName() {
            return itemQualityName;
        }

        public void setItemQualityName(String itemQualityName) {
            this.itemQualityName = itemQualityName;
        }

        public Integer getOnSaleStock() {
            return onSaleStock;
        }

        public void setOnSaleStock(Integer onSaleStock) {
            this.onSaleStock = onSaleStock;
        }

        public Double getMinSellPrice() {
            return minSellPrice;
        }

        public void setMinSellPrice(Double minSellPrice) {
            this.minSellPrice = minSellPrice;
        }

        public String getItemIcon() {
            return itemIcon;
        }

        public void setItemIcon(String itemIcon) {
            this.itemIcon = itemIcon;
        }
    }
}
