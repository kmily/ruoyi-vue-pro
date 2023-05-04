package cn.iocoder.yudao.module.system.controller.app.social;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.api.social.SocialUserApi;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserBindReqVO;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserUnbindReqVO;
import cn.iocoder.yudao.module.system.convert.social.AppSocialUserConvert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 社交用户")
@RestController
@RequestMapping("/oa/social-user")
@Validated
public class AppSocialUserController {

    @Resource
    private SocialUserApi socialUserApi;

    @PostMapping("/bind")
    @Operation(summary = "社交绑定，使用 code 授权码")
    public CommonResult<Boolean> socialBind(@RequestBody @Valid AppSocialUserBindReqVO reqVO) {
        socialUserApi.bindSocialUser(AppSocialUserConvert.INSTANCE.convert(getLoginUserId(), UserTypeEnum.MEMBER.getValue(), reqVO));
        return CommonResult.success(true);
    }

    @DeleteMapping("/unbind")
    @Operation(summary = "取消社交绑定")
    public CommonResult<Boolean> socialUnbind(@RequestBody AppSocialUserUnbindReqVO reqVO) {
        socialUserApi.unbindSocialUser(AppSocialUserConvert.INSTANCE.convert(getLoginUserId(), UserTypeEnum.MEMBER.getValue(), reqVO));
        return CommonResult.success(true);
    }

}
