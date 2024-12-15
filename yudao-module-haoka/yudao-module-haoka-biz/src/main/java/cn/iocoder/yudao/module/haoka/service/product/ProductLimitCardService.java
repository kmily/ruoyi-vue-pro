package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品身份证限制 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductLimitCardService {

    /**
     * 创建产品身份证限制
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductLimitCard(@Valid ProductLimitCardSaveReqVO createReqVO);

    /**
     * 更新产品身份证限制
     *
     * @param updateReqVO 更新信息
     */
    void updateProductLimitCard(@Valid ProductLimitCardSaveReqVO updateReqVO);

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

    /**
     * 获得产品身份证限制分页
     *
     * @param pageReqVO 分页查询
     * @return 产品身份证限制分页
     */
    PageResult<ProductLimitCardDO> getProductLimitCardPage(ProductLimitCardPageReqVO pageReqVO);

}