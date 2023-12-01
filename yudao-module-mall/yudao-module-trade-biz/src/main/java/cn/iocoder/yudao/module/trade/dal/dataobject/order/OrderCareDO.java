package cn.iocoder.yudao.module.trade.dal.dataobject.order;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单医护映射 DO
 *
 * @author 管理人
 */
@TableName("trade_order_care")
@KeySequence("trade_order_care_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCareDO extends BaseDO {

    /**
     * 附加信息编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 医护人员编号
     */
    private Long careId;
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 结束时间
     */
    private LocalDateTime refuseTime;
    /**
     * 拒绝原因
     */
    private String refuse;

}
