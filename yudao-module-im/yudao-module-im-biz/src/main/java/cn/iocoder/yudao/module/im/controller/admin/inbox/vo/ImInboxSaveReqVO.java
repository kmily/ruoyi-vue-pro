package cn.iocoder.yudao.module.im.controller.admin.inbox.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 收件箱新增/修改 Request VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImInboxSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18389")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3979")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    @NotNull(message = "消息编号不能为空")
    private Long messageId;

    @Schema(description = "序号，按照 user 递增", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "序号，按照 user 递增不能为空")
    private Long sequence;

    public ImInboxSaveReqVO(Long userId, Long messageId, Long sequence) {
        this.userId = userId;
        this.messageId = messageId;
        this.sequence = sequence;
    }

}