package cn.iocoder.yudao.module.steam.service.youyoucommodity;

import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import liquibase.pro.packaged.S;
import lombok.Data;
import org.checkerframework.checker.units.qual.Mass;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityDTO {
    @JsonProperty("commodityStickers")
    private List<CommodityStickersDTO> commodityStickers;

//    @JsonProperty("commodityDoppler")
//    private List<CommodityDopplerDTO> commodityDoppler;
//
//    @JsonProperty("commodityFade")
//    private List<CommodityFadeDTO> commodityFade;
//
//    @JsonProperty("commodityHardened")
//    private List<CommodityHardenedDTO> commodityHardened;

    // 印花数据
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommodityStickersDTO {

        // 印花Id
        @JsonProperty("stickerId")
        private Integer stickerId;

        //	插槽编号
        @JsonProperty("rawIndex")
        private Integer rawIndex;

        // 印花名称
        @JsonProperty("name")
        private String name;

        // 唯一名称
        @JsonProperty("hashName")
        private String hashName;

        // 材料
        @JsonProperty("material")
        private String material;

        // 图片链接地址
        @JsonProperty("imgUrl")
        private String imgUrl;

        // 印花价格(单位元)
        @JsonProperty("price")
        private String price;

        // 磨损值
        @JsonProperty("abrade")
        private String abrade;
    }

}
