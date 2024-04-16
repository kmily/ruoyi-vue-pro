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
    private String quantity;
    /**
     * 参考价
     */
    private Integer referencePrice;
    /**
     * 最低价
     */
    private Integer minPrice;
    private String selQuality; // 类别选择
    private String selItemset; // 收藏品选择
    private String selWeapon; // 武器选择
    private String selExterior; // 外观选择
    private String selRarity; // 品质选择
    private String selType; // 类型选择
    private Boolean existInv; // 是否存在库存
}
