package cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 提现 DO
 *
 * @author 芋道源码
 */
@TableName("steam_withdrawal")
@KeySequence("steam_withdrawal_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 是否已支付[0未支付，1支付]
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean payStatus;
    /**
     * 支付订单编号
     */
    private Long payOrderId;
    /**
     * 支付成功的支付渠道
     */
    private String payChannelCode;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 退款订单编号
     */
    private Long payRefundId;
    /**
     * 退款金额，单位分
     */
    private Integer refundPrice;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 提现金额
     */
    private Integer price;

}