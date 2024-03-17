package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import lombok.Data;

import java.util.List;

@Data
public class SellingMergeListReqVo {
    // 图片
    private String iconUrl;
    //库存表ID
    private List<String> invDescId;
    //库存表ID
    private List<String> invId;
    // 名称
    private String marketName;
    // 哈希name
    private String marketHashName;
    // 状态
    private Integer status;
    // 发货状态
    private Integer transferStatus;
    // 唯一实例id
    private String assetId;
    // 商品数量
    private Integer number;
    // 价格
    private List<Integer> price;

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

    /**
     * 平台最低价
     */
    private Integer minPrice;


    private C5ItemInfo itemInfo;
}
