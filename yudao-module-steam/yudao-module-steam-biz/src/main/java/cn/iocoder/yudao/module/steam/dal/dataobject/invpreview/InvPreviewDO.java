package cn.iocoder.yudao.module.steam.dal.dataobject.invpreview;

import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemTag;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
//import jdk.internal.instrumentation.TypeMapping;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 饰品在售预览 DO
 *
 * @author 芋道源码
 */
@TableName(value = "steam_inv_preview",autoResultMap = true)
@KeySequence("steam_inv_preview_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvPreviewDO extends BaseDO {


    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * price
     */
    private String price;
    /**
     * quantity
     */
    private String quantity;
    /**
     * deals
     */
    private String deals;
    /**
     * item_id
     */
    private Long itemId;
    /**
     * app_id
     */
    private Integer appId;
    /**
     * itemName
     */
    private String itemName;
    /**
     * shortName
     */
    private String shortName;
    /**
     * marketHashName
     */
    private String marketHashName;
    /**
     * imageUrl
     */
    private String imageUrl;
    /**
     * itemInfo
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private C5ItemInfo itemInfo;
    /**
     * sellType
     */
    private String sellType;
    /**
     * currencyId
     */
    private String currencyId;
    /**
     * cnyPrice
     */
    private String cnyPrice;
    /**
     * salePrice
     */
    private String salePrice;
    /**
     * subsidyPrice
     */
    private String subsidyPrice;
    /**
     * activityTag
     */
    private String activityTag;
    /**
     * tagList
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<C5ItemTag> tagList;
    /**
     * subsidyTag
     */
    private String subsidyTag;
    /**
     * 自动发货价格
     */
    private String autoPrice;
    /**
     * 自动发货数量
     */
    private String autoQuantity;
    /**
     * 参考价
     */
    private String referencePrice;
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
     * 是否存在库存
     */
    private Boolean existInv;


}