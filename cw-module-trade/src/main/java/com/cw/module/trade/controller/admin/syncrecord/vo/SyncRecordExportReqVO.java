package com.cw.module.trade.controller.admin.syncrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 账号同步记录 Excel 导出 Request VO，参数和 SyncRecordPageReqVO 是一致的")
@Data
public class SyncRecordExportReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "账户id", example = "6815")
    private Long accountId;

    @Schema(description = "同步类型", example = "2")
    private String type;

    @Schema(description = "第三方数据")
    private String thirdData;

}
