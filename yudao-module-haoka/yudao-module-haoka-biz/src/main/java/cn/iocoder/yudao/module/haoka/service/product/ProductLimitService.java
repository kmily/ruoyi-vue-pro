package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品限制条件 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductLimitService {

    /**
     * 创建产品限制条件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductLimit(@Valid ProductLimitSaveReqVO createReqVO);

    /**
     * 更新产品限制条件
     *
     * @param updateReqVO 更新信息
     */
    void updateProductLimit(@Valid ProductLimitSaveReqVO updateReqVO);

    /**
     * 删除产品限制条件
     *
     * @param id 编号
     */
    void deleteProductLimit(Long id);

    /**
     * 获得产品限制条件
     *
     * @param id 编号
     * @return 产品限制条件
     */
    ProductLimitDO getProductLimit(Long id);

    /**
     * 获得产品限制条件分页
     *
     * @param pageReqVO 分页查询
     * @return 产品限制条件分页
     */
    PageResult<ProductLimitDO> getProductLimitPage(ProductLimitPageReqVO pageReqVO);

    // ==================== 子表（产品身份证限制） ====================

    /**
     * 获得产品身份证限制分页
     *
     * @param pageReqVO 分页查询
     * @param haokaProductLimitId 产品限制ID
     * @return 产品身份证限制分页
     */
    PageResult<ProductLimitCardDO> getProductLimitCardPage(PageParam pageReqVO, Long haokaProductLimitId);

    /**
     * 创建产品身份证限制
     *
     * @param productLimitCard 创建信息
     * @return 编号
     */
    Long createProductLimitCard(@Valid ProductLimitCardDO productLimitCard);

    /**
     * 更新产品身份证限制
     *
     * @param productLimitCard 更新信息
     */
    void updateProductLimitCard(@Valid ProductLimitCardDO productLimitCard);

    /**
     * 删除产品身份证限制
     *
     * @param id 编号
     */
    void deleteProductLimitCard(Long id);

	/**
	 * 获得产品身份证限制
	 *
	 * @param id 编号
     * @return 产品身份证限制
	 */
    ProductLimitCardDO getProductLimitCard(Long id);

}