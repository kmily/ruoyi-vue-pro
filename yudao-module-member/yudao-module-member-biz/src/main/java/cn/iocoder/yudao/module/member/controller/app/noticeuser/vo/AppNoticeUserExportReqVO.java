package cn.iocoder.yudao.module.member.controller.app.noticeuser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 用户消息关联 Excel 导出 Request VO，参数和 NoticeUserPageReqVO 是一致的")
@Data
public class AppNoticeUserExportReqVO {

    @Schema(description = "消息ID", example = "27372")
    private Long noticeId;

    @Schema(description = "用户ID", example = "29307")
    private Long userId;

    @Schema(description = "阅读时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] readTime;

    @Schema(description = "阅读状态0-未读1-已读", example = "2")
    private Byte status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
