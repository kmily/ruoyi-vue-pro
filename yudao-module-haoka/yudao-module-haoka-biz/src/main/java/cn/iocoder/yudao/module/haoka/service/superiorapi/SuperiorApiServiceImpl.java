package cn.iocoder.yudao.module.haoka.service.superiorapi;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiDevConfigMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiSkuConfigMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorproductconfig.SuperiorProductConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 上游API接口 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SuperiorApiServiceImpl implements SuperiorApiService {

    @Resource
    private SuperiorApiMapper superiorApiMapper;
    @Resource
    private SuperiorApiDevConfigMapper superiorApiDevConfigMapper;
    @Resource
    private SuperiorApiSkuConfigMapper superiorApiSkuConfigMapper;
    @Resource
    private SuperiorProductConfigMapper superiorProductConfigMapper;

    @Override
    public Long createSuperiorApi(SuperiorApiSaveReqVO createReqVO) {
        // 插入
        SuperiorApiDO superiorApi = BeanUtils.toBean(createReqVO, SuperiorApiDO.class);
        superiorApiMapper.insert(superiorApi);
        // 返回
        return superiorApi.getId();
    }

    @Override
    public void updateSuperiorApi(SuperiorApiSaveReqVO updateReqVO) {
        // 校验存在
        validateSuperiorApiExists(updateReqVO.getId());
        // 更新
        SuperiorApiDO updateObj = BeanUtils.toBean(updateReqVO, SuperiorApiDO.class);
        superiorApiMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSuperiorApi(Long id) {
        // 校验存在
        validateSuperiorApiExists(id);
        // 删除
        superiorApiMapper.deleteById(id);

        // 删除子表
        deleteSuperiorApiDevConfigByHaokaSuperiorApiId(id);
        deleteSuperiorApiSkuConfigByHaokaSuperiorApiId(id);
        deleteSuperiorProductConfigByHaokaSuperiorApiId(id);
    }

    private void validateSuperiorApiExists(Long id) {
        if (superiorApiMapper.selectById(id) == null) {
            throw exception(SUPERIOR_API_NOT_EXISTS);
        }
    }

    @Override
    public SuperiorApiDO getSuperiorApi(Long id) {
        return superiorApiMapper.selectById(id);
    }

    @Override
    public PageResult<SuperiorApiDO> getSuperiorApiPage(SuperiorApiPageReqVO pageReqVO) {
        return superiorApiMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（上游API接口开发配置） ====================

    @Override
    public PageResult<SuperiorApiDevConfigDO> getSuperiorApiDevConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId) {
        return superiorApiDevConfigMapper.selectPage(pageReqVO, haokaSuperiorApiId);
    }

    @Override
    public Long createSuperiorApiDevConfig(SuperiorApiDevConfigDO superiorApiDevConfig) {
        superiorApiDevConfigMapper.insert(superiorApiDevConfig);
        return superiorApiDevConfig.getId();
    }

    @Override
    public void updateSuperiorApiDevConfig(SuperiorApiDevConfigDO superiorApiDevConfig) {
        // 校验存在
        validateSuperiorApiDevConfigExists(superiorApiDevConfig.getId());
        // 更新
        superiorApiDevConfig.setUpdater(null).setUpdateTime(null); // 解决更新情况下：updateTime 不更新
        superiorApiDevConfigMapper.updateById(superiorApiDevConfig);
    }

    @Override
    public void deleteSuperiorApiDevConfig(Long id) {
        // 校验存在
        validateSuperiorApiDevConfigExists(id);
        // 删除
        superiorApiDevConfigMapper.deleteById(id);
    }

    @Override
    public SuperiorApiDevConfigDO getSuperiorApiDevConfig(Long id) {
        return superiorApiDevConfigMapper.selectById(id);
    }

    private void validateSuperiorApiDevConfigExists(Long id) {
        if (superiorApiDevConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_API_DEV_CONFIG_NOT_EXISTS);
        }
    }

    private void deleteSuperiorApiDevConfigByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        superiorApiDevConfigMapper.deleteByHaokaSuperiorApiId(haokaSuperiorApiId);
    }

    // ==================== 子表（上游API接口SKU要求配置） ====================

    @Override
    public PageResult<SuperiorApiSkuConfigDO> getSuperiorApiSkuConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId) {
        return superiorApiSkuConfigMapper.selectPage(pageReqVO, haokaSuperiorApiId);
    }

    @Override
    public Long createSuperiorApiSkuConfig(SuperiorApiSkuConfigDO superiorApiSkuConfig) {
        superiorApiSkuConfigMapper.insert(superiorApiSkuConfig);
        return superiorApiSkuConfig.getId();
    }

    @Override
    public void updateSuperiorApiSkuConfig(SuperiorApiSkuConfigDO superiorApiSkuConfig) {
        // 校验存在
        validateSuperiorApiSkuConfigExists(superiorApiSkuConfig.getId());
        // 更新
        superiorApiSkuConfig.setUpdater(null).setUpdateTime(null); // 解决更新情况下：updateTime 不更新
        superiorApiSkuConfigMapper.updateById(superiorApiSkuConfig);
    }

    @Override
    public void deleteSuperiorApiSkuConfig(Long id) {
        // 校验存在
        validateSuperiorApiSkuConfigExists(id);
        // 删除
        superiorApiSkuConfigMapper.deleteById(id);
    }

    @Override
    public SuperiorApiSkuConfigDO getSuperiorApiSkuConfig(Long id) {
        return superiorApiSkuConfigMapper.selectById(id);
    }

    private void validateSuperiorApiSkuConfigExists(Long id) {
        if (superiorApiSkuConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_API_SKU_CONFIG_NOT_EXISTS);
        }
    }

    private void deleteSuperiorApiSkuConfigByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        superiorApiSkuConfigMapper.deleteByHaokaSuperiorApiId(haokaSuperiorApiId);
    }

    // ==================== 子表（产品对接上游配置） ====================

    @Override
    public PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId) {
        return superiorProductConfigMapper.selectPage(pageReqVO, haokaSuperiorApiId);
    }

    @Override
    public Long createSuperiorProductConfig(SuperiorProductConfigDO superiorProductConfig) {
        superiorProductConfigMapper.insert(superiorProductConfig);
        return superiorProductConfig.getId();
    }

    @Override
    public void updateSuperiorProductConfig(SuperiorProductConfigDO superiorProductConfig) {
        // 校验存在
        validateSuperiorProductConfigExists(superiorProductConfig.getId());
        // 更新
        superiorProductConfig.setUpdater(null).setUpdateTime(null); // 解决更新情况下：updateTime 不更新
        superiorProductConfigMapper.updateById(superiorProductConfig);
    }

    @Override
    public void deleteSuperiorProductConfig(Long id) {
        // 校验存在
        validateSuperiorProductConfigExists(id);
        // 删除
        superiorProductConfigMapper.deleteById(id);
    }

    @Override
    public SuperiorProductConfigDO getSuperiorProductConfig(Long id) {
        return superiorProductConfigMapper.selectById(id);
    }

    private void validateSuperiorProductConfigExists(Long id) {
        if (superiorProductConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_PRODUCT_CONFIG_NOT_EXISTS);
        }
    }

    private void deleteSuperiorProductConfigByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        superiorProductConfigMapper.deleteByHaokaSuperiorApiId(haokaSuperiorApiId);
    }

}