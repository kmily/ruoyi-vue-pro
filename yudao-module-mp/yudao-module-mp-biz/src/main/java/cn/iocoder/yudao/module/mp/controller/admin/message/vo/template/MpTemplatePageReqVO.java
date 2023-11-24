package cn.iocoder.yudao.module.mp.controller.admin.message.vo.template;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 公众号粉丝分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpTemplatePageReqVO extends PageParam {

    @Schema(description = "公众号账号的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "公众号账号的编号不能为空")
    private Long accountId;

    @Schema(description = "公众号模版名称，模糊匹配", example = "XKHiMo9sK761moU2C99j0jscZVKusH9kOhTTUqkpOek")
    private String templateId;

    @Schema(description = "公众号模板标题，模糊匹配", example = "测试")
    private String title;

}
