package cn.iocoder.yudao.module.scan.service.users;

import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.users.UsersConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.users.UsersMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 扫描用户 Service 实现类
 *
 * @author lyz
 */
@Service
@Validated
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMapper;

    @Override
    public Long createUsers(UsersCreateReqVO createReqVO) {
        UsersDO usersDO=usersMapper.selectOne(UsersDO::getPhone, createReqVO.getPhone());
        if(null ==usersDO){
            // 插入
            UsersDO users = UsersConvert.INSTANCE.convert(createReqVO);
            usersMapper.insert(users);
            return users.getId();
        }

        // 返回
        return usersDO.getId();
    }

    @Override
    public void updateUsers(UsersUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateUsersExists(updateReqVO.getId());
        // 更新
        UsersDO updateObj = UsersConvert.INSTANCE.convert(updateReqVO);
        usersMapper.updateById(updateObj);
    }

    @Override
    public void deleteUsers(Long id) {
        // 校验存在
        this.validateUsersExists(id);
        // 删除
        usersMapper.deleteById(id);
    }

    private void validateUsersExists(Long id) {
        if (usersMapper.selectById(id) == null) {
            throw exception(USERS_NOT_EXISTS);
        }
    }

    @Override
    public UsersDO getUsers(Long id) {
        return usersMapper.selectById(id);
    }

    @Override
    public List<UsersDO> getUsersList(Collection<Long> ids) {
        return usersMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<UsersDO> getUsersPage(UsersPageReqVO pageReqVO) {
        return usersMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UsersDO> getUsersList(UsersExportReqVO exportReqVO) {
        return usersMapper.selectList(exportReqVO);
    }

}
