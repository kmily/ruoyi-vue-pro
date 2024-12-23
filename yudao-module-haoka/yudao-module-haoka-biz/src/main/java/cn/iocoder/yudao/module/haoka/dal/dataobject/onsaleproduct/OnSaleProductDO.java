package cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 在售产品 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_on_sale_product")
@KeySequence("haoka_on_sale_product_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSaleProductDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 产品
     */
    private Long parentProductId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商家编码
     */
    private String sku;
    /**
     * 商品注意点
     */
    private String precautions;
    /**
     * 卖点，使用逗号隔开
     */
    private String sellingPoints;
    /**
     * 承接佣金规则
     */
    private String acceptanceRules;
    /**
     * 结算要求
     */
    private String settlementRequirement;
    /**
     * 佣金结算规则（内部）
     */
    private String settlementRulesInner;
    /**
     * 销售页上传照片
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean needSaleUploadImage;
    /**
     * 产品主图
     */
    private String mainImg;
    /**
     * 商品分享图
     */
    private String shareImg;
    /**
     * 商品详情
     */
    private String detail;
    /**
     * 其他备注
     */
    private String otherNote;
    /**
     * 月租
     */
    private String monthlyRent;
    /**
     * 语言通话
     */
    private String voiceCall;
    /**
     * 通用流量
     */
    private String universalTraffic;
    /**
     * 定向流量
     */
    private String targetedTraffic;
    /**
     * 归属地
     */
    private String belongArea;
    /**
     * 套餐详情
     */
    private String packageDetails;
    /**
     * 套餐优惠期
     */
    private Integer packageDiscountPeriod;
    /**
     * 优惠期起始时间:当月，次月，三月
     */
    private Integer packageDiscountPeriodStart;
    /**
     * 上架
     */
    private Boolean onSale;
    /**
     * 是否顶置
     */
    private Boolean isTop;
    /**
     * 部门ID
     */
    private Long deptId;

}