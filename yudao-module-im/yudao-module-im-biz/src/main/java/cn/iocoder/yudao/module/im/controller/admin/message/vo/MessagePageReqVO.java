package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

// TODO @anhaohao：这个类，不要交 Page，而是消息列表，MessageListReqVO
@Schema(description = "管理后台 - 消息分页 Request VO")
@Data
@ToString(callSuper = true)
public class MessagePageReqVO {

    // TODO @anhaohao：还是传递 targetId 和 conversationType，我们要弱化 no 的概念；

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "g_1000")
    private String conversationNo;

    // TODO @anhaohao：应该不传递时间范围，而是传递分页的时间，然后根据时间，查询消息

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] sendTime;

}