package cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 提现新增/修改 Request VO")
@Data
public class WithdrawalSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16393")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11178")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    @Schema(description = "是否已支付[0未支付，1支付]", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "是否已支付[0未支付，1支付]不能为空")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "30058")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款订单编号", example = "18236")
    private Long payRefundId;

    @Schema(description = "退款金额，单位分", example = "20210")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "提现金额", example = "25442")
    private Integer price;

    @Schema(description = "提现信息")
    private String withdrawalInfo;

}