package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.HaoKaProductDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.HaoKaProductMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorproductconfig.SuperiorProductConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品/渠道 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HaoKaProductServiceImpl implements HaoKaProductService {

    @Resource
    private HaoKaProductMapper haoKaProductMapper;
    @Resource
    private SuperiorProductConfigMapper superiorProductConfigMapper;

    @Override
    public Long createHaoKaProduct(HaoKaProductSaveReqVO createReqVO) {
        // 插入
        HaoKaProductDO haoKaProduct = BeanUtils.toBean(createReqVO, HaoKaProductDO.class);
        haoKaProductMapper.insert(haoKaProduct);
        // 返回
        return haoKaProduct.getId();
    }

    @Override
    public void updateHaoKaProduct(HaoKaProductSaveReqVO updateReqVO) {
        // 校验存在
        validateHaoKaProductExists(updateReqVO.getId());
        // 更新
        HaoKaProductDO updateObj = BeanUtils.toBean(updateReqVO, HaoKaProductDO.class);
        haoKaProductMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHaoKaProduct(Long id) {
        // 校验存在
        validateHaoKaProductExists(id);
        // 删除
        haoKaProductMapper.deleteById(id);

        // 删除子表
        deleteSuperiorProductConfigByHaokaProductId(id);
    }

    private void validateHaoKaProductExists(Long id) {
        if (haoKaProductMapper.selectById(id) == null) {
            throw exception(HAO_KA_PRODUCT_NOT_EXISTS);
        }
    }

    @Override
    public HaoKaProductDO getHaoKaProduct(Long id) {
        return haoKaProductMapper.selectById(id);
    }

    @Override
    public PageResult<HaoKaProductDO> getHaoKaProductPage(HaoKaProductPageReqVO pageReqVO) {
        return haoKaProductMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（产品对接上游配置） ====================

    @Override
    public PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(PageParam pageReqVO, Long haokaProductId) {
        return superiorProductConfigMapper.selectPageByHaokaProductId(pageReqVO, haokaProductId);
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

    private void deleteSuperiorProductConfigByHaokaProductId(Long haokaProductId) {
        superiorProductConfigMapper.deleteByHaokaProductId(haokaProductId);
    }

}
