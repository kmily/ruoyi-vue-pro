package cn.iocoder.yudao.module.im.websocket.message;

import cn.iocoder.yudao.module.im.dal.dataobject.message.body.ImMessageBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 消息发送 send")
@Data
public class ImSendMessage {

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer conversationType; // 对应 ImConversationTypeEnum 枚举

    @Schema(description = "聊天对象，用户编号或群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long toId;  // 根据 conversationType 区分

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private ImMessageBody body;

}