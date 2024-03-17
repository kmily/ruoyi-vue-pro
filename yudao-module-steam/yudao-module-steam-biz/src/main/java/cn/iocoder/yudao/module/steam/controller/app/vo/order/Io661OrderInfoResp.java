package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class Io661OrderInfoResp {
    private String orderNo;
    private String merchantNo;
    /**
     * 购买的steamId
     */
    private String steamId;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 是否已支付：[0:未支付 1:已经支付过]
     */
    private Boolean payStatus;
    /**
     * 价格，单位：分
     */
    private Integer paymentAmount;
    /**
     * 报价id
     */
    private String tradeOfferId;
    /**
     * 发货状态
     *
     */
    private Integer transferStatus;
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
     * 交易失败时退还
     */
    private Integer transferRefundAmount;
    /**
     * 交易违约金
     */
    private Integer transferDamagesAmount;
    /**
     * 购买平台
     * 枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatFormEnum 对应的类}
     */
    private String platformName;
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
     * 卖家steamId
     */
    private String sellSteamId;
    /**
     * 商品名称
     */
    private String marketName;
}
