package cn.iocoder.yudao.module.mp.controller.admin.message.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 公众号模板 发送消息 VO")
@Data
public class MpTemplateFormUserReqVO {

    @NotEmpty(message = "模板ID不得为空")
    @Schema(description = "id", example = "XKHiMo9sK761moU2C99j0jscZVKusH9kOhTTUqkpOek")
    private Long id;

    @Schema(description = "公众号账号的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "公众号账号的编号不能为空")
    private Long accountId;

    @Schema(description = "公众号粉丝标识，模糊匹配", example = "o6_bmjrPTlm6_2sgVt7hMZOPfL2M")
    private String openid;

    @Schema(description = "公众号粉丝昵称，模糊匹配", example = "芋艿")
    private String nickname;

    @Schema(description = "省份", example = "广东省")
    private String province;

    @Schema(description = "城市", example = "广州市")
    private String city;

    @Schema(description = "标签", example = "1")
    private String tagId;

}
