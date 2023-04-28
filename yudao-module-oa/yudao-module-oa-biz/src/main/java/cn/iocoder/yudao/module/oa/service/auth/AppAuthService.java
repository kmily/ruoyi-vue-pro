package cn.iocoder.yudao.module.oa.service.auth;

import cn.iocoder.yudao.module.oa.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;

import javax.validation.Valid;

public interface AppAuthService {
    /**
     * 用户名 + 密码登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO login(@Valid AppAuthUsernamePasswordLoginReqVO reqVO);

    /**
     * 手机 + 密码登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO phonePasswordLogin(@Valid AppAuthLoginReqVO reqVO);

    /**
     * 基于 token 退出登录
     *
     * @param token token
     */
    void logout(String token);

    /**
     * 手机 + 验证码登陆
     *
     * @param reqVO 登陆信息
     * @return 登录结果
     */
    AppAuthLoginRespVO smsLogin(@Valid AppAuthSmsLoginReqVO reqVO);

    /**
     * 社交登录，使用 code 授权码
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO socialLogin(@Valid AppAuthSocialLoginReqVO reqVO);

    /**
     * 微信小程序的一键登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO weChatMiniAppLogin(AppAuthWeChatMiniAppLoginReqVO reqVO);


    /**
     * 修改用户密码
     * @param userId 用户id
     * @param userReqVO 用户请求实体类
     */
    void updatePassword(Long userId, AppAuthUpdatePasswordReqVO userReqVO);

    /**
     * 忘记密码
     * @param userReqVO 用户请求实体类
     */
    void resetPassword(AppAuthResetPasswordReqVO userReqVO);

    /**
     * 给用户发送短信验证码
     *
     * @param userId 用户编号
     * @param reqVO 发送信息
     */
    void sendSmsCode(Long userId, AuthSmsSendReqVO reqVO);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AppAuthLoginRespVO refreshToken(String refreshToken);
}
