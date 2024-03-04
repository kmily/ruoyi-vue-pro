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

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", example = "1")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27655")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payTime;

    @Schema(description = "退款金额，单位：分", example = "18072")
    private Integer refundPrice;

    @Schema(description = "退款订单编号", example = "14290")
    private Long payRefundId;

    @Schema(description = "退款时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] refundTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

    @Schema(description = "发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

    @Schema(description = "商户订单号")
    private String merchantOrderNo;

    @Schema(description = "订单号，内容生成")
    private String orderNo;

    @Schema(description = "发货模式 0,卖家直发；1,极速发货")
    private Integer shippingMode;

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

    @Schema(description = "有品订单号")
    private String uuOrderNo;

    @Schema(description = "有品商户订单号")
    private String uuMerchantOrderNo;

    @Schema(description = "交易状态 0,成功；2,失败。", example = "2")
    private Integer uuOrderStatus;

    @Schema(description = "收款状态", example = "1")
    private Integer sellCashStatus;

    @Schema(description = "卖家用户ID", example = "23929")
    private Long sellUserId;

    @Schema(description = "卖家用户类型", example = "2")
    private Integer sellUserType;

}