package cn.iocoder.yudao.module.therapy.controller.app.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "治疗方案 历史聊天内容 Response VO")
@Data
public class TreatmentHistoryChatMessageVO {

        @Schema(description = "消息内容", example = "你好")
        private Object message;

        @Schema(description = "是否系统消息", example = "true")
        private Boolean isSystem;

        @Schema(description = "消息时间", example = "")
        private LocalDateTime createTime;
}
