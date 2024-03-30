package cn.iocoder.yudao.module.im.websocket.message;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ConversationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消息发送 receive")
@Data
public class ImReceiveMessage {

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(ConversationTypeEnum.class)
    private Integer conversationType;

    // TODO @anhaohao：我们应该是 senderId；因为它和 receiverId 是相对应的哈。
    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long fromId;  // 根据 conversationType 区分

    // TODO @hao：昵称和头像，也直接发给接收人好了；

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    private Long messageId;

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contentType; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime sendTime;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sequence;

}