package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品/渠道 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductService {

    /**
     * 创建产品/渠道
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProduct(@Valid ProductSaveReqVO createReqVO);

    /**
     * 更新产品/渠道
     *
     * @param updateReqVO 更新信息
     */
    void updateProduct(@Valid ProductSaveReqVO updateReqVO);

    /**
     * 删除产品/渠道
     *
     * @param id 编号
     */
    void deleteProduct(Long id);

    /**
     * 获得产品/渠道
     *
     * @param id 编号
     * @return 产品/渠道
     */
    ProductDO getProduct(Long id);

    /**
     * 获得产品/渠道分页
     *
     * @param pageReqVO 分页查询
     * @return 产品/渠道分页
     */
    PageResult<ProductDO> getProductPage(ProductPageReqVO pageReqVO);

    // ==================== 子表（产品对接上游配置） ====================

    /**
     * 获得产品对接上游配置分页
     *
     * @param pageReqVO 分页查询
     * @param haokaProductId 产品ID
     * @return 产品对接上游配置分页
     */
    PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(PageParam pageReqVO, Long haokaProductId);

    /**
     * 创建产品对接上游配置
     *
     * @param superiorProductConfig 创建信息
     * @return 编号
     */
    Long createSuperiorProductConfig(@Valid SuperiorProductConfigDO superiorProductConfig);

    /**
     * 更新产品对接上游配置
     *
     * @param superiorProductConfig 更新信息
     */
    void updateSuperiorProductConfig(@Valid SuperiorProductConfigDO superiorProductConfig);

    /**
     * 删除产品对接上游配置
     *
     * @param id 编号
     */
    void deleteSuperiorProductConfig(Long id);

	/**
	 * 获得产品对接上游配置
	 *
	 * @param id 编号
     * @return 产品对接上游配置
	 */
    SuperiorProductConfigDO getSuperiorProductConfig(Long id);

}