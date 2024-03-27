package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 消息分页 Request VO")
@Data
@ToString(callSuper = true)
public class MessagePageReqVO {

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "g_1000")
    private String conversationNo;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] sendTime;

}