package cn.iocoder.yudao.module.steam.controller.admin.selling.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 在售饰品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SellingPageReqVO extends PageParam {

    @Schema(description = "csgoid", example = "18471")
    private Integer appid;

    @Schema(description = "资产id(饰品唯一)", example = "7558")
    private String assetid;

    @Schema(description = "classid", example = "15926")
    private String classid;

    @Schema(description = "instanceid", example = "28349")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "steamId", example = "23183")
    private String steamId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "出售价格单价分", example = "30804")
    private Integer price;

    @Schema(description = "平台用户ID", example = "12743")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "2")
    private Integer userType;

    @Schema(description = "绑定用户ID", example = "30171")
    private Long bindUserId;

    @Schema(description = "contextid", example = "12276")
    private String contextid;

    @Schema(description = "inv_desc_id", example = "3271")
    private Long invDescId;

    @Schema(description = "发货状态(0代表未出售，1代表出售中，2代表已出售 )", example = "1")
    private Integer transferStatus;


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

    @Schema(description = "market_name", example = "王五")
    private String marketName;

    @Schema(description = "market_hash_name", example = "23726")
    private String marketHashName;

    @Schema(description = "w", example = "王五")
    private String iconUrl;

    @Schema(description = "库存表id", example = "23726")
    private Long invId;

}