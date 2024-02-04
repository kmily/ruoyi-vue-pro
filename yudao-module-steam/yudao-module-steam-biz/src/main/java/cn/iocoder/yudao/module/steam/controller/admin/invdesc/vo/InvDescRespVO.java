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

    @Schema(description = "主键", example = "2594")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "appid", example = "9909")
    @ExcelProperty("appid")
    private Integer appid;

    @Schema(description = "classid", example = "17461")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "22443")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "currency")
    @ExcelProperty("currency")
    private Integer currency;

    @Schema(description = "background_color")
    @ExcelProperty("background_color")
    private String backgroundColor;

    @Schema(description = "icon_url", example = "https://www.iocoder.cn")
    @ExcelProperty("icon_url")
    private String iconUrl;

    @Schema(description = "icon_url_large")
    @ExcelProperty("icon_url_large")
    private String iconUrlLarge;

    @Schema(description = "tradable")
    @ExcelProperty("tradable")
    private Integer tradable;

    @Schema(description = "actions")
    @ExcelProperty("actions")
    private String actions;

    @Schema(description = "fraudwarnings")
    @ExcelProperty("fraudwarnings")
    private String fraudwarnings;

    @Schema(description = "name", example = "李四")
    @ExcelProperty("name")
    private String name;

    @Schema(description = "name_color")
    @ExcelProperty("name_color")
    private String nameColor;

    @Schema(description = "type", example = "2")
    @ExcelProperty("type")
    private String type;

    @Schema(description = "market_name", example = "芋艿")
    @ExcelProperty("market_name")
    private String marketName;

    @Schema(description = "market_hash_name", example = "张三")
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

    @Schema(description = "描述")
    @ExcelProperty("描述")
    private String tags;

}