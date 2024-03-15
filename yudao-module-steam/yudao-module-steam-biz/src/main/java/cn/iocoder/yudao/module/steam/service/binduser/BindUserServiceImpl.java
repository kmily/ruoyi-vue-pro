package cn.iocoder.yudao.module.steam.service.binduser;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.BIND_USER_NOT_EXISTS;

/**
 *  steam用户绑定 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
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

    /**
     * 保存用户cookie 专用于steamweb更新
     * @param bindUserDO
     */
    @Override
    public void changeBindUserCookie(BindUserDO bindUserDO){
        if(Objects.isNull(bindUserDO.getId())){
            log.error("更新用户登录账号出错");
            return;
        }
        if(Objects.isNull(bindUserDO.getLoginCookie())){
            log.error("更新用户登录账号出错");
            return;
        }
        bindUserMapper.updateById(new BindUserDO().setId(bindUserDO.getId()).setLoginCookie(bindUserDO.getLoginCookie()));
    }
}