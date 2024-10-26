package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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

    @Schema(description = "会话接受者头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String avatar;

    @Schema(description = "会话接受者昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "最后一条消息的描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastMessageDescription;

    @Schema(description = "未读消息条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unreadMessagesCount;

}