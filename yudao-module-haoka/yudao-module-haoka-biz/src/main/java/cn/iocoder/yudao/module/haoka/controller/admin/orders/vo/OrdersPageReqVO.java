package cn.iocoder.yudao.module.haoka.controller.admin.orders.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrdersPageReqVO extends PageParam {

    @Schema(description = "供应商-商品名称", example = "芋艿")
    private String supplierProductName;

    @Schema(description = "供应商-商品编码SKU")
    private String supplierProductSku;

    @Schema(description = "外部订单编号", example = "1396")
    private String sourceId;

    @Schema(description = "产品SKU")
    private String productSku;

    @Schema(description = "外部SKU")
    private String sourceSku;

    @Schema(description = "证件号码")
    private String idCardNum;

    @Schema(description = "收件人电话")
    private String addressMobile;

    @Schema(description = "物流单号")
    private String trackingNumber;

    @Schema(description = "订单状态码", example = "1")
    private Long status;

    @Schema(description = "标志")
    private Long flag;

    @Schema(description = "订单来源")
    private String source;

    @Schema(description = "用户下单时间")
    private LocalDateTime orderedAt;

    @Schema(description = "生产时间")
    private LocalDateTime producedAt;

    @Schema(description = "发货时间")
    private LocalDateTime deliveredAt;

    @Schema(description = "激活时间")
    private LocalDateTime activatedAt;

    @Schema(description = "充值时间")
    private LocalDateTime rechargedAt;

    @Schema(description = "状态变更时间")
    private LocalDateTime statusUpdatedAt;

    @Schema(description = "退款状态", example = "1")
    private String refundStatus;

    @Schema(description = "激活状态", example = "1")
    private String activeStatus;

    @Schema(description = "ICCID", example = "2781")
    private String iccid;

    @Schema(description = "真实外部订单编号", example = "25683")
    private String realSourceId;

    @Schema(description = "分销商名称", example = "王五")
    private String merchantName;

    @Schema(description = "上游状态", example = "2")
    private String upStatus;

    @Schema(description = "上游订单号", example = "21235")
    private String upstreamOrderId;

    @Schema(description = "订单状态名称", example = "王五")
    private String statusName;

}