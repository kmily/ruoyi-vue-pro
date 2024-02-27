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

    @Schema(description = "参考价", example = "31534")
    @ExcelProperty("参考价")
    private String referencePrice;

}