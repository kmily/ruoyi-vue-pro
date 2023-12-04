package cn.iocoder.yudao.module.system.controller.app.helpcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 常见问题 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class HelpCenterBaseVO {

    @Schema(description = "帮助类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "帮助类型不能为空")
    private Byte type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问题描述", example = "随便")
    private String description;

    @Schema(description = "状态（0正常 1禁用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态（0正常 1禁用）不能为空")
    private Byte status;

}
