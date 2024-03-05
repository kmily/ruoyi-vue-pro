package cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 悠悠商品列表 DO
 *
 * @author 管理员
 */
@TableName("steam_youyou_commodity")
@KeySequence("steam_youyou_commodity_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouyouCommodityDO extends BaseDO {

    /**
     * 商品id
     */
    @TableId
    private Integer id;
    /**
     * 商品模板id
     */
    private Integer templateId;
    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品价格（单位元）
     */
    private String commodityPrice;
    /**
     * 商品磨损度
     */
    private String commodityAbrade;
    /**
     * 图案模板
     */
    private Integer commodityPaintSeed;
    /**
     * 皮肤编号
     */
    private Integer commodityPaintIndex;
    /**
     * 是否有名称标签：0否1是
     */
    private Integer commodityHaveNameTag;
    /**
     * 是否有布章：0否1是
     */
    private Integer commodityHaveBuzhang;
    /**
     * 是否有印花：0否1是
     */
    private Integer commodityHaveSticker;
    /**
     * 发货模式：0,卖家直发；1,极速发货
     */
    private Integer shippingMode;
    /**
     * 是否渐变色：0否1是
     */
    private Integer templateisFade;
    /**
     * Integer	是否表面淬火：0否1是
     */
    private Integer templateisHardened;
    /**
     * 是否多普勒：0否1是
     */
    private Integer templateisDoppler;
    /**
     * 印花Id
     */
    private Integer commodityStickersStickerId;
    /**
     * 插槽编号
     */
    private Integer commodityStickersRawIndex;
    /**
     * 印花名称
     */
    private String commodityStickersName;
    /**
     * 唯一名称
     */
    private String commodityStickersHashName;
    /**
     * 材料
     */
    private String commodityStickersMaterial;
    /**
     * 图片链接地址
     */
    private String commodityStickersImgUrl;
    /**
     * 印花价格(单位元)
     */
    private String commodityStickersPrice;
    /**
     * 磨损值
     */
    private String commodityStickersAbrade;
    /**
     * 多普勒属性分类名称
     */
    private String commodityDopplerTitle;
    /**
     * 多普勒属性分类缩写
     */
    private String commodityDopplerAbbrTitle;
    /**
     * 多普勒属性显示颜色
     */
    private String commodityDopplerColor;
    /**
     * 渐变色属性属性名称
     */
    private String commodityFadeTitle;
    /**
     * 渐变色属性对应数值
     */
    private String commodityFadeNumerialValue;
    /**
     * 渐变色属性显示颜色
     */
    private String commodityFadeColor;
    /**
     * 表面淬火属性分类名称
     */
    private String commodityHardenedTitle;
    /**
     * 表面淬火属性分类缩写
     */
    private String commodityHardenedAbbrTitle;
    /**
     * 表面淬火属性显示颜色
     */
    private String commodityHardenedColor;
    /**
     * 发货状态
     */
    private String transferStatus;

}