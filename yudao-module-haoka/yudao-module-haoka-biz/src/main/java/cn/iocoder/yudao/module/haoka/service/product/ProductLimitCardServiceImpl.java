package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitCardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品身份证限制 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductLimitCardServiceImpl implements ProductLimitCardService {

    @Resource
    private ProductLimitCardMapper productLimitCardMapper;

    @Override
    public Long createProductLimitCard(ProductLimitCardSaveReqVO createReqVO) {
        // 插入
        ProductLimitCardDO productLimitCard = BeanUtils.toBean(createReqVO, ProductLimitCardDO.class);
        productLimitCardMapper.insert(productLimitCard);
        // 返回
        return productLimitCard.getId();
    }

    @Override
    public void updateProductLimitCard(ProductLimitCardSaveReqVO updateReqVO) {
        // 校验存在
        validateProductLimitCardExists(updateReqVO.getId());
        // 更新
        ProductLimitCardDO updateObj = BeanUtils.toBean(updateReqVO, ProductLimitCardDO.class);
        productLimitCardMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductLimitCard(Long id) {
        // 校验存在
        validateProductLimitCardExists(id);
        // 删除
        productLimitCardMapper.deleteById(id);
    }

    private void validateProductLimitCardExists(Long id) {
        if (productLimitCardMapper.selectById(id) == null) {
            throw exception(PRODUCT_LIMIT_CARD_NOT_EXISTS);
        }
    }

    @Override
    public ProductLimitCardDO getProductLimitCard(Long id) {
        return productLimitCardMapper.selectById(id);
    }

    @Override
    public PageResult<ProductLimitCardDO> getProductLimitCardPage(ProductLimitCardPageReqVO pageReqVO) {
        return productLimitCardMapper.selectPage(pageReqVO);
    }

}