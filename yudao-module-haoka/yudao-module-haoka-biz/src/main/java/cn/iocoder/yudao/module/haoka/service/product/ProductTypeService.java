package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductTypeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品类型 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductTypeService {

    /**
     * 创建产品类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductType(@Valid ProductTypeSaveReqVO createReqVO);

    /**
     * 更新产品类型
     *
     * @param updateReqVO 更新信息
     */
    void updateProductType(@Valid ProductTypeSaveReqVO updateReqVO);

    /**
     * 删除产品类型
     *
     * @param id 编号
     */
    void deleteProductType(Long id);

    /**
     * 获得产品类型
     *
     * @param id 编号
     * @return 产品类型
     */
    ProductTypeDO getProductType(Long id);

    /**
     * 获得产品类型分页
     *
     * @param pageReqVO 分页查询
     * @return 产品类型分页
     */
    PageResult<ProductTypeDO> getProductTypePage(ProductTypePageReqVO pageReqVO);

}