package cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 提现 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WithdrawalRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16393")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11178")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

    @Schema(description = "是否已支付", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "是否已支付", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "30058")
    @ExcelProperty("支付订单编号")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    @ExcelProperty("支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @ExcelProperty("订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款订单编号", example = "18236")
    @ExcelProperty("退款订单编号")
    private Long payRefundId;

    @Schema(description = "退款金额，单位分", example = "20210")
    @ExcelProperty("退款金额，单位分")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    @Schema(description = "提现金额", example = "19208")
    @ExcelProperty("提现金额")
    private Integer withdrawalPrice;

    @Schema(description = "提现信息")
    @ExcelProperty("提现信息")
    private String withdrawalInfo;

    @Schema(description = "服务费", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("服务费")
    private Integer serviceFee;

    @Schema(description = "费率", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("费率")
    private String serviceFeeRate;

    @Schema(description = "支付金额")
    @ExcelProperty("支付金额")
    private Integer paymentAmount;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审批状态")
    private Integer auditStatus;

    @Schema(description = "审核人", example = "15626")
    @ExcelProperty("审核人")
    private Long auditUserId;

    @Schema(description = "审核信息")
    @ExcelProperty("审核信息")
    private String auditMsg;

    @Schema(description = "提现手续费收款钱包", example = "25194")
    @ExcelProperty("提现手续费收款钱包")
    private Long serviceFeeUserId;

    @Schema(description = "提现手续费收款人类型", example = "2")
    @ExcelProperty("提现手续费收款人类型")
    private Integer serviceFeeUserType;

}