package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 指定商品购买入参
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiUUCommodityVo implements Serializable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("templateId")
    private Integer templateId;
    @JsonProperty("commodityName")
    private String commodityName;
    @JsonProperty("commodityPrice")
    private String commodityPrice;
    @JsonProperty("commodityAbrade")
    private String commodityAbrade;
    @JsonProperty("commodityPaintSeed")
    private Integer commodityPaintSeed;
    @JsonProperty("commodityPaintIndex")
    private Integer commodityPaintIndex;
    @JsonProperty("commodityHaveNameTag")
    private Integer commodityHaveNameTag;
    @JsonProperty("commodityHaveBuZhang")
    private Integer commodityHaveBuZhang;
    @JsonProperty("commodityHaveSticker")
    private Integer commodityHaveSticker;
    @JsonProperty("commodityStickers")
    private List<?> commodityStickers;
    @JsonProperty("templateIsFade")
    private Integer templateIsFade;
    @JsonProperty("templateIsHardened")
    private Integer templateIsHardened;
    @JsonProperty("templateIsDoppler")
    private Integer templateIsDoppler;
}
