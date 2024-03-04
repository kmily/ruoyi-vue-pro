package cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * steam订单 DO
 *
 * @author 管理员
 */
@TableName("steam_youyou_order")
@KeySequence("steam_youyou_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouyouOrderDO extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 价格，单位：分 
     */
    private Integer payAmount;
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
     * 退款金额，单位：分
     */
    private Integer refundPrice;
    /**
     * 退款订单编号
     */
    private Long payRefundId;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 发货信息 json
     */
    private String transferText;
    /**
     * 发货状态
     */
    private Integer transferStatus;
    /**
     * 订单支付状态
     *
     * 枚举 {@link TODO pay_order_status 对应的类}
     */
    private Integer payOrderStatus;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;
    /**
     * 订单号，内容生成
     */
    private String orderNo;
    /**
     * 发货模式 0,卖家直发；1,极速发货
     */
    private Integer shippingMode;
    /**
     *  收货方的Steam交易链接
     */
    private String tradeLinks;
    /**
     * 商品模版ID
     */
    private String commodityTemplateId;
    /**
     * 模板hashname
     */
    private String commodityHashName;
    /**
     * 商品ID
     */
    private String commodityId;
    /**
     * 购买最高价,单价元
     */
    private String purchasePrice;
    /**
     * 实际商品ID
     */
    private String realCommodityId;
    /**
     * 极速发货购买模式0：优先购买极速发货；1：只购买极速发货
     */
    private Integer fastShipping;
    /**
     * 有品订单号
     */
    private String uuOrderNo;
    /**
     * 有品商户订单号
     */
    private String uuMerchantOrderNo;
    /**
     * 交易状态 0,成功；2,失败。
     */
    private Integer uuOrderStatus;

}