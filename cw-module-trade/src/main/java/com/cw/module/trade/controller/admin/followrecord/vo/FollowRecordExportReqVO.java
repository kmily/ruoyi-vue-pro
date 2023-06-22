package com.cw.module.trade.controller.admin.followrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 账号跟随记录 Excel 导出 Request VO，参数和 FollowRecordPageReqVO 是一致的")
@Data
public class FollowRecordExportReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "账号通知信息", example = "4307")
    private Long accountNotifyId;

    @Schema(description = "跟随账号", example = "25533")
    private Long followAccount;

    @Schema(description = "操作账号", example = "15154")
    private Long operateAccount;

    @Schema(description = "操作时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] operateTime;

    @Schema(description = "操作内容")
    private String operateInfo;

    @Schema(description = "操作结果")
    private Boolean operateSuccess;

    @Schema(description = "操作结果响应数据")
    private String operateResult;

    @Schema(description = "跟单操作描述")
    private String operateDesc;

}
