package cn.iocoder.yudao.module.steam.service.binduser;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 *  steam用户绑定 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BindUserServiceImpl implements BindUserService {

    @Resource
    private BindUserMapper bindUserMapper;

    @Override
    public Long createBindUser(BindUserSaveReqVO createReqVO) {
        // 插入
        BindUserDO bindUser = BeanUtils.toBean(createReqVO, BindUserDO.class);
        bindUserMapper.insert(bindUser);
        // 返回
        return bindUser.getId();
    }

    @Override
    public void updateBindUser(BindUserSaveReqVO updateReqVO) {
        // 校验存在
        validateBindUserExists(updateReqVO.getId());
        // 更新
        BindUserDO updateObj = BeanUtils.toBean(updateReqVO, BindUserDO.class);
        bindUserMapper.updateById(updateObj);
    }

    @Override
    public void deleteBindUser(Long id) {
        // 校验存在
        validateBindUserExists(id);
        // 删除
        bindUserMapper.deleteById(id);
    }

    private void validateBindUserExists(Long id) {
        if (bindUserMapper.selectById(id) == null) {
            throw exception(BIND_USER_NOT_EXISTS);
        }
    }

    @Override
    public BindUserDO getBindUser(Long id) {
        return bindUserMapper.selectById(id);
    }

    @Override
    public PageResult<BindUserDO> getBindUserPage(BindUserPageReqVO pageReqVO) {
        return bindUserMapper.selectPage(pageReqVO);
    }

}