package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductMapper;
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
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private SuperiorProductConfigMapper superiorProductConfigMapper;

    @Override
    public Long createProduct(ProductSaveReqVO createReqVO) {
        // 插入
        ProductDO product = BeanUtils.toBean(createReqVO, ProductDO.class);
        productMapper.insert(product);
        // 返回
        return product.getId();
    }

    @Override
    public void updateProduct(ProductSaveReqVO updateReqVO) {
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 更新
        ProductDO updateObj = BeanUtils.toBean(updateReqVO, ProductDO.class);
        productMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        // 校验存在
        validateProductExists(id);
        // 删除
        productMapper.deleteById(id);

        // 删除子表
        deleteSuperiorProductConfigByHaokaProductId(id);
    }

    private void validateProductExists(Long id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    @Override
    public ProductDO getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public PageResult<ProductDO> getProductPage(ProductPageReqVO pageReqVO) {
        return productMapper.selectPage(pageReqVO);
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
