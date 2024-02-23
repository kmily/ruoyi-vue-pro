package cn.iocoder.yudao.module.steam.dal.dataobject.invorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.steam.service.steam.TransferMsg;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;

/**
 * steam订单 DO
 *
 * @author 芋道源码
 */
@TableName(value = "steam_inv_order",autoResultMap = true)
@KeySequence("steam_inv_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvOrderDO extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 是否已支付：[0:未支付 1:已经支付过]
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
     * 退款金额，单位：分
     */
    private Integer refundPrice;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 价格，单位：分 
     */
    private Integer price;
    /**
     * 库存表ID参考steam_inv
     */
    private Long invId;
    /**
     * 用户类型
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer userType;
    /**
     * 购买的steamId
     */
    private String steamId;
    /**
     * 发货信息 json
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TransferMsg transferText;
    /**
     * 发货状态
     * 枚举 {@link cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum  对应的类}
     */
    private Integer transferStatus;

}