package cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单来源配置 DO
 *
 * @author xiongxiong
 */
@TableName("haoka_order_source")
@KeySequence("haoka_order_source_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSourceDO extends BaseDO {

    /**
     * 来源ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 来源备注
     */
    private String sourceRemark;
    /**
     * 渠道ID
     *
     * 枚举 {@link TODO haoka_order_channel 对应的类}
     */
    private Long channel;
    /**
     * 成本更新时间
     */
    private LocalDateTime costUpdatedAt;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 卖家ID
     */
    private Long sellerId;

}