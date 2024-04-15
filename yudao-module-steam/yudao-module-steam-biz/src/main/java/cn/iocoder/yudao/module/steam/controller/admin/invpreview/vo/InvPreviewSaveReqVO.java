package cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class InvPreviewSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22314")
    private Long id;

    @Schema(description = "price", example = "0.03")
    private String price;

    @Schema(description = "quantity", example = "25")
    private String quantity;

    @Schema(description = "deals", example = "0")
    private String deals;

    @Schema(description = "item_id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9261")
    @NotNull(message = "item_id不能为空")
    private Long itemId;

    @Schema(description = "app_id", example = "11263")
    private Integer appId;

    @Schema(description = "itemName", example = "张三")
    private String itemName;

    @Schema(description = "shortName", example = "张三")
    private String shortName;

    @Schema(description = "marketHashName", example = "王五")
    private String marketHashName;

    @Schema(description = "imageUrl", example = "https://www.iocoder.cn")
    private String imageUrl;

    @Schema(description = "itemInfo")
    private String itemInfo;

    @Schema(description = "sellType", example = "1")
    private String sellType;

    @Schema(description = "currencyId", example = "8912")
    private String currencyId;

    @Schema(description = "cnyPrice", example = "13545")
    private String cnyPrice;

    @Schema(description = "salePrice", example = "13436")
    private String salePrice;

    @Schema(description = "subsidyPrice", example = "27090")
    private String subsidyPrice;

    @Schema(description = "activityTag")
    private String activityTag;

    @Schema(description = "tagList")
    private String tagList;

    @Schema(description = "subsidyTag")
    private String subsidyTag;

    @Schema(description = "自动发货价格", example = "14804")
    private String autoPrice;

    @Schema(description = "自动发货数量")
    private String autoQuantity;

    @Schema(description = "参考价", example = "31534")
    private String referencePrice;

    @Schema(description = "类别选择")
    private String selQuality;

    @Schema(description = "收藏品选择")
    private String selItemset;

    @Schema(description = "武器选择")
    private String selWeapon;

    @Schema(description = "外观选择")
    private String selExterior;

    @Schema(description = "品质选择")
    private String selRarity;

    @Schema(description = "类型选择", example = "1")
    private String selType;

    @Schema(description = "是否存在库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否存在库存不能为空")
    private Boolean existInv;

    @Schema(description = "展示权重")
    private Integer displayWeight;

}