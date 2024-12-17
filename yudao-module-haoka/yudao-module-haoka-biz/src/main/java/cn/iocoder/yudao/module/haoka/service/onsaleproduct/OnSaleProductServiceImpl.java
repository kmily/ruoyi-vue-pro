package cn.iocoder.yudao.module.haoka.service.onsaleproduct;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct.OnSaleProductDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.onsaleproduct.OnSaleProductMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 在售产品 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OnSaleProductServiceImpl implements OnSaleProductService {

    @Resource
    private OnSaleProductMapper onSaleProductMapper;

    @Override
    public Long createOnSaleProduct(OnSaleProductSaveReqVO createReqVO) {
        // 插入
        OnSaleProductDO onSaleProduct = BeanUtils.toBean(createReqVO, OnSaleProductDO.class);
        onSaleProductMapper.insert(onSaleProduct);
        // 返回
        return onSaleProduct.getId();
    }

    @Override
    public void updateOnSaleProduct(OnSaleProductSaveReqVO updateReqVO) {
        // 校验存在
        validateOnSaleProductExists(updateReqVO.getId());
        // 更新
        OnSaleProductDO updateObj = BeanUtils.toBean(updateReqVO, OnSaleProductDO.class);
        onSaleProductMapper.updateById(updateObj);
    }

    @Override
    public void deleteOnSaleProduct(Long id) {
        // 校验存在
        validateOnSaleProductExists(id);
        // 删除
        onSaleProductMapper.deleteById(id);
    }

    private void validateOnSaleProductExists(Long id) {
        if (onSaleProductMapper.selectById(id) == null) {
            throw exception(ON_SALE_PRODUCT_NOT_EXISTS);
        }
    }

    @Override
    public OnSaleProductDO getOnSaleProduct(Long id) {
        return onSaleProductMapper.selectById(id);
    }

    @Override
    public PageResult<OnSaleProductDO> getOnSaleProductPage(OnSaleProductPageReqVO pageReqVO) {
        return onSaleProductMapper.selectPage(pageReqVO);
    }

}