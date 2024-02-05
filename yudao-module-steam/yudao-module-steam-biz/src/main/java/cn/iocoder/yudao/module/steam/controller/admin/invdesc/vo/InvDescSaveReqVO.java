package cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存信息详情新增/修改 Request VO")
@Data
public class InvDescSaveReqVO {

    @Schema(description = "appid", example = "835")
    private Integer appid;

    @Schema(description = "classid", example = "31980")
    private String classid;

    @Schema(description = "instanceid", example = "15663")
    private String instanceid;

    @Schema(description = "currency")
    private Integer currency;

    @Schema(description = "background_color")
    private String backgroundColor;

    @Schema(description = "icon_url", example = "https://www.iocoder.cn")
    private String iconUrl;

    @Schema(description = "icon_url_large")
    private String iconUrlLarge;

    @Schema(description = "descriptions")
    private String descriptions;

    @Schema(description = "tradable")
    private Integer tradable;

    @Schema(description = "actions")
    private String actions;

    @Schema(description = "fraudwarnings")
    private String fraudwarnings;

    @Schema(description = "name", example = "赵六")
    private String name;

    @Schema(description = "name_color")
    private String nameColor;

    @Schema(description = "type", example = "1")
    private String type;

    @Schema(description = "market_name", example = "王五")
    private String marketName;

    @Schema(description = "market_hash_name", example = "赵六")
    private String marketHashName;

    @Schema(description = "market_actions")
    private String marketActions;

    @Schema(description = "commodity")
    private Integer commodity;

    @Schema(description = "market_tradable_restriction")
    private Integer marketTradableRestriction;

    @Schema(description = "marketable")
    private Integer marketable;

    @Schema(description = "描述")
    private String tags;

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

    @Schema(description = "类型选择", example = "2")
    private String selType;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25981")
    private Long id;

    @Schema(description = "steamId", example = "25194")
    private String steamId;

}