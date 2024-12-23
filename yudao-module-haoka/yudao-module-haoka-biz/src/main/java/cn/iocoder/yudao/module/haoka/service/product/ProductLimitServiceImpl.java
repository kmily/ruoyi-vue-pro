package cn.iocoder.yudao.module.haoka.service.product;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitAreaMapper;
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
    private ProductLimitAreaMapper productLimitAreaMapper;
    @Resource
    private ProductLimitCardMapper productLimitCardMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProductLimit(ProductLimitSaveReqVO createReqVO) {
        // 插入
        ProductLimitDO productLimit = BeanUtils.toBean(createReqVO, ProductLimitDO.class);
        productLimitMapper.insert(productLimit);

        // 插入子表
        createProductLimitAreaList(productLimit.getId(), createReqVO.getProductLimitAreas());
        createProductLimitCardList(productLimit.getId(), createReqVO.getProductLimitCards());
        // 返回
        return productLimit.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductLimit(ProductLimitSaveReqVO updateReqVO) {
        // 校验存在
        validateProductLimitExists(updateReqVO.getId());
        // 更新
        ProductLimitDO updateObj = BeanUtils.toBean(updateReqVO, ProductLimitDO.class);
        productLimitMapper.updateById(updateObj);

        // 更新子表
        updateProductLimitAreaList(updateReqVO.getId(), updateReqVO.getProductLimitAreas());
        updateProductLimitCardList(updateReqVO.getId(), updateReqVO.getProductLimitCards());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductLimit(Long id) {
        // 校验存在
        validateProductLimitExists(id);
        // 删除
        productLimitMapper.deleteById(id);

        // 删除子表
        deleteProductLimitAreaByHaokaProductLimitId(id);
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

    // ==================== 子表（产品区域配置） ====================

    @Override
    public List<ProductLimitAreaDO> getProductLimitAreaListByHaokaProductLimitId(Long haokaProductLimitId) {
        return productLimitAreaMapper.selectListByHaokaProductLimitId(haokaProductLimitId);
    }

    private void createProductLimitAreaList(Long haokaProductLimitId, List<ProductLimitAreaDO> list) {
        list.forEach(o -> o.setHaokaProductLimitId(haokaProductLimitId));
        productLimitAreaMapper.insertBatch(list);
    }

    private void updateProductLimitAreaList(Long haokaProductLimitId, List<ProductLimitAreaDO> list) {
        deleteProductLimitAreaByHaokaProductLimitId(haokaProductLimitId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createProductLimitAreaList(haokaProductLimitId, list);
    }

    private void deleteProductLimitAreaByHaokaProductLimitId(Long haokaProductLimitId) {
        productLimitAreaMapper.deleteByHaokaProductLimitId(haokaProductLimitId);
    }

    // ==================== 子表（产品身份证限制） ====================

    @Override
    public List<ProductLimitCardDO> getProductLimitCardListByHaokaProductLimitId(Long haokaProductLimitId) {
        return productLimitCardMapper.selectListByHaokaProductLimitId(haokaProductLimitId);
    }

    private void createProductLimitCardList(Long haokaProductLimitId, List<ProductLimitCardDO> list) {
        list.forEach(o -> o.setHaokaProductLimitId(haokaProductLimitId));
        productLimitCardMapper.insertBatch(list);
    }

    private void updateProductLimitCardList(Long haokaProductLimitId, List<ProductLimitCardDO> list) {
        deleteProductLimitCardByHaokaProductLimitId(haokaProductLimitId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createProductLimitCardList(haokaProductLimitId, list);
    }

    private void deleteProductLimitCardByHaokaProductLimitId(Long haokaProductLimitId) {
        productLimitCardMapper.deleteByHaokaProductLimitId(haokaProductLimitId);
    }

}