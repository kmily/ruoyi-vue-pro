package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 消息列表 Request VO")
@Data
public class ImMessageListReqVO {

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

    @Schema(description = "会话所属人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "会话所属人编号不能为空")
    private Long userId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @InEnum(value = ImConversationTypeEnum.class,message = "会话类型必须是 {value}")
    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDateTime sendTime;

}