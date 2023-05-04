package cn.iocoder.yudao.module.system.service.social;


import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.social.core.YudaoAuthRequestFactory;
import cn.iocoder.yudao.module.system.dal.dataobject.social.AppSocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserBindDO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserMapper;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SOCIAL_USER_AUTH_FAILURE;

/**
 * APP 段社交用户绑定解绑
 */
@Service
@Validated
@Slf4j
public class AppSocialUserServiceImpl implements AppSocialUserService{

    @Resource// 由于自定义了 YudaoAuthRequestFactory 无法覆盖默认的 AuthRequestFactory，所以只能注入它
    private YudaoAuthRequestFactory yudaoAuthRequestFactory;

    @Resource
    private SocialUserMapper socialUserMapper;

    @Resource
    private SocialUserBindMapper socialUserBindMapper;

    @Override
    @Transactional
    public void bindSocialUser(AppSocialUserBindReqDTO reqDTO) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(reqDTO.getType(), reqDTO.getCode(), reqDTO.getState());
        Assert.notNull(socialUser, "社交用户不能为空");

        // 社交用户可能之前绑定过别的用户，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndSocialUserId(reqDTO.getUserType(), socialUser.getId());

        // 用户可能之前已经绑定过该社交类型，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(reqDTO.getUserType(), reqDTO.getUserId(),
                socialUser.getType());

        // 绑定当前登录的社交用户
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(reqDTO.getUserId()).userType(reqDTO.getUserType())
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
    }

    @Override
    public SocialUserDO authSocialUser(Integer type, String code, String state) {
        // 优先从 DB 中获取，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAnState(type, code, state);
        if (socialUser != null) {
            return socialUser;
        }

        // 请求获取
        AuthUser authUser = getAuthUser(type, code, state);
        Assert.notNull(authUser, "三方用户不能为空");

        // 保存到 DB 中
        socialUser = socialUserMapper.selectByTypeAndOpenid(type, authUser.getUuid());
        if (socialUser == null) {
            socialUser = new SocialUserDO();
        }
        socialUser.setType(type).setCode(code).setState(state) // 需要保存 code + state 字段，保证后续可查询
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken()).setRawTokenInfo((toJsonString(authUser.getToken())))
                .setNickname(authUser.getNickname()).setAvatar(authUser.getAvatar()).setRawUserInfo(toJsonString(authUser.getRawUserInfo()));
        if (socialUser.getId() == null) {
            socialUserMapper.insert(socialUser);
        } else {
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }

    /**
     * 请求社交平台，获得授权的用户
     *
     * @param type 社交平台的类型
     * @param code 授权码
     * @param state 授权 state
     * @return 授权的用户
     */
    private AuthUser getAuthUser(Integer type, String code, String state) {
        AuthRequest authRequest = yudaoAuthRequestFactory.get(SocialTypeEnum.valueOfType(type).getSource());
        AuthCallback authCallback = AuthCallback.builder().code(code).state(state).build();
        AuthResponse<?> authResponse = authRequest.login(authCallback);
        log.info("[getAuthUser][请求社交平台 type({}) request({}) response({})]", type,
                toJsonString(authCallback), toJsonString(authResponse));
        if (!authResponse.ok()) {
            throw exception(SOCIAL_USER_AUTH_FAILURE, authResponse.getMsg());
        }
        return (AuthUser) authResponse.getData();
    }

    @Override
    public void unbindSocialUser(Long userId, Integer userType, Integer type, String openid) {

    }
}
