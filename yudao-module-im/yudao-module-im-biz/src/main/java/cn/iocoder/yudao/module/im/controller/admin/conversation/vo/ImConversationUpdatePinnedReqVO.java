package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 会话置顶 Request VO")
@Data
public class ImConversationUpdatePinnedReqVO {

    @Schema(description = "是否置顶", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否置顶不能为空")
    private Boolean pinned;

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = ImConversationTypeEnum.class, message = "会话类型必须是 {value}")
    private Integer type;

}