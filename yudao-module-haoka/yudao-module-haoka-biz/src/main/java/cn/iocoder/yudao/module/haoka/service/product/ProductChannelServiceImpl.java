package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductChannelDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductChannelMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 产品的渠道 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductChannelServiceImpl implements ProductChannelService {

    @Resource
    private ProductChannelMapper productChannelMapper;

    @Override
    public Long createProductChannel(ProductChannelSaveReqVO createReqVO) {
        // 插入
        ProductChannelDO productChannel = BeanUtils.toBean(createReqVO, ProductChannelDO.class);
        productChannelMapper.insert(productChannel);
        // 返回
        return productChannel.getId();
    }

    @Override
    public void updateProductChannel(ProductChannelSaveReqVO updateReqVO) {
        // 校验存在
        validateProductChannelExists(updateReqVO.getId());
        // 更新
        ProductChannelDO updateObj = BeanUtils.toBean(updateReqVO, ProductChannelDO.class);
        productChannelMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductChannel(Long id) {
        // 校验存在
        validateProductChannelExists(id);
        // 删除
        productChannelMapper.deleteById(id);
    }

    private void validateProductChannelExists(Long id) {
        if (productChannelMapper.selectById(id) == null) {
            throw exception(PRODUCT_CHANNEL_NOT_EXISTS);
        }
    }

    @Override
    public ProductChannelDO getProductChannel(Long id) {
        return productChannelMapper.selectById(id);
    }

    @Override
    public PageResult<ProductChannelDO> getProductChannelPage(ProductChannelPageReqVO pageReqVO) {
        return productChannelMapper.selectPage(pageReqVO);
    }

}