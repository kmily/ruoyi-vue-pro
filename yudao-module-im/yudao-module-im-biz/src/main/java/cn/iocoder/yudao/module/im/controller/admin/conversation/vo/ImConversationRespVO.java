package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会话 Response VO")
@Data
@ExcelIgnoreUnannotated // TODO @hao：excel 的注解可以先删除
public class ImConversationRespVO {

    // TODO @hao：example 都写下

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13905")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "所属用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "11545")
    @ExcelProperty("所属用户")
    private Long userId;

    // TODO @hao：@Schema 可以改成“会话类型”，不用把具体的数字写在上面哈。这样后续改动，会比较难改
    @Schema(description = "类型：1 单聊；2 群聊；4 通知会话（预留）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("类型：1 单聊；2 群聊；4 通知会话（预留）")
    private Integer conversationType;

    // TODO @hao：只写，聊天对象编号
    @Schema(description = "单聊时，用户编号；群聊时，群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21454")
    @ExcelProperty("单聊时，用户编号；群聊时，群编号")
    private Long targetId;

    // TODO @hao：只写 no 即可。
    @Schema(description = "会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId")
    private String no;

    // TODO @hao：只写 是否置顶；0 1 是数据库的结果哈；
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