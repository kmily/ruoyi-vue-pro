package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConversationRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13905")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "11545")
    @ExcelProperty("所属用户")
    private Long userId;

    @Schema(description = "类型：1 单聊；2 群聊；4 通知会话（预留）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("类型：1 单聊；2 群聊；4 通知会话（预留）")
    private Boolean conversationType;

    @Schema(description = "单聊时，用户编号；群聊时，群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21454")
    @ExcelProperty("单聊时，用户编号；群聊时，群编号")
    private String targetId;

    @Schema(description = "会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId")
    private String no;

    @Schema(description = "是否置顶 0否 1是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否置顶 0否 1是")
    private Boolean pinned;

    @Schema(description = "最后已读时间")
    @ExcelProperty("最后已读时间")
    private LocalDateTime lastReadTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}