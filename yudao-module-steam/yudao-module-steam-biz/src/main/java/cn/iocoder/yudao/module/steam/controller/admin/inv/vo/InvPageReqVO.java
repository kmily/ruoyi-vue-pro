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

    @Schema(description = "appid", example = "13212")
    private Integer appid;

    @Schema(description = "assetid", example = "7883")
    private String assetid;

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

    @Schema(description = "启用", example = "1")
    private Boolean status;

    @Schema(description = "出售价格单价分", example = "26052")
    private Integer price;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

}