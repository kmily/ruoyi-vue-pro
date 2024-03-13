package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 消息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImMessagePageReqVO extends PageParam {

    @Schema(description = "客户端消息编号 uuid，用于排重", example = "3331")
    private String clientMessageId;

    @Schema(description = "发送人编号", example = "23239")
    private Long senderId;

    @Schema(description = "接收人编号", example = "32494")
    private Long receiverId;

    @Schema(description = "发送人昵称", example = "李四")
    private String senderNickname;

    @Schema(description = "发送人头像")
    private String senderAvatar;

    @Schema(description = "会话类型", example = "2")
    private Integer conversationType;

    @Schema(description = "会话标志")
    private String conversationNo;

    @Schema(description = "消息类型", example = "1")
    private Integer contentType;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "发送时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] sendTime;

    @Schema(description = "消息来源 100-用户发送；200-系统发送（一般是通知）；")
    private Integer sendFrom;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "消息状态 1 发送中、2 发送成功、3 发送失败、4 已删除、5 已撤回", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer messageStatus;

}