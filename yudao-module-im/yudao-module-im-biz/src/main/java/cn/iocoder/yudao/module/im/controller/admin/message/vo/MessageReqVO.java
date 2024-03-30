package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageContentTypeEnum;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

// TODO @anhaohao：MessageRespVO 消息响应 Response VO
// TODO @anahaohao：每个 example 都写下；啊哈，漏了地方，要补下；因为 http mock 的时候，可以根据它去生成
@Schema(description = "管理后台 - 消息 Request VO")
@Data
public class MessageReqVO {

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    private Long id;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer conversationType; // 对应 ImConversationTypeEnum 枚举

    // TODO @anhaohao：这个应该是 senderId
    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long fromId;  // 根据 conversationType 区分

    @Schema(description = "发送人昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String senderNickname;

    @Schema(description = "发送人头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senderAvatar;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    private Long receiverId;

    @Schema(description = "内容类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contentType; // 参见 ImMessageTypeEnum 枚举

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime sendTime;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sequence;

}