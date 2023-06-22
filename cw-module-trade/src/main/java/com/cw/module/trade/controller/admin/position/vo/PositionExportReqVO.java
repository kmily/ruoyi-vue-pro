package com.cw.module.trade.controller.admin.position.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 账户持仓信息 Excel 导出 Request VO，参数和 PositionPageReqVO 是一致的")
@Data
public class PositionExportReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "账户id", example = "2873")
    private Long accountId;

    @Schema(description = "交易对")
    private String symbol;

    @Schema(description = "持仓数量")
    private Object quantity;

    @Schema(description = "第三方数据")
    private String thirdData;

}
