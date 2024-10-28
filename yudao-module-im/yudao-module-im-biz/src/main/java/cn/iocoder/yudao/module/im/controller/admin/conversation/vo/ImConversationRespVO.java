package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话 Response VO")
@Data
public class ImConversationRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13905")
    private Long id;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "s_1_2")
    private String no;

    @Schema(description = "是否置顶", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Boolean pinned;

    @Schema(description = "最后已读时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-01 00:00:00")
    private LocalDateTime lastReadTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    // TODO @dylan：这块，交给前端聚合哈。im 这块，我们重前端，后端更多解决消息的通信和存储。
    // 1. 基础信息：根据会话类型，查询会话接受者的头像、昵称
    // 2. 未读信息：前端自己增量拉取，基于本地 db 查看

    @Schema(description = "会话接受者头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String avatar;

    @Schema(description = "会话接受者昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "最后一条消息的描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastMessageDescription;

    @Schema(description = "未读消息条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unreadMessagesCount;

}