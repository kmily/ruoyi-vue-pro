package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

// TODO @anhaohao：改成 ConversationUpdateLastReadTimeReqVO，项目目前都是动名词哈。更新置顶
@Schema(description = "管理后台 - 会话最后已读时间 Request VO")
@Data
public class ConversationLastTimeReqVO {

    // TODO @anhaohao：no 不用传递哈。因为 userId + targetId 可以推出来
    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "s_1_2")
    @NotEmpty(message = "会话标志不能为空")
    private String no;

    @Schema(description = "最后已读时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-01 00:00:00")
    @NotNull(message = "最后已读时间不能为空")
    private LocalDateTime lastReadTime;

    // TODO @anhaohao：userId 不用传递，因为 token 已经能解析出当前用户
    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话类型",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type; // 枚举 ConversationTypeEnum TODO ，这里可以使用 @InEnum 校验，这样这个注释就不用写了

}