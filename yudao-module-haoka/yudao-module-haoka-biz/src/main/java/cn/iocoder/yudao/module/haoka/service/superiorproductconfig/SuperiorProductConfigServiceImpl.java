package cn.iocoder.yudao.module.haoka.service.superiorproductconfig;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.superiorproductconfig.SuperiorProductConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品对接上游配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SuperiorProductConfigServiceImpl implements SuperiorProductConfigService {

    @Resource
    private SuperiorProductConfigMapper superiorProductConfigMapper;

    @Override
    public Long createSuperiorProductConfig(SuperiorProductConfigSaveReqVO createReqVO) {
        // 插入
        SuperiorProductConfigDO superiorProductConfig = BeanUtils.toBean(createReqVO, SuperiorProductConfigDO.class);
        superiorProductConfigMapper.insert(superiorProductConfig);
        // 返回
        return superiorProductConfig.getId();
    }

    @Override
    public void updateSuperiorProductConfig(SuperiorProductConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateSuperiorProductConfigExists(updateReqVO.getId());
        // 更新
        SuperiorProductConfigDO updateObj = BeanUtils.toBean(updateReqVO, SuperiorProductConfigDO.class);
        superiorProductConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteSuperiorProductConfig(Long id) {
        // 校验存在
        validateSuperiorProductConfigExists(id);
        // 删除
        superiorProductConfigMapper.deleteById(id);
    }

    private void validateSuperiorProductConfigExists(Long id) {
        if (superiorProductConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_PRODUCT_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public SuperiorProductConfigDO getSuperiorProductConfig(Long id) {
        return superiorProductConfigMapper.selectById(id);
    }

    @Override
    public PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(SuperiorProductConfigPageReqVO pageReqVO) {
        return superiorProductConfigMapper.selectPage(pageReqVO);
    }

}