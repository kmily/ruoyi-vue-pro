package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品区域配置 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductLimitAreaService {

    /**
     * 创建产品区域配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductLimitArea(@Valid ProductLimitAreaSaveReqVO createReqVO);

    /**
     * 更新产品区域配置
     *
     * @param updateReqVO 更新信息
     */
    void updateProductLimitArea(@Valid ProductLimitAreaSaveReqVO updateReqVO);

    /**
     * 删除产品区域配置
     *
     * @param id 编号
     */
    void deleteProductLimitArea(Long id);

    /**
     * 获得产品区域配置
     *
     * @param id 编号
     * @return 产品区域配置
     */
    ProductLimitAreaDO getProductLimitArea(Long id);

    /**
     * 获得产品区域配置分页
     *
     * @param pageReqVO 分页查询
     * @return 产品区域配置分页
     */
    PageResult<ProductLimitAreaDO> getProductLimitAreaPage(ProductLimitAreaPageReqVO pageReqVO);

}