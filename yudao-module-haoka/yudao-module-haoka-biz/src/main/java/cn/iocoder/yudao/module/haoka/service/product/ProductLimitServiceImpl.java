package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitCardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品限制条件 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductLimitServiceImpl implements ProductLimitService {

    @Resource
    private ProductLimitMapper productLimitMapper;
    @Resource
    private ProductLimitCardMapper productLimitCardMapper;

    @Override
    public Long createProductLimit(ProductLimitSaveReqVO createReqVO) {
        // 插入
        ProductLimitDO productLimit = BeanUtils.toBean(createReqVO, ProductLimitDO.class);
        productLimitMapper.insert(productLimit);
        // 返回
        return productLimit.getId();
    }

    @Override
    public void updateProductLimit(ProductLimitSaveReqVO updateReqVO) {
        // 校验存在
        validateProductLimitExists(updateReqVO.getId());
        // 更新
        ProductLimitDO updateObj = BeanUtils.toBean(updateReqVO, ProductLimitDO.class);
        productLimitMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductLimit(Long id) {
        // 校验存在
        validateProductLimitExists(id);
        // 删除
        productLimitMapper.deleteById(id);

        // 删除子表
        deleteProductLimitCardByHaokaProductLimitId(id);
    }

    private void validateProductLimitExists(Long id) {
        if (productLimitMapper.selectById(id) == null) {
            throw exception(PRODUCT_LIMIT_NOT_EXISTS);
        }
    }

    @Override
    public ProductLimitDO getProductLimit(Long id) {
        return productLimitMapper.selectById(id);
    }

    @Override
    public PageResult<ProductLimitDO> getProductLimitPage(ProductLimitPageReqVO pageReqVO) {
        return productLimitMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（产品身份证限制） ====================

    @Override
    public PageResult<ProductLimitCardDO> getProductLimitCardPage(PageParam pageReqVO, Long haokaProductLimitId) {
        return productLimitCardMapper.selectPage(pageReqVO, haokaProductLimitId);
    }

    @Override
    public Long createProductLimitCard(ProductLimitCardDO productLimitCard) {
        productLimitCardMapper.insert(productLimitCard);
        return productLimitCard.getId();
    }

    @Override
    public void updateProductLimitCard(ProductLimitCardDO productLimitCard) {
        // 校验存在
        validateProductLimitCardExists(productLimitCard.getId());
        // 更新
        productLimitCard.setUpdater(null).setUpdateTime(null); // 解决更新情况下：updateTime 不更新
        productLimitCardMapper.updateById(productLimitCard);
    }

    @Override
    public void deleteProductLimitCard(Long id) {
        // 校验存在
        validateProductLimitCardExists(id);
        // 删除
        productLimitCardMapper.deleteById(id);
    }

    @Override
    public ProductLimitCardDO getProductLimitCard(Long id) {
        return productLimitCardMapper.selectById(id);
    }

    private void validateProductLimitCardExists(Long id) {
        if (productLimitCardMapper.selectById(id) == null) {
            throw exception(PRODUCT_LIMIT_CARD_NOT_EXISTS);
        }
    }

    private void deleteProductLimitCardByHaokaProductLimitId(Long haokaProductLimitId) {
        productLimitCardMapper.deleteByHaokaProductLimitId(haokaProductLimitId);
    }

}