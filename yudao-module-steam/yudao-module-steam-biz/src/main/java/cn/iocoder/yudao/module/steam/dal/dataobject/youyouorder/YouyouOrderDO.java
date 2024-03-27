package cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder;

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
     * 用户编号
     */
    private Long buyUserId;
    /**
     * 用户类型
     */
    private Integer buyUserType;
    /**
     * 买方绑定用户
     */
    private Long buyBindUserId;
    /**
     * 购买的steamId
     */
    private String buySteamId;
    /**
     *  收货方的Steam交易链接
     */
    private String buyTradeLinks;
    /**
     * 卖家用户ID
     */
    private Long sellUserId;
    /**
     * 卖家用户类型
     */
    private Integer sellUserType;
    /**
     * 卖方绑定用户ID
     */
    private Long sellBindUserId;
    /**
     * 卖家steamId
     */
    private String sellSteamId;
    /**
     * 资金付款信息
     */
    private String sellCashRet;
    /**
     * 收款状态
     */
    private Integer sellCashStatus;
    /**
     * 订单号，内容生成
     */
    private String orderNo;
    /**
     * 商品模版ID
     */
    private String commodityTemplateId;
    /**
     * 实际商品ID
     */
    private String realCommodityId;
    /**
     * 商品ID
     */
    private String commodityId;
    /**
     * 商品总额
     */
    private Integer commodityAmount;
    /**
     * 极速发货购买模式0：优先购买极速发货；1：只购买极速发货
     */
    private Integer fastShipping;
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
     * 价格，单位：分 
     */
    private Integer payAmount;
    /**
     * 是否已支付：[0:未支付 1:已经支付过]
     */
    private Boolean payStatus;
    /**
     * 订单支付状态
     */
    private Integer payOrderStatus;
    /**
     * 提现手续费收款人类型
     */
    private Integer serviceFeeUserType;
    /**
     * 服务费，单位分
     */
    private Integer serviceFee;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;
    /**
     * 模板hashname
     */
    private String commodityHashName;
    /**
     * 违约付款接口返回
     */
    private String transferDamagesRet;
    /**
     * 违约金
     */
    private Integer transferDamagesAmount;
    /**
     * 发货状态
     */
    private Integer transferStatus;
    /**
     * 交易失败时退还
     */
    private Integer transferRefundAmount;
    /**
     * 服务费率
     */
    private String serviceFeeRate;
    /**
     * 提现手续费收款钱包
     */
    private Long serviceFeeUserId;
    /**
     * 交易违约判定时间
     */
    private LocalDateTime transferDamagesTime;
    /**
     * 商品名称
     */
    private String marketName;
    /**
     * 发货信息 json
     */
    private String transferText;
    /**
     * 购买最高价,单价元
     */
    private String purchasePrice;
    /**
     * 转帐接口返回
     */
    private String serviceFeeRet;
    /**
     * 发货模式 0,卖家直发；1,极速发货
     */
    private Integer shippingMode;
    /**
     * 有品商户订单号
     */
    private String uuMerchantOrderNo;
    /**
     * 购买用户编号。
     */
    private Integer uuBuyerUserId;
    /**
     * 订单失败原因编号。
     */
    private Integer uuFailCode;
    /**
     * 订单失败原因提示信息。
     */
    private String uuFailReason;
    /**
     * 有品订单号
     */
    private String uuOrderNo;
    /**
     * 通知类型描述(等待发货，等待收货，购买成功，订单取消)。
     */
    private String uuNotifyDesc;
    /**
     * 通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。
     */
    private Integer uuNotifyType;
    /**
     * 交易状态 0,成功；2,失败。
     */
    private Integer uuOrderStatus;
    /**
     * 订单小状态。
     */
    private Integer uuOrderSubStatus;
    /**
     * 预留字段
     */
    private Integer uuOrderSubType;
    /**
     * 预留字段
     */
    private Integer uuOrderType;
    /**
     * 发货模式：0,卖家直发；1,极速发货
     */
    private Integer uuShippingMode;
    /**
     * 报价ID
     */
    private Long uuTradeOfferId;
    /**
     * 报价链接
     */
    private String uuTradeOfferLinks;
    /**
     * 支付退款结果
     */
    private String payRefundRet;
    /**
     * 支付接口返回
     */
    private String payPayRet;


}