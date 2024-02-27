package cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 饰品在售预览分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvPreviewPageReqVO extends PageParam {

    @Schema(description = "price", example = "0.03")
    private String price;

    @Schema(description = "quantity", example = "25")
    private String quantity;

    @Schema(description = "deals", example = "0")
    private String deals;

    @Schema(description = "item_id", example = "9261")
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

}