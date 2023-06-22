package com.cw.module.trade.controller.admin.account.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 交易账号分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountPageReqVO extends PageParam {

    @Schema(description = "账号名称", example = "李四")
    private String name;

    @Schema(description = "api访问key")
    private String appKey;

    @Schema(description = "api访问秘钥")
    private String appSecret;

    @Schema(description = "余额（第三方查询返回信息）")
    private String balance;

    @Schema(description = "最后一次余额查询时间(时间戳)")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Long[] lastBalanceQueryTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "账号管理用户ID")
    private Long relateUser;

}
