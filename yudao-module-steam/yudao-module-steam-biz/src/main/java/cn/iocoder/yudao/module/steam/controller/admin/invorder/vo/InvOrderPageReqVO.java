package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - steam订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvOrderPageReqVO extends PageParam {

    @Schema(description = "用户编号", example = "32273")
    private Long userId;

    @Schema(description = "assetid", example = "12103")
    private String assetid;

    @Schema(description = "classid", example = "24796")
    private String classid;

    @Schema(description = "instanceid", example = "29854")
    private String instanceid;

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", example = "2")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27239")
    private Long payOrderId;

}