package cn.iocoder.yudao.module.member.controller.app.family.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author whycode
 * @title: FamilyAddMobileVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/812:09
 */

@Schema(description = "用户 APP - 修改手机 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FamilyUpdateMobileVO extends FamilyAddMobileVO{

    @Schema(description = "原手机验证码", required = true, example = "1024")
    @NotEmpty(message = "原手机验证码不能为空")
    @Length(min = 4, max = 6, message = "手机验证码长度为 4-6 位")
    @Pattern(regexp = "^[0-9]+$", message = "手机验证码必须都是数字")
    private String oldCode;

    @Schema(description = "原手机号",required = true,example = "15823654487")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 8, max = 11, message = "手机号码长度为 8-11 位")
    @Mobile
    private String oldMobile;
}
