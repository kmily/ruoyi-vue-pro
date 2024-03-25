package cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - steam订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YouyouOrderPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "用户编号", example = "3421")
    private Long buyUserId;

    @Schema(description = "用户类型", example = "1")
    private Integer buyUserType;

    @Schema(description = "买方绑定用户", example = "31207")
    private Long buyBindUserId;

    @Schema(description = "购买的steamId", example = "8816")
    private String buySteamId;

    @Schema(description = " 收货方的Steam交易链接")
    private String buyTradeLinks;

    @Schema(description = "卖家用户ID", example = "16980")
    private Long sellUserId;

    @Schema(description = "卖家用户类型", example = "2")
    private Integer sellUserType;

    @Schema(description = "卖方绑定用户ID", example = "9686")
    private Long sellBindUserId;

    @Schema(description = "卖家steamId", example = "32234")
    private String sellSteamId;

    @Schema(description = "订单号，内容生成")
    private String orderNo;

    @Schema(description = "商户订单号")
    private String merchantNo;

    @Schema(description = "商品模版ID", example = "27844")
    private String commodityTemplateId;

    @Schema(description = "实际商品ID", example = "11047")
    private String realCommodityId;

    @Schema(description = "商品ID", example = "17168")
    private String commodityId;

    @Schema(description = "极速发货购买模式0：优先购买极速发货；1：只购买极速发货")
    private Integer fastShipping;

    @Schema(description = "支付订单编号", example = "25681")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payTime;

    @Schema(description = "价格，单位：分 ")
    private Integer payAmount;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", example = "1")
    private Boolean payStatus;

    @Schema(description = "订单支付状态", example = "2")
    private Integer payOrderStatus;

    @Schema(description = "退款订单编号", example = "22378")
    private Long payRefundId;

    @Schema(description = "退款金额，单位：分", example = "7154")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] refundTime;

    @Schema(description = "提现手续费收款人类型", example = "1")
    private Integer serviceFeeUserType;

    @Schema(description = "服务费，单位分")
    private Integer serviceFee;

    @Schema(description = "商户订单号")
    private String merchantOrderNo;

    @Schema(description = "模板hashname", example = "赵六")
    private String commodityHashName;

    @Schema(description = "违约付款接口返回")
    private String transferDamagesRet;

    @Schema(description = "收款状态", example = "2")
    private Integer sellCashStatus;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

    @Schema(description = "交易失败时退还")
    private Integer transferRefundAmount;

    @Schema(description = "服务费率")
    private String serviceFeeRate;

    @Schema(description = "提现手续费收款钱包", example = "23307")
    private Long serviceFeeUserId;

    @Schema(description = "交易违约判定时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] transferDamagesTime;

    @Schema(description = "商品名称", example = "赵六")
    private String marketName;

    @Schema(description = "发货信息 json")
    private String transferText;

    @Schema(description = "购买最高价,单价元", example = "25616")
    private String purchasePrice;

    @Schema(description = "转帐接口返回")
    private String serviceFeeRet;

    @Schema(description = "发货模式 0,卖家直发；1,极速发货")
    private Integer shippingMode;

    @Schema(description = "有品商户订单号")
    private String uuMerchantOrderNo;

    @Schema(description = "购买用户编号。", example = "27881")
    private Integer uuBuyerUserId;

    @Schema(description = "订单失败原因编号。")
    private Integer uuFailCode;

    @Schema(description = "订单失败原因提示信息。", example = "不香")
    private String uuFailReason;

    @Schema(description = "有品订单号")
    private String uuOrderNo;

    @Schema(description = "通知类型描述(等待发货，等待收货，购买成功，订单取消)。")
    private String uuNotifyDesc;

    @Schema(description = "通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。", example = "1")
    private Integer uuNotifyType;

    @Schema(description = "交易状态 0,成功；2,失败。", example = "1")
    private Integer uuOrderStatus;

    @Schema(description = "订单小状态。", example = "2")
    private Integer uuOrderSubStatus;

    @Schema(description = "预留字段", example = "1")
    private Integer uuOrderSubType;

    @Schema(description = "预留字段", example = "2")
    private Integer uuOrderType;

    @Schema(description = "发货模式：0,卖家直发；1,极速发货")
    private Integer uuShippingMode;

    @Schema(description = "报价ID", example = "2477")
    private Long uuTradeOfferId;

    @Schema(description = "报价链接")
    private String uuTradeOfferLinks;

}