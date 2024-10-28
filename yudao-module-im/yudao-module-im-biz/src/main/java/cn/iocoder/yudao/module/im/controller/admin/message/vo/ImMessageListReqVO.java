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

    // TODO @dylan：是不是 receiverId 和 senderId 保留一个 receiverId 就行。这样，A + 自己（调用接口，从 getLoginUserId 拿），然后 conversationType，最终可以拼接出 conversationNo
    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "发送人编号不能为空")
    private Long senderId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @InEnum(value = ImConversationTypeEnum.class,message = "会话类型必须是 {value}")
    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDateTime sendTime;

    // TODO @dylan：这个，可以根据 receiverId、senderId、conversationType 推导的，建议可以去掉。
    @Schema(description = "会话编号编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "会话编号不能为空")
    private String conversationNo;

}