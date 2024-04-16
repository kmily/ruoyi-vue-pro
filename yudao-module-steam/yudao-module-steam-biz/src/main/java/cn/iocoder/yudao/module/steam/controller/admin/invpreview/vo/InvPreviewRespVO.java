package cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 饰品在售预览 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvPreviewRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22314")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "price", example = "0.03")
    @ExcelProperty("price")
    private String price;

    @Schema(description = "quantity", example = "25")
    @ExcelProperty("quantity")
    private String quantity;

    @Schema(description = "deals", example = "0")
    @ExcelProperty("deals")
    private String deals;

    @Schema(description = "item_id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9261")
    @ExcelProperty("item_id")
    private Long itemId;

    @Schema(description = "app_id", example = "11263")
    @ExcelProperty("app_id")
    private Integer appId;

    @Schema(description = "itemName", example = "张三")
    @ExcelProperty("itemName")
    private String itemName;

    @Schema(description = "shortName", example = "张三")
    @ExcelProperty("shortName")
    private String shortName;

    @Schema(description = "marketHashName", example = "王五")
    @ExcelProperty("marketHashName")
    private String marketHashName;

    @Schema(description = "imageUrl", example = "https://www.iocoder.cn")
    @ExcelProperty("imageUrl")
    private String imageUrl;

    @Schema(description = "itemInfo")
    @ExcelProperty("itemInfo")
    private String itemInfo;

    @Schema(description = "sellType", example = "1")
    @ExcelProperty("sellType")
    private String sellType;

    @Schema(description = "currencyId", example = "8912")
    @ExcelProperty("currencyId")
    private String currencyId;

    @Schema(description = "cnyPrice", example = "13545")
    @ExcelProperty("cnyPrice")
    private String cnyPrice;

    @Schema(description = "salePrice", example = "13436")
    @ExcelProperty("salePrice")
    private String salePrice;

    @Schema(description = "subsidyPrice", example = "27090")
    @ExcelProperty("subsidyPrice")
    private String subsidyPrice;

    @Schema(description = "activityTag")
    @ExcelProperty("activityTag")
    private String activityTag;

    @Schema(description = "tagList")
    @ExcelProperty("tagList")
    private String tagList;

    @Schema(description = "subsidyTag")
    @ExcelProperty("subsidyTag")
    private String subsidyTag;

    @Schema(description = "自动发货价格", example = "14804")
    @ExcelProperty("自动发货价格")
    private String autoPrice;

    @Schema(description = "自动发货数量")
    @ExcelProperty("自动发货数量")
    private String autoQuantity;

    @Schema(description = "其他平台在售数量")
    @ExcelProperty("其他平台在售数量")
    private Integer OtherSellQuantity;

    @Schema(description = "本平台在售数量")
    @ExcelProperty("其他平台在售数量")
    private Integer OurSellQuantity;

    @Schema(description = "参考价", example = "31534")
    @ExcelProperty("参考价")
    private String referencePrice;

    @Schema(description = "类别选择")
    @ExcelProperty("类别选择")
    private String selQuality;

    @Schema(description = "收藏品选择")
    @ExcelProperty("收藏品选择")
    private String selItemset;

    @Schema(description = "武器选择")
    @ExcelProperty("武器选择")
    private String selWeapon;

    @Schema(description = "外观选择")
    @ExcelProperty("外观选择")
    private String selExterior;

    @Schema(description = "品质选择")
    @ExcelProperty("品质选择")
    private String selRarity;

    @Schema(description = "类型选择", example = "1")
    @ExcelProperty("类型选择")
    private String selType;

    @Schema(description = "是否存在库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否存在库存")
    private Boolean existInv;

    @Schema(description = "页码")
    private int pageNum;

    @Schema(description = "展示权重")
    private Integer displayWeight;

}