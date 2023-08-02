package cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 砍价 DO
 *
 * @author WangBosheng
 */
@TableName("promotion_bargain_activity")
@KeySequence("promotion_bargain_activity_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BargainActivityDO extends BaseDO {

    /**
     * 砍价活动编号
     */
    @TableId
    private Long id;
    /**
     * 关联商品ID
     */
    private Long spuId;
    /**
     * 砍价商品表skuID
     */
    private Long bargainSkuId;
    /**
     * 砍价活动名称
     */
    private String name;
    /**
     * 砍价开启时间
     */
    private Integer startTime;
    /**
     * 砍价结束时间
     */
    private Integer endTime;
    /**
     * 砍价状态 0(关闭)  1(开启)
     *
     * 枚举 {@link TODO infra_config_type 对应的类}
     */
    private Byte status;
    /**
     * 用户每次砍价的最大金额
     */
    private Integer bargainMaxPrice;
    /**
     * 用户每次砍价的最小金额
     */
    private Integer bargainMinPrice;
    /**
     * 用户帮砍的次数
     */
    private Integer bargainCount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 砍价有效时间
     */
    private LocalDateTime bargainEffectiveTime;
    /**
     * 砍价商品最低价
     */
    private BigDecimal minPrice;
    /**
     * 砍价起始金额
     */
    private Integer bargainFirstPrice;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 用户帮砍的次数
     */
    private Integer peopleNum;

}
