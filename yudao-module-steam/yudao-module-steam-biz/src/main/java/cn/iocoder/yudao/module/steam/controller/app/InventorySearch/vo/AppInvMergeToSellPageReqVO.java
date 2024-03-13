package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import lombok.Data;
@Data
public class AppInvMergeToSellPageReqVO {

    // 图片
    private String iconUrl;
    // 名称
    private String marketName;
    // 状态
    private Integer status;
    // 发货状态
    private Integer transferStatus;
    // 唯一实例id
    private String assetId;
    // 商品数量
    private Integer number;
    // 价格
    private Integer price;

    // 分类查询字段
    /**
     * 类别选择
     */
    private String selQuality;
    /**
     * 收藏品选择
     */
    private String selItemset;
    /**
     * 武器选择
     */
    private String selWeapon;
    /**
     * 外观选择
     */
    private String selExterior;
    /**
     * 品质选择
     */
    private String selRarity;
    /**
     * 类型选择
     */
    private String selType;

}
