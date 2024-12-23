package cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 在售产品新增/修改 Request VO")
@Data
public class OnSaleProductSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10064")
    private Long id;

    @Schema(description = "产品", requiredMode = Schema.RequiredMode.REQUIRED, example = "17875")
    @NotNull(message = "产品不能为空")
    private Long parentProductId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品名称不能为空")
    private String name;

    @Schema(description = "商家编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商家编码不能为空")
    private String sku;

    @Schema(description = "商品注意点")
    private String precautions;

    @Schema(description = "卖点，使用逗号隔开")
    private String sellingPoints;

    @Schema(description = "承接佣金规则")
    private String acceptanceRules;

    @Schema(description = "结算要求")
    private String settlementRequirement;

    @Schema(description = "佣金结算规则（内部）")
    private String settlementRulesInner;

    @Schema(description = "销售页上传照片", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "销售页上传照片不能为空")
    private Boolean needSaleUploadImage;

    @Schema(description = "产品主图")
    private String mainImg;

    @Schema(description = "商品分享图")
    private String shareImg;

    @Schema(description = "商品详情")
    private String detail;

    @Schema(description = "其他备注")
    private String otherNote;

    @Schema(description = "月租")
    private String monthlyRent;

    @Schema(description = "语言通话")
    private String voiceCall;

    @Schema(description = "通用流量")
    private String universalTraffic;

    @Schema(description = "定向流量")
    private String targetedTraffic;

    @Schema(description = "归属地")
    private String belongArea;

    @Schema(description = "套餐详情")
    private String packageDetails;

    @Schema(description = "套餐优惠期")
    private Integer packageDiscountPeriod;

    @Schema(description = "优惠期起始时间:当月，次月，三月")
    private Integer packageDiscountPeriodStart;

    @Schema(description = "上架", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "上架不能为空")
    private Boolean onSale;

    @Schema(description = "是否顶置")
    private Boolean isTop;

}