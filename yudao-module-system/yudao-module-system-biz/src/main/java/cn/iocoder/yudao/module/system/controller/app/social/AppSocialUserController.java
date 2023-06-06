package cn.iocoder.yudao.module.system.controller.app.social;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.social.core.YudaoAuthRequestFactory;
import cn.iocoder.yudao.module.system.api.social.SocialUserApi;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserBindReqVO;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserUnbindByCodeReqVO;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserUnbindReqVO;
import cn.iocoder.yudao.module.system.convert.social.AppSocialUserConvert;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SOCIAL_USER_AUTH_FAILURE;

@Tag(name = "用户 App - 社交用户")
@RestController
@RequestMapping("/oa/social-user")
@Validated
public class AppSocialUserController {

    @Resource
    private SocialUserApi socialUserApi;

    @Resource// 由于自定义了 YudaoAuthRequestFactory 无法覆盖默认的 AuthRequestFactory，所以只能注入它
    private YudaoAuthRequestFactory yudaoAuthRequestFactory;


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

    @DeleteMapping("/unbindByCode")
    @Operation(summary = "取消社交绑定ByCode")
    public CommonResult<Boolean> socialUnbind(@RequestBody AppSocialUserUnbindByCodeReqVO reqVO) {

        AuthUser authUser = getAuthUser(reqVO.getType(), reqVO.getCode(), "");
        Assert.notNull(authUser, "三方用户不能为空");

        if(getLoginUserId() == null){
            Assert.notNull(authUser, "用户未登录");
        }

        socialUserApi.unbindSocialUser(AppSocialUserConvert.INSTANCE.convert(getLoginUserId(),
                UserTypeEnum.MEMBER.getValue(), reqVO.getType(),authUser.getUuid()));
        return CommonResult.success(true);
    }


    private AuthUser getAuthUser(Integer type, String code, String state) {
        AuthRequest authRequest = yudaoAuthRequestFactory.get(SocialTypeEnum.valueOfType(type).getSource());
        AuthCallback authCallback = AuthCallback.builder().code(code).state(state).build();
        AuthResponse<?> authResponse = authRequest.login(authCallback);
        if (!authResponse.ok()) {
            throw exception(SOCIAL_USER_AUTH_FAILURE, authResponse.getMsg());
        }
        return (AuthUser) authResponse.getData();
    }

}
