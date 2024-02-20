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
    @ExcelProperty("用户类型")
    private Integer userType;

    @Schema(description = "是否已支付[0未支付，1支付]", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "是否已支付[0未支付，1支付]", converter = DictConvert.class)
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

    @Schema(description = "提现金额", example = "25442")
    @ExcelProperty("提现金额")
    private Integer price;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}