package cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - steam订单新增/修改 Request VO")
@Data
public class YouyouOrderSaveReqVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23430")
    private Long id;

    @Schema(description = "价格，单位：分 ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "价格，单位：分 不能为空")
    private Integer payAmount;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否已支付：[0:未支付 1:已经支付过]不能为空")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27655")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED, example = "18072")
    @NotNull(message = "退款金额，单位：分不能为空")
    private Integer refundPrice;

    @Schema(description = "退款订单编号", example = "14290")
    private Long payRefundId;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23382")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

    @Schema(description = "发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

    @Schema(description = "订单支付状态", example = "2")
    private Integer payOrderStatus;

    @Schema(description = "商户订单号")
    private String merchantOrderNo;

    @Schema(description = "订单号，内容生成")
    private String orderNo;

    @Schema(description = "发货模式 0,卖家直发；1,极速发货")
    private Integer shippingMode;

    @Schema(description = " 收货方的Steam交易链接")
    private String tradeLinks;

    @Schema(description = "商品模版ID", example = "23427")
    private String commodityTemplateId;

    @Schema(description = "模板hashname", example = "王五")
    private String commodityHashName;

    @Schema(description = "商品ID", example = "26763")
    private String commodityId;

    @Schema(description = "购买最高价,单价元", example = "18706")
    private String purchasePrice;

    @Schema(description = "实际商品ID", example = "11462")
    private String realCommodityId;

    @Schema(description = "极速发货购买模式0：优先购买极速发货；1：只购买极速发货")
    private Integer fastShipping;

    @Schema(description = "有品订单号")
    private String uuOrderNo;

    @Schema(description = "有品商户订单号")
    private String uuMerchantOrderNo;

    @Schema(description = "交易状态 0,成功；2,失败。", example = "2")
    private Integer uuOrderStatus;

}