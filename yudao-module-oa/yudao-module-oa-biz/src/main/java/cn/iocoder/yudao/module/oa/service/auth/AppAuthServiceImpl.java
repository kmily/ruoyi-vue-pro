package cn.iocoder.yudao.module.oa.service.auth;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.oa.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.oa.convert.auth.AuthConvert;
import cn.iocoder.yudao.module.system.api.logger.LoginLogApi;
import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.yudao.module.system.api.oauth2.OAuth2TokenApi;
import cn.iocoder.yudao.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.module.system.api.sms.SmsCodeApi;
import cn.iocoder.yudao.module.system.api.social.SocialUserApi;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

@Service
@Slf4j
public class AppAuthServiceImpl implements AppAuthService{

    @Resource
    private AdminUserService userService;
    @Resource
    private SmsCodeApi smsCodeApi;
    @Resource
    private LoginLogApi loginLogApi;
    @Resource
    private SocialUserApi socialUserApi;
    @Resource
    private OAuth2TokenApi oauth2TokenApi;

    @Resource
    private WxMaService wxMaService;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AdminUserMapper userMapper;


    @Override
    public AppAuthLoginRespVO login(AppAuthLoginReqVO reqVO) {
        AdminUserDO user = userMapper.selectByMobile(reqVO.getMobile());
        // 如果 socialType 非空，说明需要绑定社交用户
        if(reqVO.getSocialCode() != null) {
            socialUserApi.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                    reqVO.getSocialType(), reqVO.getSocialCode(), reqVO.getSocialState()));
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE);

    }
    private AppAuthLoginRespVO createTokenAfterLoginSuccess(AdminUserDO user, String mobile, LoginLogTypeEnum logType) {
        // 插入登陆日志
        createLoginLog(user.getId(), mobile, logType, LoginResultEnum.SUCCESS);
        // 创建 Token 令牌
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(user.getId()).setUserType(getUserType().getValue())
                .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenRespDTO);
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO reqVO) {
        return null;
    }

    @Override
    public AppAuthLoginRespVO socialLogin(AppAuthSocialLoginReqVO reqVO) {
        return null;
    }

    @Override
    public AppAuthLoginRespVO weixinMiniAppLogin(AppAuthWeixinMiniAppLoginReqVO reqVO) {
        // 获得对应的手机号信息
        WxMaPhoneNumberInfo phoneNumberInfo;
        try {
            phoneNumberInfo = wxMaService.getUserService().getNewPhoneNoInfo(reqVO.getPhoneCode());
        } catch (Exception exception) {
            throw exception(AUTH_WEIXIN_MINI_APP_PHONE_CODE_ERROR);
        }
        // 获得获得注册用户
        AdminUserDO user = userService.createUserIfAbsent(phoneNumberInfo.getPurePhoneNumber(), getClientIP());
        Assert.notNull(user, "获取用户失败，结果为空");

        // 绑定社交用户
        socialUserApi.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                SocialTypeEnum.WECHAT_MINI_APP.getType(), reqVO.getLoginCode(), ""));

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, user.getMobile(), LoginLogTypeEnum.LOGIN_SOCIAL);
    }

    @Override
    public String getSocialAuthorizeUrl(Integer type, String redirectUri) {
        return null;
    }

    @Override
    public void updatePassword(Long userId, AppAuthUpdatePasswordReqVO userReqVO) {

    }

    @Override
    public void resetPassword(AppAuthResetPasswordReqVO userReqVO) {

    }

    @Override
    public void sendSmsCode(Long userId, AppAuthSmsSendReqVO reqVO) {

    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        return null;
    }

    private AdminUserDO login0(String mobile, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_MOBILE;
        // 校验账号是否存在
        AdminUserDO user = userService.getUserByMobile(mobile);
        if (user == null) {
            createLoginLog(null, mobile, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), mobile, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), mobile, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }

    private void createLoginLog(Long userId, String mobile, LoginLogTypeEnum logType, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(mobile);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogApi.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, getClientIP());
        }
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }
}
