package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import lombok.*;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellingHotDO {
    /**
     * 在售数量
     */
    private Integer sellNum;
    /**
     * 出售价格单价分
     */
    private Integer price;
    /**
     * 商品名称
     */
    private String marketName;
    /**
     * 图片地址
     */
    private String iconUrl;
    /**
     * 类别选择
     */
    private String selQuality;
    /**
     * 外观选择
     */
    private String selExterior;
    /**
     * marketHashName
     */
    private String marketHashName;
    /**
     * 在售展示权重
     */
    private Integer displayWeight;
    /**
     * itemInfo
     */
    private C5ItemInfo itemInfo;

}
