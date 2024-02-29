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

    @Schema(description = "是否已支付：[0:未支付 1:已经支付过]", example = "2")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "27239")
    private Long payOrderId;

    @Schema(description = "用户类型", example = "2")
    private Integer userType;

    @Schema(description = "购买的steamId", example = "25575")
    private String steamId;

    @Schema(description = "发货信息 json")
    private String transferText;

    @Schema(description = "发货状态", example = "2")
    private Integer transferStatus;

    @Schema(description = "订单支付状态", example = "1")
    private Integer payOrderStatus;

    @Schema(description = "库存表ID参考steam_sell", example = "8828")
    private Long sellId;

    @Schema(description = "卖家用户类型", example = "2")
    private Integer sellUserType;

    @Schema(description = "卖家ID", example = "27846")
    private Long sellUserId;

    @Schema(description = "卖家金额状态", example = "2")
    private Integer sellCashStatus;

}