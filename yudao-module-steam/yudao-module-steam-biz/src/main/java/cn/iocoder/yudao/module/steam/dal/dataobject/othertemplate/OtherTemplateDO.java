package cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 其他平台模板 DO
 *
 * @author 管理员
 */
@TableName("steam_other_template")
@KeySequence("steam_other_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherTemplateDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 出售平台id
     */
    private Integer platformIdentity;
    /**
     * 外观
     */
    private String exterior;
    /**
     * 外观名称
     */
    private String exteriorName;
    /**
     * 饰品id
     */
    private Integer itemId;
    /**
     * 饰品名称
     */
    private String itemName;
    /**
     * marketHashName
     */
    private String marketHashName;
    /**
     * 自动发货在售最低价
     */
    private Float autoDeliverPrice;
    /**
     * 品质
     */
    private String quality;
    /**
     * 稀有度
     */
    private String rarity;
    /**
     * steam类型
     */
    private String type;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 自动发货在售数量
     */
    private Integer autoDeliverQuantity;
    /**
     * 品质颜色
     */
    private String qualityColor;
    /**
     * 品质名称
     */
    private String qualityName;
    /**
     * 稀有度颜色
     */
    private String rarityColor;
    /**
     * 稀有度名称
     */
    private String rarityName;
    /**
     * 短名称，去掉前缀
     */
    private String shortName;
    /**
     * steam类型名称
     */
    private String typeName;

}