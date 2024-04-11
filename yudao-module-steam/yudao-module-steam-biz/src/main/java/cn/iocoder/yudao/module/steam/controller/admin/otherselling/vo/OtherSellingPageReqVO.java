package cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 其他平台在售分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OtherSellingPageReqVO extends PageParam {

    @Schema(description = "csgoid", example = "14673")
    private Integer appid;

    @Schema(description = "资产id(饰品唯一)", example = "457")
    private String assetid;

    @Schema(description = "classid", example = "19441")
    private String classid;

    @Schema(description = "instanceid", example = "17732")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "出售价格单价分", example = "1948")
    private Integer price;

    @Schema(description = "发货状态(0代表未出售，1代表出售中，2代表已出售 )", example = "2")
    private Integer transferStatus;

    @Schema(description = "contextid", example = "18890")
    private String contextid;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    private String iconUrl;

    @Schema(description = "商品名称", example = "张三")
    private String marketName;

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

    @Schema(description = "marketHashName", example = "赵六")
    private String marketHashName;

    @Schema(description = "在售展示权重")
    private Integer displayWeight;

    @Schema(description = "itemInfo")
    private String itemInfo;

    @Schema(description = "short_name", example = "赵六")
    private String shortName;

    @Schema(description = "出售用户名字", example = "李四")
    private String sellingUserName;

    @Schema(description = "出售用户头像")
    private String sellingAvator;

    @Schema(description = "出售用户id", example = "21029")
    private Integer sellingUserId;

    @Schema(description = "出售平台id")
    private Integer platformIdentity;

    @Schema(description = "steamId", example = "15026")
    private String steamId;

}