package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitAreaMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品区域配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductLimitAreaServiceImpl implements ProductLimitAreaService {

    @Resource
    private ProductLimitAreaMapper productLimitAreaMapper;

    @Override
    public Long createProductLimitArea(ProductLimitAreaSaveReqVO createReqVO) {
        // 插入
        ProductLimitAreaDO productLimitArea = BeanUtils.toBean(createReqVO, ProductLimitAreaDO.class);
        productLimitAreaMapper.insert(productLimitArea);
        // 返回
        return productLimitArea.getId();
    }

    @Override
    public void updateProductLimitArea(ProductLimitAreaSaveReqVO updateReqVO) {
        // 校验存在
        validateProductLimitAreaExists(updateReqVO.getId());
        // 更新
        ProductLimitAreaDO updateObj = BeanUtils.toBean(updateReqVO, ProductLimitAreaDO.class);
        productLimitAreaMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductLimitArea(Long id) {
        // 校验存在
        validateProductLimitAreaExists(id);
        // 删除
        productLimitAreaMapper.deleteById(id);
    }

    private void validateProductLimitAreaExists(Long id) {
        if (productLimitAreaMapper.selectById(id) == null) {
            throw exception(PRODUCT_LIMIT_AREA_NOT_EXISTS);
        }
    }

    @Override
    public ProductLimitAreaDO getProductLimitArea(Long id) {
        return productLimitAreaMapper.selectById(id);
    }

    @Override
    public PageResult<ProductLimitAreaDO> getProductLimitAreaPage(ProductLimitAreaPageReqVO pageReqVO) {
        return productLimitAreaMapper.selectPage(pageReqVO);
    }

}