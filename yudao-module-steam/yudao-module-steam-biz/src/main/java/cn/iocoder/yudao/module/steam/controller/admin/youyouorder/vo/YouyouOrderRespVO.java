package cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo;

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
public class YouyouOrderRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23430")
    @ExcelProperty("订单编号")
    private Long id;

    @Schema(description = "价格，单位：分 ", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("价格，单位：分 ")
    private Integer payAmount;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("是否已支付：[0:未支付 1:已经支付过]")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27655")
    @ExcelProperty("支付订单编号")
    private Long payOrderId;

    @Schema(description = "支付成功的支付渠道")
    @ExcelProperty("支付成功的支付渠道")
    private String payChannelCode;

    @Schema(description = "订单支付时间")
    @ExcelProperty("订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED, example = "18072")
    @ExcelProperty("退款金额，单位：分")
    private Integer refundPrice;

    @Schema(description = "退款订单编号", example = "14290")
    @ExcelProperty("退款订单编号")
    private Long payRefundId;

    @Schema(description = "退款时间")
    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23382")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "用户类型", example = "1")
    @ExcelProperty(value = "用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

    @Schema(description = "发货信息 json")
    @ExcelProperty("发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    @ExcelProperty("发货状态")
    private Integer transferStatus;

    @Schema(description = "商户订单号")
    @ExcelProperty("商户订单号")
    private String merchantOrderNo;

    @Schema(description = "订单号，内容生成")
    @ExcelProperty("订单号，内容生成")
    private String orderNo;

    @Schema(description = "发货模式 0,卖家直发；1,极速发货")
    @ExcelProperty("发货模式 0,卖家直发；1,极速发货")
    private Integer shippingMode;

    @Schema(description = "商品模版ID", example = "23427")
    @ExcelProperty("商品模版ID")
    private String commodityTemplateId;

    @Schema(description = "模板hashname", example = "王五")
    @ExcelProperty("模板hashname")
    private String commodityHashName;

    @Schema(description = "商品ID", example = "26763")
    @ExcelProperty("商品ID")
    private String commodityId;

    @Schema(description = "购买最高价,单价元", example = "18706")
    @ExcelProperty("购买最高价,单价元")
    private String purchasePrice;

    @Schema(description = "实际商品ID", example = "11462")
    @ExcelProperty("实际商品ID")
    private String realCommodityId;

    @Schema(description = "有品订单号")
    @ExcelProperty("有品订单号")
    private String uuOrderNo;

    @Schema(description = "有品商户订单号")
    @ExcelProperty("有品商户订单号")
    private String uuMerchantOrderNo;

    @Schema(description = "交易状态 0,成功；2,失败。", example = "2")
    @ExcelProperty("交易状态 0,成功；2,失败。")
    private Integer uuOrderStatus;

    @Schema(description = "收款状态", example = "1")
    @ExcelProperty("收款状态")
    private Integer sellCashStatus;

    @Schema(description = "卖家用户ID", example = "23929")
    @ExcelProperty("卖家用户ID")
    private Long sellUserId;

    @Schema(description = "卖家用户类型", example = "2")
    @ExcelProperty(value = "卖家用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer sellUserType;

}