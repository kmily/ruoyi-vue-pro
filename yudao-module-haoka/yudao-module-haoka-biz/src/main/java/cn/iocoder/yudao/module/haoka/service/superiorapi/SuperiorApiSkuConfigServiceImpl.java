package cn.iocoder.yudao.module.haoka.service.superiorapi;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiSkuConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 上游API接口SKU要求配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SuperiorApiSkuConfigServiceImpl implements SuperiorApiSkuConfigService {

    @Resource
    private SuperiorApiSkuConfigMapper superiorApiSkuConfigMapper;

    @Override
    public Long createSuperiorApiSkuConfig(SuperiorApiSkuConfigSaveReqVO createReqVO) {
        // 插入
        SuperiorApiSkuConfigDO superiorApiSkuConfig = BeanUtils.toBean(createReqVO, SuperiorApiSkuConfigDO.class);
        superiorApiSkuConfigMapper.insert(superiorApiSkuConfig);
        // 返回
        return superiorApiSkuConfig.getId();
    }

    @Override
    public void updateSuperiorApiSkuConfig(SuperiorApiSkuConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateSuperiorApiSkuConfigExists(updateReqVO.getId());
        // 更新
        SuperiorApiSkuConfigDO updateObj = BeanUtils.toBean(updateReqVO, SuperiorApiSkuConfigDO.class);
        superiorApiSkuConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteSuperiorApiSkuConfig(Long id) {
        // 校验存在
        validateSuperiorApiSkuConfigExists(id);
        // 删除
        superiorApiSkuConfigMapper.deleteById(id);
    }

    private void validateSuperiorApiSkuConfigExists(Long id) {
        if (superiorApiSkuConfigMapper.selectById(id) == null) {
            throw exception(SUPERIOR_API_SKU_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public SuperiorApiSkuConfigDO getSuperiorApiSkuConfig(Long id) {
        return superiorApiSkuConfigMapper.selectById(id);
    }

    @Override
    public PageResult<SuperiorApiSkuConfigDO> getSuperiorApiSkuConfigPage(SuperiorApiSkuConfigPageReqVO pageReqVO) {
        return superiorApiSkuConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SuperiorApiSkuConfigDO> getAllSuperiorApiSkuConfig(Long superiorApiId) {
        return superiorApiSkuConfigMapper.selectList(new LambdaQueryWrapperX<SuperiorApiSkuConfigDO>()
                .eqIfPresent(SuperiorApiSkuConfigDO::getHaokaSuperiorApiId, superiorApiId));
    }

}
