package cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 在售产品 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OnSaleProductRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10064")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "产品", requiredMode = Schema.RequiredMode.REQUIRED, example = "17875")
    @ExcelProperty("产品")
    private Long parentProductId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("商品名称")
    private String name;

    @Schema(description = "商家编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("商家编码")
    private String sku;

    @Schema(description = "商品注意点")
    @ExcelProperty("商品注意点")
    private String precautions;

    @Schema(description = "卖点，使用逗号隔开")
    @ExcelProperty("卖点，使用逗号隔开")
    private String sellingPoints;

    @Schema(description = "承接佣金规则")
    @ExcelProperty("承接佣金规则")
    private String acceptanceRules;

    @Schema(description = "结算要求")
    @ExcelProperty("结算要求")
    private String settlementRequirement;

    @Schema(description = "佣金结算规则（内部）")
    @ExcelProperty("佣金结算规则（内部）")
    private String settlementRulesInner;

    @Schema(description = "销售页上传照片", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "销售页上传照片", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean needSaleUploadImage;

    @Schema(description = "产品主图")
    @ExcelProperty("产品主图")
    private String mainImg;

    @Schema(description = "商品分享图")
    @ExcelProperty("商品分享图")
    private String shareImg;

    @Schema(description = "商品详情")
    @ExcelProperty("商品详情")
    private String detail;

    @Schema(description = "其他备注")
    @ExcelProperty("其他备注")
    private String otherNote;

    @Schema(description = "月租")
    @ExcelProperty("月租")
    private String monthlyRent;

    @Schema(description = "语言通话")
    @ExcelProperty("语言通话")
    private String voiceCall;

    @Schema(description = "通用流量")
    @ExcelProperty("通用流量")
    private String universalTraffic;

    @Schema(description = "定向流量")
    @ExcelProperty("定向流量")
    private String targetedTraffic;

    @Schema(description = "归属地")
    @ExcelProperty("归属地")
    private String belongArea;

    @Schema(description = "套餐详情")
    @ExcelProperty("套餐详情")
    private String packageDetails;

    @Schema(description = "套餐优惠期")
    @ExcelProperty("套餐优惠期")
    private Integer packageDiscountPeriod;

    @Schema(description = "优惠期起始时间:当月，次月，三月")
    @ExcelProperty("优惠期起始时间:当月，次月，三月")
    private Integer packageDiscountPeriodStart;

    @Schema(description = "上架", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("上架")
    private Boolean onSale;

    @Schema(description = "是否顶置")
    @ExcelProperty("是否顶置")
    private Boolean isTop;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}