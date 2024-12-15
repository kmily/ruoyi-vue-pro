package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;

@Schema(description = "管理后台 - 产品/渠道新增/修改 Request VO")
@Data
public class ProductSaveReqVO {

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3973")
    private Long id;

    @Schema(description = "运营商", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "运营商不能为空")
    private Integer operator;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品编码不能为空")
    private String sku;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "产品名称不能为空")
    private String name;

    @Schema(description = "产品类型", example = "21014")
    private Long haokaProductTypeId;

    @Schema(description = "归属地", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "归属地不能为空")
    private Integer belongAreaCode;

    @Schema(description = "产品渠道", example = "6850")
    private Long haokaProductChannelId;

    @Schema(description = "产品限制", requiredMode = Schema.RequiredMode.REQUIRED, example = "31322")
    @NotNull(message = "产品限制不能为空")
    private Long haokaProductLimitId;

    @Schema(description = "身份证号码验证", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证号码验证不能为空")
    private Integer idCardNumVerify;

    @Schema(description = "身份证图片验证", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证图片验证不能为空")
    private Integer idCardImgVerify;

    @Schema(description = "生产地址")
    private String produceAddress;

    @Schema(description = "黑名单过滤", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "黑名单过滤不能为空")
    private Boolean needBlacklistFilter;

    @Schema(description = "是否启用库存限制")
    private Boolean enableStockLimit;

    @Schema(description = "库存数量")
    private Integer stockNum;

    @Schema(description = "库存报警数量")
    private Integer stockWarnNum;

    @Schema(description = "生产备注")
    private String produceRemarks;

    @Schema(description = "结算规则")
    private String settlementRules;

    @Schema(description = "预估收益")
    private String estimatedRevenue;

    @Schema(description = "上架")
    private Boolean onSale;

    @Schema(description = "是否顶置")
    private Boolean isTop;

}