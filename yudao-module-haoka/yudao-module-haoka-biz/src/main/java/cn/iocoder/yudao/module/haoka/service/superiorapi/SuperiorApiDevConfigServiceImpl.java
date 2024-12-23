package cn.iocoder.yudao.module.haoka.service.superiorapi;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiDevConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 上游API接口开发配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SuperiorApiDevConfigServiceImpl implements SuperiorApiDevConfigService {

    @Resource
    private SuperiorApiDevConfigMapper superiorApiDevConfigMapper;

    @Override
    public Long createSuperiorApiDevConfig(SuperiorApiDevConfigSaveReqVO createReqVO) {
        // 插入
        SuperiorApiDevConfigDO superiorApiDevConfig = BeanUtils.toBean(createReqVO, SuperiorApiDevConfigDO.class);
        superiorApiDevConfigMapper.insert(superiorApiDevConfig);
        // 返回
        return superiorApiDevConfig.getId();
    }

    @Override
    public void updateSuperiorApiDevConfig(SuperiorApiDevConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateSuperiorApiDevConfigExists(updateReqVO.getId());
        // 更新
        SuperiorApiDevConfigDO updateObj = BeanUtils.toBean(updateReqVO, SuperiorApiDevConfigDO.class);
        superiorApiDevConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteSuperiorApiDevConfig(Long id) {
        // 校验存在
        validateSuperiorApiDevConfigExists(id);
        // 删除
        superiorApiDevConfigMapper.deleteById(id);
    }

    private void validateSuperiorApiDevConfigExists(Long id) {
        if (superiorApiDevConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_API_DEV_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public SuperiorApiDevConfigDO getSuperiorApiDevConfig(Long id) {
        return superiorApiDevConfigMapper.selectById(id);
    }

    @Override
    public PageResult<SuperiorApiDevConfigDO> getSuperiorApiDevConfigPage(SuperiorApiDevConfigPageReqVO pageReqVO) {
        return superiorApiDevConfigMapper.selectPage(pageReqVO);
    }

}