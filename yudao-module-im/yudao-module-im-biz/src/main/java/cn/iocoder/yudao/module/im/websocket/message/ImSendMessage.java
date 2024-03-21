package cn.iocoder.yudao.module.im.websocket.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - IM 消息发送 send")
@Data
public class ImSendMessage {

    @Schema(description = "客户端消息编号 uuid，用于排重", requiredMode = Schema.RequiredMode.REQUIRED, example = "3331")
    private String clientMessageId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    // TODO @hao：用下 InEnum 校验。这样可以少写 15 的注释哈；类似 contentType 也是；
    private Integer conversationType; // 对应 ImConversationTypeEnum 枚举

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long receiverId;  // 根据 conversationType 区分

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contentType; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

}