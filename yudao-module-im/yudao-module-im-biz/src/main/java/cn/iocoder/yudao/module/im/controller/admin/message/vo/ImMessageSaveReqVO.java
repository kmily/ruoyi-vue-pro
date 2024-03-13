package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消息新增/修改 Request VO")
@Data
public class ImMessageSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30713")
    private Long id;

    @Schema(description = "客户端消息编号 uuid，用于排重", requiredMode = Schema.RequiredMode.REQUIRED, example = "3331")
    @NotEmpty(message = "客户端消息编号 uuid，用于排重不能为空")
    private String clientMessageId;

    @Schema(description = "发送人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23239")
    @NotNull(message = "发送人编号不能为空")
    private Long senderId;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

    @Schema(description = "发送人昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "发送人昵称不能为空")
    private String senderNickname;

    @Schema(description = "发送人头像", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发送人头像不能为空")
    private String senderAvatar;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;

    @Schema(description = "会话标志 conversation_no = a_b", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "会话标志不能为空")
    private String conversationNo;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "消息类型不能为空")
    private Integer contentType;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "消息内容不能为空")
    private String content;

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime sendTime;

    @Schema(description = "消息来源 100-用户发送；200-系统发送（一般是通知）；", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "消息来源 100-用户发送；200-系统发送（一般是通知）；不能为空")
    private Integer sendFrom;

    @Schema(description = "消息状态 1 发送中、2 发送成功、3 发送失败、4 已删除、5 已撤回", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "消息状态不能为空")
    private Integer messageStatus;
}