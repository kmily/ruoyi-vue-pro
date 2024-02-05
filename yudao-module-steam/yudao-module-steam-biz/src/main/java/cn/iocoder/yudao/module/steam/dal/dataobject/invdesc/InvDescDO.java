package cn.iocoder.yudao.module.steam.dal.dataobject.invdesc;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存信息详情 DO
 *
 * @author 芋道源码
 */
@TableName("steam_inv_desc")
@KeySequence("steam_inv_desc_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvDescDO extends BaseDO {

    /**
     * appid
     */
    private Integer appid;
    /**
     * classid
     */
    private String classid;
    /**
     * instanceid
     */
    private String instanceid;
    /**
     * currency
     */
    private Integer currency;
    /**
     * background_color
     */
    private String backgroundColor;
    /**
     * icon_url
     */
    private String iconUrl;
    /**
     * icon_url_large
     */
    private String iconUrlLarge;
    /**
     * descriptions
     */
    private String descriptions;
    /**
     * tradable
     */
    private Integer tradable;
    /**
     * actions
     */
    private String actions;
    /**
     * fraudwarnings
     */
    private String fraudwarnings;
    /**
     * name
     */
    private String name;
    /**
     * name_color
     */
    private String nameColor;
    /**
     * type
     */
    private String type;
    /**
     * market_name
     */
    private String marketName;
    /**
     * market_hash_name
     */
    private String marketHashName;
    /**
     * market_actions
     */
    private String marketActions;
    /**
     * commodity
     */
    private Integer commodity;
    /**
     * market_tradable_restriction
     */
    private Integer marketTradableRestriction;
    /**
     * marketable
     */
    private Integer marketable;
    /**
     * 描述
     */
    private String tags;
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
     * 主键
     */
    @TableId
    private Long id;
    /**
     * steamId
     */
    private String steamId;

}