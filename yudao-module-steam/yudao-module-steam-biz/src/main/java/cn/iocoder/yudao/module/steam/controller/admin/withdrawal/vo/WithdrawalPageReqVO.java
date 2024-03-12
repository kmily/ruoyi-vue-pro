package cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 提现分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WithdrawalPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "11178")
    private Long userId;

    @Schema(description = "用户类型", example = "2")
    private Integer userType;

    @Schema(description = "是否已支付", example = "2")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "30058")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payTime;

    @Schema(description = "退款订单编号", example = "18236")
    private Long payRefundId;

    @Schema(description = "退款金额，单位分", example = "20210")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] refundTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "提现金额", example = "19208")
    private Integer withdrawalPrice;

    @Schema(description = "提现信息")
    private String withdrawalInfo;

    @Schema(description = "服务费")
    private Integer serviceFee;

    @Schema(description = "费率")
    private String serviceFeeRate;

    @Schema(description = "支付金额")
    private Integer paymentAmount;

}