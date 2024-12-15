package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductTypeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductTypeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品类型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductTypeServiceImpl implements ProductTypeService {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Override
    public Long createProductType(ProductTypeSaveReqVO createReqVO) {
        // 插入
        ProductTypeDO productType = BeanUtils.toBean(createReqVO, ProductTypeDO.class);
        productTypeMapper.insert(productType);
        // 返回
        return productType.getId();
    }

    @Override
    public void updateProductType(ProductTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateProductTypeExists(updateReqVO.getId());
        // 更新
        ProductTypeDO updateObj = BeanUtils.toBean(updateReqVO, ProductTypeDO.class);
        productTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductType(Long id) {
        // 校验存在
        validateProductTypeExists(id);
        // 删除
        productTypeMapper.deleteById(id);
    }

    private void validateProductTypeExists(Long id) {
        if (productTypeMapper.selectById(id) == null) {
            throw exception(PRODUCT_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public ProductTypeDO getProductType(Long id) {
        return productTypeMapper.selectById(id);
    }

    @Override
    public PageResult<ProductTypeDO> getProductTypePage(ProductTypePageReqVO pageReqVO) {
        return productTypeMapper.selectPage(pageReqVO);
    }

}