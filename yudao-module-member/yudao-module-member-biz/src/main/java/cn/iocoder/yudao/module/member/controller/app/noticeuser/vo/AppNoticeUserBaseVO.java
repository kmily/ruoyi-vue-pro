package cn.iocoder.yudao.module.member.controller.app.noticeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 用户消息关联 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppNoticeUserBaseVO {

    @Schema(description = "消息ID", required = true, example = "27372")
    @NotNull(message = "消息ID不能为空")
    private Long noticeId;

    @Schema(description = "用户ID", required = true, example = "29307")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "阅读时间", required = true)
    @NotNull(message = "阅读时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime readTime;

    @Schema(description = "阅读状态0-未读1-已读", example = "2")
    private Byte status;

}
