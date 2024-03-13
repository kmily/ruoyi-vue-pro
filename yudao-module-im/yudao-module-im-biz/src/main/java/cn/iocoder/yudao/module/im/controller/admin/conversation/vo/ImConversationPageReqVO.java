package cn.iocoder.yudao.module.im.controller.admin.conversation.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会话分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImConversationPageReqVO extends PageParam {

    @Schema(description = "所属用户", example = "11545")
    private Long userId;

    @Schema(description = "类型：1 单聊；2 群聊；4 通知会话（预留）", example = "1")
    private Integer conversationType;

    @Schema(description = "单聊时，用户编号；群聊时，群编号", example = "21454")
    private String targetId;

    @Schema(description = "会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId")
    private String no;

    @Schema(description = "是否置顶 0否 1是")
    private Boolean pinned;

    @Schema(description = "最后已读时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastReadTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}