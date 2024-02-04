package cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库存信息详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvDescRespVO {

    @Schema(description = "appid", example = "835")
    @ExcelProperty("appid")
    private Integer appid;

    @Schema(description = "classid", example = "31980")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "15663")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "currency")
    @ExcelProperty("currency")
    private Integer currency;

    @Schema(description = "background_color")
    @ExcelProperty("background_color")
    private String backgroundColor;

    @Schema(description = "tradable")
    @ExcelProperty("tradable")
    private Integer tradable;

    @Schema(description = "actions")
    @ExcelProperty("actions")
    private String actions;

    @Schema(description = "fraudwarnings")
    @ExcelProperty("fraudwarnings")
    private String fraudwarnings;

    @Schema(description = "name", example = "赵六")
    @ExcelProperty("name")
    private String name;

    @Schema(description = "name_color")
    @ExcelProperty("name_color")
    private String nameColor;

    @Schema(description = "type", example = "1")
    @ExcelProperty("type")
    private String type;

    @Schema(description = "market_name", example = "王五")
    @ExcelProperty("market_name")
    private String marketName;

    @Schema(description = "market_hash_name", example = "赵六")
    @ExcelProperty("market_hash_name")
    private String marketHashName;

    @Schema(description = "market_actions")
    @ExcelProperty("market_actions")
    private String marketActions;

    @Schema(description = "commodity")
    @ExcelProperty("commodity")
    private Integer commodity;

    @Schema(description = "market_tradable_restriction")
    @ExcelProperty("market_tradable_restriction")
    private Integer marketTradableRestriction;

    @Schema(description = "marketable")
    @ExcelProperty("marketable")
    private Integer marketable;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21516")
    @ExcelProperty("主键")
    private Long id;

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

    @Schema(description = "类型选择", example = "2")
    @ExcelProperty("类型选择")
    private String selType;

}