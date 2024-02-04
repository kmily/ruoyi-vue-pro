package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - steam用户库存储分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvPageReqVO extends PageParam {

    @Schema(description = "appid", example = "24254")
    private Integer appid;

    @Schema(description = "assetid", example = "29566")
    private String assetid;

    @Schema(description = "classid", example = "6035")
    private String classid;

    @Schema(description = "instanceid", example = "30735")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "steamId", example = "5752")
    private String steamId;

    @Schema(description = "启用", example = "1")
    private String status;

}