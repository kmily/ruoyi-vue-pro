package cn.iocoder.yudao.module.oa.service.user;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.oa.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.oa.controller.app.user.vo.AppUserUpdateMobileReqVO;
import cn.iocoder.yudao.module.oa.service.auth.AppAuthService;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Service
@Valid
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminUserDO getUserByMobile(String mobile) {
        return adminUserMapper.selectByMobile(mobile);
    }

    @Override
    public List<AdminUserDO> getUserListByNickname(String nickname) {
        return adminUserMapper.selectListByNicknameLike(nickname);
    }

    @Override
    public AdminUserDO createUserIfAbsent(String mobile, String registerIp) {
        AdminUserDO user = adminUserMapper.selectByMobile(mobile);
        if(user != null) {
            return user;
        }
        return this.createUser(mobile, registerIp);
    }

    private AdminUserDO createUser(String mobile, String registerIp) {
        // 生成密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        AdminUserDO user = new AdminUserDO();
        user.setMobile(mobile);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(password)); // 加密密码
        adminUserMapper.insert(user);
        return user;
        // TODO: 2023/04/24 不允许创建
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {

    }

    @Override
    public AdminUserDO getUser(Long id) {
        return null;
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        return null;
    }

    @Override
    public void updateUserNickname(Long userId, String nickname) {

    }

    @Override
    public String updateUserAvatar(Long userId, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public void updateUserMobile(Long userId, AppUserUpdateMobileReqVO reqVO) {

    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return false;
    }
}
