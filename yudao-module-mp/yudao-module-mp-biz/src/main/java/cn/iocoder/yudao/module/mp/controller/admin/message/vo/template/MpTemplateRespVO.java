package cn.iocoder.yudao.module.mp.controller.admin.message.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 公众号模板 Response VO")
@Data
public class MpTemplateRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "模版id", example = "XKHiMo9sK761moU2C99j0jscZVKusH9kOhTTUqkpOek")
    private String templateId;

    @Schema(description = "是否有效", example = "2")
    private Integer status;

    @Schema(description = "小程序id")
    private String appId;

    @Schema(description = "小程序地址")
    private String appPath;

    @Schema(description = "链接", example = "https://www.baidu.com")
    private String url;

    @Schema(description = "消息内容")
    private List<Map<String,String>> messageData;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "标题")
    private String title;

}
