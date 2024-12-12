package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话最后已读时间 Request VO")
@Data
public class ImConversationCreateReqVO {

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = ImConversationTypeEnum.class, message = "会话类型必须是 {value}")
    private Integer type;

}