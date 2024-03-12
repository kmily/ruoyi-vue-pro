package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话新增/修改 Request VO")
@Data
public class ConversationSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13905")
    private Long id;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "11545")
    @NotNull(message = "所属用户不能为空")
    private Long userId;

    @Schema(description = "类型：1 单聊；2 群聊；4 通知会话（预留）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型：1 单聊；2 群聊；4 通知会话（预留）不能为空")
    private Boolean conversationType;

    @Schema(description = "单聊时，用户编号；群聊时，群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21454")
    @NotEmpty(message = "单聊时，用户编号；群聊时，群编号不能为空")
    private String targetId;

    @Schema(description = "会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId不能为空")
    private String no;

    @Schema(description = "是否置顶 0否 1是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否置顶 0否 1是不能为空")
    private Boolean pinned;

    @Schema(description = "最后已读时间")
    private LocalDateTime lastReadTime;

}