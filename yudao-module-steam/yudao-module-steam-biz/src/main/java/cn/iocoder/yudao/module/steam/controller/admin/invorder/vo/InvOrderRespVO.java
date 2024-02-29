package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import cn.iocoder.yudao.module.steam.service.steam.TransferMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

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

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "是否已支付：[0:未支付 1:已经支付过]", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
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

    @Schema(description = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED, example = "19998")
    @ExcelProperty("退款金额，单位：分")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "价格，单位：分 ", requiredMode = Schema.RequiredMode.REQUIRED, example = "5643")
    @ExcelProperty("价格，单位：分 ")
    private Integer price;

    @Schema(description = "用户类型", example = "2")
    @ExcelProperty("用户类型")
    private Integer userType;

    @Schema(description = "购买的steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "25575")
    @ExcelProperty("购买的steamId")
    private String steamId;

    @Schema(description = "发货信息 json")
//    @ExcelProperty("发货信息 json")
    private TransferMsg transferText;

    @Schema(description = "发货状态", example = "2")
    @ExcelProperty("发货状态")
    private Integer transferStatus;

    @Schema(description = "订单支付状态", example = "1")
    @ExcelProperty(value = "订单支付状态", converter = DictConvert.class)
    @DictFormat("pay_order_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer payOrderStatus;

    @Schema(description = "库存表ID参考steam_sell", example = "8828")
    @ExcelProperty("库存表ID参考steam_sell")
    private Long sellId;

    @Schema(description = "商品描述ID", example = "3266")
    @ExcelProperty("商品描述ID")
    private Long invDescId;

    @Schema(description = "库存表ID", example = "2078")
    @ExcelProperty("库存表ID")
    private Long invId;

}