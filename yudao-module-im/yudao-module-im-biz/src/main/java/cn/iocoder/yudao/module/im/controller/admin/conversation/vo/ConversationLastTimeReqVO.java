package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话最后已读时间 Request VO")
@Data
public class ConversationLastTimeReqVO {

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "s_1_2")
    @NotEmpty(message = "会话标志不能为空")
    private String no;

    @Schema(description = "最后已读时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-01 00:00:00")
    @NotNull(message = "最后已读时间不能为空")
    private LocalDateTime lastReadTime;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话类型",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type; //枚举 ConversationTypeEnum

}