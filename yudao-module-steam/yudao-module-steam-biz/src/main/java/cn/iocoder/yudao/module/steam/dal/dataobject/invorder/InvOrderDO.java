package cn.iocoder.yudao.module.steam.dal.dataobject.invorder;

import cn.iocoder.yudao.module.steam.service.steam.TransferMsg;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

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
     * 购买的steamId
     */
    private String steamId;
    /**
     * 是否已支付：[0:未支付 1:已经支付过]
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
    private Integer refundAmount;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 价格，单位：分
     */
    private Integer paymentAmount;
    /**
     * 用户类型
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer userType;
    /**
     * 订单支付状态
     */
    private Integer payOrderStatus;
    /**
     * 服务费，单位分
     */
    private Integer serviceFee;
    /**
     * 服务费率
     */
    private String serviceFeeRate;
    /**
     * 优惠金额 分
     */
    private Integer discountAmount;
    /**
     * 发货信息 json
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TransferMsg transferText;
    /**
     * 发货状态
     * 枚举 {@link cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum 对应的类}
     */
    private Integer transferStatus;
    /**
     * 库存表ID参考steam_sell
     */
    private Long sellId;
    /**
     * 商品描述ID
     */
    private Long invDescId;
    /**
     * 库存表ID
     */
    private Long invId;
    /**
     * 卖家用户类型
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer sellUserType;
    /**
     * 卖家ID
     */
    private Long sellUserId;
    /**
     * 卖家金额状态
     * 枚举 {@link cn.iocoder.yudao.module.steam.service.steam.InvSellCashStatusEnum 对应的类}
     */
    private Integer sellCashStatus;
    /**
     * 商品总额
     */
    private Integer commodityAmount;
    /**
     * 提现手续费收款钱包
     */
    private Long serviceFeeUserId;
    /**
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer serviceFeeUserType;
    /**
     * 服务费转帐接口返回
     */
    private String serviceFeeRet;
    /**
     * 卖家收货转帐返回
     */
    private String sellCashRet;
    /**
     * 购买平台
     * 枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatFormEnum 对应的类}
     */
    private String platformName;
    /**
     * 购买平台代码
     * 枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatFormEnum 对应的类}
     */
    private Integer platformCode;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String merchantNo;
    /**
     * 交易失败时退还
     */
    private Integer transferRefundAmount;
    /**
     * 交易违约金
     */
    private Integer transferDamagesAmount;
    /**
     * 交易违约判定时间
     */
    private LocalDateTime transferDamagesTime;
    /**
     * 交易违约判定时间
     */
    private String transferDamagesRet;

    /**
     * 卖家steamId
     */
    private String sellSteamId;
    /**
     * 商品名称
     */
    private String marketName;

    private Long sellBindUserId;
    private Long buyBindUserId;

}