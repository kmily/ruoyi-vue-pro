package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MessageRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30713")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "客户端消息编号 uuid，用于排重", requiredMode = Schema.RequiredMode.REQUIRED, example = "3331")
    @ExcelProperty("客户端消息编号 uuid，用于排重")
    private String clientMessageId;

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23239")
    @ExcelProperty("发送人编号")
    private Long senderId;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @ExcelProperty("接收人编号")
    private Long receiverId;

    @Schema(description = "发送人昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("发送人昵称")
    private String senderNickname;

    @Schema(description = "发送人头像", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发送人头像")
    private String senderAvatar;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("会话类型")
    private Boolean conversationType;

    @Schema(description = "会话标志", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("会话标志")
    private String conversationNo;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("消息类型")
    private Boolean contentType;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("消息内容")
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "消息来源 100-用户发送；200-系统发送（一般是通知）；", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("消息来源 100-用户发送；200-系统发送（一般是通知）；")
    private Boolean sendFrom;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}