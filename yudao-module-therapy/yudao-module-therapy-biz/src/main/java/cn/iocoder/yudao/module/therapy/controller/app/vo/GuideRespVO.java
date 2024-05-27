package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "治疗交互 - 引导语 Response VO")
@Data
@ToString(callSuper = true)
public class GuideRespVO {
    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "欢迎来到中国！")
    private String content;
}
