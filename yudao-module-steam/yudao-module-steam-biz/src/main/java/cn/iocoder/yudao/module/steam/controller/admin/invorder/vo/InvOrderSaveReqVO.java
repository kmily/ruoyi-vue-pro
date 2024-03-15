package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - steam订单新增/修改 Request VO")
@Data
public class InvOrderSaveReqVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18145")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24410")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "购买的steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "10415")
    @NotEmpty(message = "购买的steamId不能为空")
    private String steamId;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "是否已支付：[0:未支付 1:已经支付过]不能为空")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27239")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款订单编号", example = "15353")
    private Long payRefundId;

    @Schema(description = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退款金额，单位：分不能为空")
    private Integer refundAmount;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "价格，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "价格，单位：分不能为空")
    private Integer paymentAmount;

    @Schema(description = "用户类型", example = "2")
    private Integer userType;

    @Schema(description = "服务费，单位分")
    private Integer serviceFee;

    @Schema(description = "服务费率")
    private String serviceFeeRate;

    @Schema(description = "优惠金额 分")
    private Integer discountAmount;

    @Schema(description = "发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

    @Schema(description = "订单支付状态", example = "1")
    private Integer payOrderStatus;

    @Schema(description = "库存表ID参考steam_sell", example = "8828")
    private Long sellId;

    @Schema(description = "商品描述ID", example = "3266")
    private Long invDescId;

    @Schema(description = "库存表ID", example = "2078")
    private Long invId;

    @Schema(description = "卖家用户类型", example = "2")
    private Integer sellUserType;

    @Schema(description = "卖家ID", example = "27846")
    private Long sellUserId;

    @Schema(description = "卖家金额状态", example = "2")
    private Integer sellCashStatus;

    @Schema(description = "商品总额")
    private Integer commodityAmount;

    @Schema(description = "提现手续费收款钱包", example = "4269")
    private Long serviceFeeUserId;

    @Schema(description = "提现手续费收款人类型", example = "1")
    private Integer serviceFeeUserType;

    @Schema(description = "转帐接口返回")
    private String serviceFeeRet;

    @Schema(description = "购买平台", example = "张三")
    private String platformName;

    @Schema(description = "购买平台代码")
    private Integer platformCode;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "商户订单号")
    private String merchantNo;

    @Schema(description = "交易失败时退还")
    private Integer transferRefundAmount;

    @Schema(description = "交易违约金")
    private Integer transferDamagesAmount;

    @Schema(description = "交易违约判定时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "交易违约判定时间不能为空")
    private LocalDateTime transferDamagesTime;

}