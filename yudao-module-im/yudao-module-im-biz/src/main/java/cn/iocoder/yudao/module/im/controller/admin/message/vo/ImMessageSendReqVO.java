package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageContentTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 发送消息 Request VO")
@Data
public class ImMessageSendReqVO {

    @Schema(description = "客户端消息编号 uuid，用于排重", requiredMode = Schema.RequiredMode.REQUIRED, example = "3331")
    @NotNull(message = "客户端消息编号不能为空")
    private String clientMessageId;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @InEnum(value = ImConversationTypeEnum.class)
    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;

    // TODO @dylan：这个是不是不用传递呀。因为 http 连接，有当前的 userid 呀
    @Schema(description = "会话所属用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "会话所属用户id")
    private Long conversationUserId;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(ImMessageContentTypeEnum.class)
    @NotNull(message = "消息类型不能为空")
    private Integer contentType;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好")
    @NotNull(message = "消息内容不能为空")
    private String content;

}