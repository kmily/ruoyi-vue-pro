package cn.iocoder.yudao.module.im.websocket.message;

import cn.iocoder.yudao.module.im.dal.dataobject.message.body.ImMessageBody;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消息发送 receive")
@Data
public class ImReceiveMessage {

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer conversationType; // 对应 ImConversationTypeEnum 枚举

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long fromId;  // 根据 conversationType 区分

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contentType; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    private Long messageId;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime sendTime;

    @Schema(description = "收件箱编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18389")
    private Long inboxId;

    @Schema(description = "序号，按照 user 递增", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sequence;

}