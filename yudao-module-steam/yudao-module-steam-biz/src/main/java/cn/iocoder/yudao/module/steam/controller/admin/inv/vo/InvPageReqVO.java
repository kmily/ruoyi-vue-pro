package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户库存储分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvPageReqVO extends PageParam {

    @Schema(description = "classid", example = "31967")
    private String classid;

    @Schema(description = "instanceid", example = "10375")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "steamId", example = "24553")
    private String steamId;

    @Schema(description = "csgoid", example = "6292")
    private Integer appid;

    @Schema(description = "绑定用户ID", example = "19319")
    private Long bindUserId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "发货状态(0代表未出售，1代表已出售 )", example = "2")
    private Integer transferStatus;

    @Schema(description = "平台用户ID", example = "20764")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "1")
    private Integer userType;

    @Schema(description = "资产id(饰品唯一)", example = "27001")
    private String assetid;

    @Schema(description = "出售价格单价分", example = "3557")
    private Integer price;

    @Schema(description = "道具名称", example = "AUG")
    private String marketName;

    @Schema(description = "出售价格单价分", example = "3557")
    private String pictureUrl;


}