package cn.iocoder.yudao.module.pay.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 记账本(商户对商户)转账订单表
 */
@Data
@TableName(value = "steam_alipay_aqf_transfer_order",autoResultMap = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SteamAlipayAqfTransferOrderDO {
    /**
     * 主键ID
     */
    @TableId
    private Integer id;

    /**
     * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一
     */
    private String outBizNo;

    /**
     * 订单总金额，单位为元，精确到小数点后两位
     */
    private BigDecimal transAmount;

    /**
     * 付款方身份类型
     */
    private String payerInfoIdentityType;

    /**
     * 付款方记账本ID
     */
    private String payerInfoIdentity;

    /**
     * 付款方的签约的支付宝协议号
     */
    private String extInfoAgreementNo;

    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示
     */
    private String orderTitle;

    /**
     * 业务备注
     */
    private String remark;

    /**
     * 收款方身份类型
     */
    private String payeeInfoIdentityType;

    /**
     * 收款方标识ID
     */
    private String payeeInfoIdentity;

    /**
     * 收款方真实姓名
     */
    private String payeeInfoName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 当前订单的创建者id
     */
    private Integer createUserId;

    /**
     * 支付宝转账单据号，仅当转账成功才会返回该参数
     */
    private String orderId;

    /**
     * 支付宝支付资金流水号，仅当转账成功才会返回该参数
     */
    private Integer payFundOrderId;

    /**
     * 业务产品码
     */
    private String productCode;

    /**
     * 业务场景码
     */
    private String bizScene;

    /**
     * 支付完成时间
     */
    private Date payDate;

    /**
     * 转账状态
     */
    private Integer status;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 转账失败，支付宝的异常错误信息
     */
    private String payErro;
}