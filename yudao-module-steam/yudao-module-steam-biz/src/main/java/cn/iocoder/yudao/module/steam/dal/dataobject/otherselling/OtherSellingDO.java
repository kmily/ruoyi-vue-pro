package cn.iocoder.yudao.module.steam.dal.dataobject.otherselling;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 其他平台在售 DO
 *
 * @author 管理员
 */
@TableName("steam_other_selling")
@KeySequence("steam_other_selling_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherSellingDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * csgoid
     */
    private Integer appid;
    /**
     * 资产id(饰品唯一)
     */
    private String assetid;
    /**
     * classid
     */
    private String classid;
    /**
     * instanceid
     */
    private String instanceid;
    /**
     * amount
     */
    private String amount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 出售价格单价分
     */
    private Integer price;
    /**
     * 发货状态(0代表未出售，1代表出售中，2代表已出售 )
     */
    private Integer transferStatus;
    /**
     * contextid
     */
    private String contextid;
    /**
     * 图片地址
     */
    private String iconUrl;
    /**
     * 商品名称
     */
    private String marketName;
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
    private String itemInfo;
    /**
     * short_name
     */
    private String shortName;
    /**
     * 出售用户名字
     */
    private String sellingUserName;
    /**
     * 出售用户头像
     */
    private String sellingAvator;
    /**
     * 出售用户id
     */
    private Integer sellingUserId;
    /**
     * 出售平台id
     */
    private Integer platformIdentity;
    /**
     * steamId
     */
    private String steamId;

}