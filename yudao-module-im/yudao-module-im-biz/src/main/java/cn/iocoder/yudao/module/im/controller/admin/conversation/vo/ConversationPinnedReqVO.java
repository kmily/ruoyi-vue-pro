package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话置顶 Request VO")
@Data
public class ConversationPinnedReqVO {

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED, example = "s_1_2")
    @NotEmpty(message = "会话标志不能为空")
    private String no;

    @Schema(description = "是否置顶", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否置顶不能为空")
    private Boolean pinned;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "聊天对象编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long targetId;

    @Schema(description = "会话类型",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type; //枚举 ConversationTypeEnum



}