package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageContentTypeEnum;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 消息 Request VO")
@Data
@ExcelIgnoreUnannotated
public class ImMessageReqVO {

    @Schema(description = "客户端消息编号 uuid，用于排重", requiredMode = Schema.RequiredMode.REQUIRED, example = "3331")
    @ExcelProperty("客户端消息编号 uuid，用于排重")
    private String clientMessageId;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @ExcelProperty("接收人编号")
    private Long receiverId;

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("会话类型")
    private Integer conversationType;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("消息类型")
    @InEnum(MessageContentTypeEnum.class)
    private Integer contentType;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("消息内容")
    private String content;

}