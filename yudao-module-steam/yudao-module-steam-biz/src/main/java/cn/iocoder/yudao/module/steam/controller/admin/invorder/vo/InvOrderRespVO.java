package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - steam订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvOrderRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12566")
    @ExcelProperty("订单编号")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32273")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "购买的steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "10415")
    @ExcelProperty("购买的steamId")
    private String steamId;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("是否已支付：[0:未支付 1:已经支付过]")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27239")
    @ExcelProperty("支付订单编号")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    @ExcelProperty("支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @ExcelProperty("订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款订单编号", example = "15353")
    @ExcelProperty("退款订单编号")
    private Long payRefundId;

    @Schema(description = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("退款金额，单位：分")
    private Integer refundAmount;

    @Schema(description = "退款时间")
    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "价格，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("价格，单位：分")
    private Integer paymentAmount;

    @Schema(description = "用户类型", example = "2")
    @ExcelProperty(value = "用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

    @Schema(description = "订单支付状态", example = "1")
    @ExcelProperty("订单支付状态")
    private Integer payOrderStatus;

    @Schema(description = "服务费，单位分")
    @ExcelProperty("服务费，单位分")
    private Integer serviceFee;

    @Schema(description = "服务费率")
    @ExcelProperty("服务费率")
    private String serviceFeeRate;

    @Schema(description = "优惠金额 分")
    @ExcelProperty("优惠金额 分")
    private Integer discountAmount;

    @Schema(description = "发货信息 json")
    @ExcelProperty("发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    @ExcelProperty("发货状态")
    private Integer transferStatus;

    @Schema(description = "库存表ID参考steam_sell", example = "8828")
    @ExcelProperty("库存表ID参考steam_sell")
    private Long sellId;

    @Schema(description = "商品描述ID", example = "3266")
    @ExcelProperty("商品描述ID")
    private Long invDescId;

    @Schema(description = "库存表ID", example = "2078")
    @ExcelProperty("库存表ID")
    private Long invId;

    @Schema(description = "卖家用户类型", example = "2")
    @ExcelProperty(value = "卖家用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer sellUserType;

    @Schema(description = "卖家ID", example = "27846")
    @ExcelProperty("卖家ID")
    private Long sellUserId;

    @Schema(description = "卖家金额状态", example = "2")
    @ExcelProperty("卖家金额状态")
    private Integer sellCashStatus;

    @Schema(description = "商品总额")
    @ExcelProperty("商品总额")
    private Integer commodityAmount;

    @Schema(description = "提现手续费收款钱包", example = "4269")
    @ExcelProperty("提现手续费收款钱包")
    private Long serviceFeeUserId;

    @Schema(description = "提现手续费收款人类型", example = "1")
    @ExcelProperty("提现手续费收款人类型")
    private Integer serviceFeeUserType;

    @Schema(description = "转帐接口返回")
    @ExcelProperty("转帐接口返回")
    private String serviceFeeRet;

    @Schema(description = "购买平台", example = "张三")
    @ExcelProperty("购买平台")
    private String platformName;

    @Schema(description = "购买平台代码")
    @ExcelProperty("购买平台代码")
    private Integer platformCode;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "商户订单号")
    @ExcelProperty("商户订单号")
    private String merchantNo;

    @Schema(description = "交易失败时退还")
    @ExcelProperty("交易失败时退还")
    private Integer transferRefundAmount;

    @Schema(description = "交易违约金")
    @ExcelProperty("交易违约金")
    private Integer transferDamagesAmount;

    @Schema(description = "交易违约判定时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交易违约判定时间")
    private LocalDateTime transferDamagesTime;

}