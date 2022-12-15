package cn.iocoder.yudao.module.trade.controller.app.order.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Schema(title = "订单交易 Response VO")
@Data
public class TradeOrderRespVO {

    @Schema(title = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    @Schema(title = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer userId;
    @Schema(title = "订单单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;
    @Schema(title = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderStatus;
    @Schema(title = "备注")
    private String remark;
    @Schema(title = "订单结束时间")
    private LocalDateTime endTime;
    @Schema(title = "订单金额(总金额)，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer buyPrice;
    @Schema(title = "优惠总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer discountPrice;
    @Schema(title = "物流金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer logisticsPrice;
    @Schema(title = "最终金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer presentPrice;
    @Schema(title = "支付金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer payPrice;
    @Schema(title = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer refundPrice;
    @Schema(title = "付款时间")
    private LocalDateTime payTime;
    @Schema(title = "支付订单编号")
    private Integer payTransactionId;
    @Schema(title = "支付渠道")
    private Integer payChannel;
    @Schema(title = "配送类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer deliveryType;
    @Schema(title = "发货时间")
    private LocalDateTime deliveryTime;
    @Schema(title = "收货时间")
    private LocalDateTime receiveTime;
    @Schema(title = "收件人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverName;
    @Schema(title = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverMobile;
    @Schema(title = "地区编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer receiverAreaCode;
    @Schema(title = "收件详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverDetailAddress;
    @Schema(title = "售后状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer afterSaleStatus;
    @Schema(title = "优惠劵编号")
    private Integer couponCardId;
    @Schema(title = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    /**
     * 订单项数组
     *
     * // TODO 芋艿，后续考虑怎么优化下，目前是内嵌了别的 dto
     */
    private List<TradeOrderItemRespVO> orderItems;


}
