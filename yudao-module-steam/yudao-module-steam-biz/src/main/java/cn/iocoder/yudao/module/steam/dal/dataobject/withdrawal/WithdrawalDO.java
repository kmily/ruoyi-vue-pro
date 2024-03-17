package cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal;

import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.WithdrawalInfo;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "steam_withdrawal",autoResultMap = true)
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
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer userType;
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
     * 是否已支付
     */
    private Boolean payStatus;
    /**
     * 提现金额
     */
    private Integer withdrawalPrice;
    /**
     * 提现信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private WithdrawalInfo withdrawalInfo;
    /**
     * 服务费
     */
    private Integer serviceFee;
    /**
     * 费率
     */
    private String serviceFeeRate;
    /**
     * 支付金额
     */
    private Integer paymentAmount;
    /**
     * 审批状态
     */
    private Integer auditStatus;
    /**
     * 审核人
     */
    private Long auditUserId;
    /**
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer auditUserType;
    /**
     * 审核信息
     */
    private String auditMsg;
    /**
     * 提现手续费收款钱包
     */
    private Long serviceFeeUserId;
    /**
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer serviceFeeUserType;
    /**
     * 支付接口返回
     */
    private String payTransferRet;
    /**
     * 服务费转帐接口返回
     */
    private String serviceFeeRet;
}