package cn.iocoder.yudao.module.system.controller.admin.sms.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* 短信模板 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class SmsTemplateBaseVO {

    @Schema(title  = "短信类型", required = true, example = "1", description = "参见 SmsTemplateTypeEnum 枚举类")
    @NotNull(message = "短信类型不能为空")
    private Integer type;

    @Schema(title  = "开启状态", required = true, example = "1", description = "参见 CommonStatusEnum 枚举类")
    @NotNull(message = "开启状态不能为空")
    private Integer status;

    @Schema(title  = "模板编码", required = true, example = "test_01")
    @NotNull(message = "模板编码不能为空")
    private String code;

    @Schema(title  = "模板名称", required = true, example = "yudao")
    @NotNull(message = "模板名称不能为空")
    private String name;

    @Schema(title  = "模板内容", required = true, example = "你好，{name}。你长的太{like}啦！")
    @NotNull(message = "模板内容不能为空")
    private String content;

    @Schema(title  = "备注", example = "哈哈哈")
    private String remark;

    @Schema(title  = "短信 API 的模板编号", required = true, example = "4383920")
    @NotNull(message = "短信 API 的模板编号不能为空")
    private String apiTemplateId;

    @Schema(title  = "短信渠道编号", required = true, example = "10")
    @NotNull(message = "短信渠道编号不能为空")
    private Long channelId;

}
