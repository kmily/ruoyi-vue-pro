package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemTag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemResp implements Serializable {
    private String itemName;
    private String shortName;
    private String marketHashName;
    private String itemId;
    private String imageUrl;
    private C5ItemInfo itemInfo;
    private Integer autoPrice;
    private Integer salePrice;
    private List<C5ItemTag> tagList;
    private String autoQuantity;
    /**
     * 参考价
     */
    private Integer referencePrice;
    /**
     * 最低价
     */
    private Integer minPrice;
}
