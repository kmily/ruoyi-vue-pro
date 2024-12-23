package cn.iocoder.yudao.module.haoka.service.product;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductChannelDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品的渠道 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductChannelService {

    /**
     * 创建产品的渠道
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductChannel(@Valid ProductChannelSaveReqVO createReqVO);

    /**
     * 更新产品的渠道
     *
     * @param updateReqVO 更新信息
     */
    void updateProductChannel(@Valid ProductChannelSaveReqVO updateReqVO);

    /**
     * 删除产品的渠道
     *
     * @param id 编号
     */
    void deleteProductChannel(Long id);

    /**
     * 获得产品的渠道
     *
     * @param id 编号
     * @return 产品的渠道
     */
    ProductChannelDO getProductChannel(Long id);

    /**
     * 获得产品的渠道分页
     *
     * @param pageReqVO 分页查询
     * @return 产品的渠道分页
     */
    PageResult<ProductChannelDO> getProductChannelPage(ProductChannelPageReqVO pageReqVO);

}