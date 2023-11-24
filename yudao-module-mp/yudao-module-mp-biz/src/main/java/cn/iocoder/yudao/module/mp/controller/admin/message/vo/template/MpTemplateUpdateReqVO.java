package cn.iocoder.yudao.module.mp.controller.admin.message.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 公众号模板 Response VO")
@Data
public class MpTemplateUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "模版id", example = "XKHiMo9sK761moU2C99j0jscZVKusH9kOhTTUqkpOek")
    @NotNull(message = "公众号账号的编号不能为空")
    private String templateId;

    @Schema(description = "是否有效", example = "2")
    @NotNull(message = "公众号模板的有效值不能为空")
    private Integer status;

    @Schema(description = "小程序地址")
    private String appPath;

    @Schema(description = "链接", example = "https://www.baidu.com")
    private String url;

    @Schema(description = "消息内容")
    @NotNull(message = "公众号模板的消息内容不能为空")
    private List<Map<String,String>> messageData;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "标题")
    @NotNull(message = "公众号模板的标题不能为空")
    private String title;

}
