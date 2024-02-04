package cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存信息详情新增/修改 Request VO")
@Data
public class InvDescSaveReqVO {

    @Schema(description = "主键", example = "2594")
    private Long id;

    @Schema(description = "appid", example = "9909")
    private Integer appid;

    @Schema(description = "classid", example = "17461")
    private String classid;

    @Schema(description = "instanceid", example = "22443")
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

    @Schema(description = "name", example = "李四")
    private String name;

    @Schema(description = "name_color")
    private String nameColor;

    @Schema(description = "type", example = "2")
    private String type;

    @Schema(description = "market_name", example = "芋艿")
    private String marketName;

    @Schema(description = "market_hash_name", example = "张三")
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

}