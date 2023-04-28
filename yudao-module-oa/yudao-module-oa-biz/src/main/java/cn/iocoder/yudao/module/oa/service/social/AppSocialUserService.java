package cn.iocoder.yudao.module.oa.service.social;

import cn.iocoder.yudao.module.oa.dal.dataobject.social.AppSocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;

import javax.validation.Valid;

public interface AppSocialUserService {

    /**
     * 绑定社交用户
     *
     * @param reqDTO 绑定信息
     */
    void bindSocialUser(@Valid AppSocialUserBindReqDTO reqDTO);

    SocialUserDO authSocialUser(Integer type, String code, String state);

    /**
     * 取消绑定社交用户
     *
     * @param userId 用户编号
     * @param userType 全局用户类型
     * @param type 社交平台的类型 {@link SocialTypeEnum}
     * @param openid 社交平台的 openid
     */
    void unbindSocialUser(Long userId, Integer userType, Integer type, String openid);
}
