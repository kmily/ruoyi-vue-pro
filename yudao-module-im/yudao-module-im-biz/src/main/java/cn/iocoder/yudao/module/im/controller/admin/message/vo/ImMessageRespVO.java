package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageContentTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消息 Response VO")
@Data
public class ImMessageRespVO {

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    private Long id;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = ImConversationTypeEnum.class)
    private Integer conversationType;

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long senderId;

    @Schema(description = "发送人昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String senderNickname;

    @Schema(description = "发送人头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://www.iocoder.cn/xxx.jpg")
    private String senderAvatar;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long receiverId;

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = ImMessageContentTypeEnum.class)
    private Integer contentType;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好")
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27 12:00:00")
    private LocalDateTime sendTime;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long sequence;

}