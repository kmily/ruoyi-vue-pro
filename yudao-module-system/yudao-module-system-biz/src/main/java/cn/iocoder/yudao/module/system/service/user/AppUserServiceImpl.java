package cn.iocoder.yudao.module.system.service.user;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.system.controller.app.user.vo.AppUserUpdateMobileReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

@Service
@Valid
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private FileApi fileApi;

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
        return adminUserMapper.selectById(id);
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        return null;
    }

    @Override
    public void updateUserNickname(Long userId, String nickname) {

    }

    @VisibleForTesting
    void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        AdminUserDO user = adminUserMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
    }

    @Override
    public String updateUserAvatar(Long userId, InputStream inputStream) throws Exception {
        validateUserExists(userId);
        // 存储文件
        String avatar = fileApi.createFile(IoUtil.readBytes(inputStream));
        // 更新路径
        AdminUserDO sysUserDO = new AdminUserDO();
        sysUserDO.setId(userId);
        sysUserDO.setAvatar(avatar);
        adminUserMapper.updateById(sysUserDO);
        return avatar;
    }

    @Override
    public void updateUserMobile(Long userId, AppUserUpdateMobileReqVO reqVO) {

    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return false;
    }

}
