package cn.iocoder.yudao.module.trade.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(title = "交易订单项 Response VO")
@Data
public class TradeOrderItemRespVO {

    @Schema(title = "id自增长", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    @Schema(title = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderId;
    @Schema(title = "订单项状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    @Schema(title = "商品 SKU 编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer skuId;
    @Schema(title = "商品 SPU 编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer spuId;
    @Schema(title = "商品名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String skuName;
    @Schema(title = "图片名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String skuImage;
    @Schema(title = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
    @Schema(title = "原始单价，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer originPrice;
    @Schema(title = "购买单价，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer buyPrice;
    @Schema(title = "最终价格，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer presentPrice;
    @Schema(title = "购买总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer buyTotal;
    @Schema(title = "优惠总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer discountTotal;
    @Schema(title = "最终总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer presentTotal;
    @Schema(title = "退款总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer refundTotal;
    @Schema(title = "物流id")
    private Integer logisticsId;
    @Schema(title = "售后状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer afterSaleStatus;
    @Schema(title = "售后订单编号")
    private Integer afterSaleOrderId;
    @Schema(title = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
