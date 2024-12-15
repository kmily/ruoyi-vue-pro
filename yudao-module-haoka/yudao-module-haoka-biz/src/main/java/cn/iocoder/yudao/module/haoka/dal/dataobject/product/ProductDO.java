package cn.iocoder.yudao.module.haoka.dal.dataobject.product;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品/渠道 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_product")
@KeySequence("haoka_product_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO extends BaseDO {

    /**
     * 产品ID
     */
    @TableId
    private Long id;
    /**
     * 运营商
     *
     * 枚举 {@link TODO haoka_operator 对应的类}
     */
    private Integer operator;
    /**
     * 产品编码
     */
    private String sku;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品类型
     */
    private Long haokaProductTypeId;
    /**
     * 归属地
     */
    private Integer belongAreaCode;
    /**
     * 产品渠道
     */
    private Long haokaProductChannelId;
    /**
     * 产品限制
     */
    private Long haokaProductLimitId;
    /**
     * 身份证号码验证
     *
     * 枚举 {@link TODO id_card_num_verify 对应的类}
     */
    private Integer idCardNumVerify;
    /**
     * 身份证图片验证
     *
     * 枚举 {@link TODO id_card_img_verify 对应的类}
     */
    private Integer idCardImgVerify;
    /**
     * 生产地址
     */
    private String produceAddress;
    /**
     * 黑名单过滤
     */
    private Boolean needBlacklistFilter;
    /**
     * 是否启用库存限制
     */
    private Boolean enableStockLimit;
    /**
     * 库存数量
     */
    private Integer stockNum;
    /**
     * 库存报警数量
     */
    private Integer stockWarnNum;
    /**
     * 生产备注
     */
    private String produceRemarks;
    /**
     * 结算规则
     */
    private String settlementRules;
    /**
     * 预估收益
     */
    private String estimatedRevenue;
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