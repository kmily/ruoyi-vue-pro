package cn.iocoder.yudao.module.system.controller.app.social.vo;


import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "取消社交绑定 Request VO, 使用code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSocialUserUnbindByCodeReqVO {
    @Schema(description = "社交平台的类型,参见 SocialTypeEnum 枚举值", required = true, example = "34")
    @InEnum(SocialTypeEnum.class)
    @NotNull(message = "社交平台的类型不能为空")
    private Integer type;

    @Schema(description = "授权码", required = true, example = "1024")
    @NotEmpty(message = "授权码不能为空")
    private String code;
}
