package cn.iocoder.yudao.module.steam.controller.app.vo.api;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ExcelIgnoreUnannotated
public class AppBatchGetOnSaleCommodityInfoRespVO implements Serializable {
    @JsonProperty("saleTemplateResponse")
    private SaleTemplateResponseDTO saleTemplateResponse;
    @JsonProperty("saleCommodityResponse")
    private SaleCommodityResponseDTO saleCommodityResponse;

    public SaleTemplateResponseDTO getSaleTemplateResponse() {
        return saleTemplateResponse;
    }

    public void setSaleTemplateResponse(SaleTemplateResponseDTO saleTemplateResponse) {
        this.saleTemplateResponse = saleTemplateResponse;
    }

    public SaleCommodityResponseDTO getSaleCommodityResponse() {
        return saleCommodityResponse;
    }

    public void setSaleCommodityResponse(SaleCommodityResponseDTO saleCommodityResponse) {
        this.saleCommodityResponse = saleCommodityResponse;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleTemplateResponseDTO {
        @JsonProperty("templateHashName")
        private String templateHashName;
        @JsonProperty("iconUrl")
        private String iconUrl;
        @JsonProperty("exteriorName")
        private String exteriorName;
        @JsonProperty("rarityName")
        private String rarityName;
        @JsonProperty("qualityName")
        private String qualityName;


        public String getTemplateHashName() {
            return templateHashName;
        }

        public void setTemplateHashName(String templateHashName) {
            this.templateHashName = templateHashName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getExteriorName() {
            return exteriorName;
        }

        public void setExteriorName(String exteriorName) {
            this.exteriorName = exteriorName;
        }

        public String getRarityName() {
            return rarityName;
        }

        public void setRarityName(String rarityName) {
            this.rarityName = rarityName;
        }

        public String getQualityName() {
            return qualityName;
        }

        public void setQualityName(String qualityName) {
            this.qualityName = qualityName;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleCommodityResponseDTO {
        @JsonProperty("minSellPrice")
        private Integer minSellPrice;
        @JsonProperty("sellNum")
        private Integer sellNum;

        public Integer getMinSellPrice() {
            return minSellPrice;
        }

        public void setMinSellPrice(Integer minSellPrice) {
            this.minSellPrice = minSellPrice;
        }

        public Integer getSellNum() {
            return sellNum;
        }

        public void setSellNum(Integer sellNum) {
            this.sellNum = sellNum;
        }
    }
}