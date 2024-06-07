package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/5/29
 * @Description: 自动思维 VO
 **/
@Data
public class AutomatedThinkingRequestVO {

        @Schema(description = "会话ID",requiredMode = Schema.RequiredMode.REQUIRED,example = "uuid")
        private String conversationId;

        @Schema(description = "会话内容",requiredMode = Schema.RequiredMode.REQUIRED,example = "你好")
        private String content;

}
