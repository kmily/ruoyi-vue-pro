package cn.iocoder.yudao.module.system.service.auth;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.system.api.logger.LoginLogApi;
import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.yudao.module.system.api.oauth2.OAuth2TokenApi;
import cn.iocoder.yudao.module.system.api.sms.SmsCodeApi;
import cn.iocoder.yudao.module.system.api.social.SocialUserApi;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.yudao.module.system.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.system.convert.auth.AppAuthConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.module.system.service.oauth2.OAuth2TokenService;
import cn.iocoder.yudao.module.system.service.user.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.USER_PASSWORD_FAILED;

@Service
@Slf4j
public class AppAuthServiceImpl implements AppAuthService{

    @Resource
    private AppUserService userService;
    @Resource
    private SmsCodeApi smsCodeApi;
    @Resource
    private LoginLogApi loginLogApi;
    @Resource
    private SocialUserApi socialUserApi;
    @Resource
    private OAuth2TokenApi oauth2TokenApi;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private WxMaService wxMaService;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private AdminAuthService adminAuthService;
    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public AppAuthLoginRespVO login(AppAuthUsernamePasswordLoginReqVO reqVO) {
        AdminUserDO user = adminAuthService.authenticate(reqVO.getUsername(), reqVO.getPassword());

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, user.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    public AppAuthLoginRespVO phonePasswordLogin(AppAuthLoginReqVO reqVO) {
        AdminUserDO user = userMapper.selectByMobile(reqVO.getMobile());
        // 如果 socialType 非空，说明需要绑定社交用户
        if (reqVO.getSocialType() != null) {
            socialUserApi.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                    reqVO.getSocialType(), reqVO.getSocialCode(), reqVO.getSocialState()));
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE);
    }

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(AdminUserDO user, String mobile, LoginLogTypeEnum logType) {
        long userId = user.getId();
        // 插入登陆日志
        createLoginLog(userId, mobile, logType, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, null);
        // 构建返回结果
        return AppAuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public void logout(String token) {
        // 删除访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        // 删除成功，则记录登出日志
        createLogoutLog(accessTokenDO.getUserId(), accessTokenDO.getUserType(), LoginLogTypeEnum.LOGOUT_SELF.getType());
    }

    @Override
    public AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO reqVO) {
        // 校验验证码
        String userIp = getClientIP();
        smsCodeApi.useSmsCode(AppAuthConvert.INSTANCE.convert(reqVO, SmsSceneEnum.APP_LOGIN.getScene(), userIp));

        // 获得获得注册用户
        // AdminUserDO user =  userService.createUserIfAbsent(reqVO.getMobile(), userIp);
        // 不允许自动创建
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        Assert.notNull(user, "获取用户失败，结果为空");

        // 如果 socialType 非空，说明需要绑定社交用户
        if (reqVO.getSocialType() != null) {
            socialUserApi.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                    reqVO.getSocialType(), reqVO.getSocialCode(), reqVO.getSocialState()));
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_SMS);
    }

    @Override
    public AppAuthLoginRespVO socialLogin(AppAuthSocialLoginReqVO reqVO) {
        // 使用 code 授权码，进行登录。然后，获得到绑定的用户编号
        //没有绑定的用户再自动创建

        Long userId = socialUserApi.getBindUserId(UserTypeEnum.MEMBER.getValue(), reqVO.getType(),
                reqVO.getCode(), reqVO.getState());
        if (userId == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_THIRD_LOGIN_NOT_BIND);
        }

        log.info("socialLogin id:" + userId);
        AdminUserDO user = userService.getUser(userId);
        if(user == null){
            throw exception(USER_NOT_EXISTS);
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, user.getMobile(), LoginLogTypeEnum.LOGIN_SOCIAL);
    }

    @Override
    public AppAuthLoginRespVO weChatMiniAppLogin(AppAuthWeChatMiniAppLoginReqVO reqVO) {
        // 获得对应的手机号信息
        WxMaPhoneNumberInfo phoneNumberInfo;
        try {
            phoneNumberInfo = wxMaService.getUserService().getNewPhoneNoInfo(reqVO.getPhoneCode());
        } catch (Exception exception) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_WE_CHAT_MINI_APP_PHONE_CODE_ERROR);
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
    public AdminUserDO checkOldPassword(Long id, String oldPassword) {
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        // 参数：未加密密码，编码后的密码
        if (!passwordEncoder.matches(oldPassword,user.getPassword())) {
            throw exception(USER_PASSWORD_FAILED);
        }
        return user;
    }

    @Override
    public void updatePassword(Long userId, AppAuthUpdatePasswordReqVO userReqVO) {
        // 检验旧密码
        AdminUserDO userDO = checkOldPassword(userId, userReqVO.getOldPassword());

        // 更新用户密码
        userMapper.updateById(AdminUserDO.builder().id(userDO.getId())
                .password(passwordEncoder.encode(userReqVO.getPassword())).build());
    }

    public AdminUserDO checkUserIfExists(String mobile) {
        AdminUserDO user = userMapper.selectByMobile(mobile);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }

    @Override
    public void resetPassword(AppAuthResetPasswordReqVO userReqVO) {
        // 检验用户是否存在
        AdminUserDO userDO = checkUserIfExists(userReqVO.getMobile());

        // 使用验证码
        smsCodeApi.useSmsCode(AppAuthConvert.INSTANCE.convert(userReqVO, SmsSceneEnum.MEMBER_FORGET_PASSWORD,
                getClientIP()));

        // 更新密码
        userMapper.updateById(AdminUserDO.builder().id(userDO.getId())
                .password(passwordEncoder.encode(userReqVO.getPassword())).build());
    }

    @Override
    public void sendSmsCode(Long userId, AuthSmsSendReqVO reqVO) {
        adminAuthService.sendSmsCode(reqVO);
    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AppAuthConvert.INSTANCE.convert(accessTokenDO);
    }

    private AdminUserDO login0(String mobile, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_MOBILE;
        // 校验账号是否存在
        AdminUserDO user = userService.getUserByMobile(mobile);
        if (user == null) {
            createLoginLog(null, mobile, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), mobile, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), mobile, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
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

    private void createLogoutLog(Long userId, Integer userType, Integer logType) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(LoginLogTypeEnum.LOGOUT_SELF.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(getMobile(userId));
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogApi.createLoginLog(reqDTO);
    }

    private String getMobile(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getMobile() : null;
    }

    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }
}
