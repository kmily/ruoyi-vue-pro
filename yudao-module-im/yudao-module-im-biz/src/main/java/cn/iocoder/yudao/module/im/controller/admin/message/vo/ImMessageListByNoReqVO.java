package cn.iocoder.yudao.module.im.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

// TODO @dylan：看看，是不是融合到 ImMessageListReqVO 里
@Schema(description = "管理后台 - 消息列表 Request VO")
@Data
@ToString(callSuper = true)
public class ImMessageListByNoReqVO {

    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-27 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDateTime sendTime;

    @Schema(description = "会话编号编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32494")
    @NotNull(message = "会话编号不能为空")
    private String conversationNo ;

}