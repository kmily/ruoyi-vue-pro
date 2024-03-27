package cn.iocoder.yudao.module.im.controller.admin.inbox.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 收件箱保存消息 Request VO")
@Data
public class InboxSaveMessageReqVO {

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer conversationType; // 对应 ImConversationTypeEnum 枚举

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long fromId;  // 根据 conversationType 区分

    @Schema(description = "发送人昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String senderNickname;

    @Schema(description = "发送人头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senderAvatar;
    
    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    private Long receiverId;

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    private Long messageId;

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contentType; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime sendTime;

//    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Long sequence;

}