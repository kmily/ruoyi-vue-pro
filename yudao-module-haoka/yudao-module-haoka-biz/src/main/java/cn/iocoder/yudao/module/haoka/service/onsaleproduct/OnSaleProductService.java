package cn.iocoder.yudao.module.haoka.service.onsaleproduct;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct.OnSaleProductDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 在售产品 Service 接口
 *
 * @author 芋道源码
 */
public interface OnSaleProductService {

    /**
     * 创建在售产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOnSaleProduct(@Valid OnSaleProductSaveReqVO createReqVO);

    /**
     * 更新在售产品
     *
     * @param updateReqVO 更新信息
     */
    void updateOnSaleProduct(@Valid OnSaleProductSaveReqVO updateReqVO);

    /**
     * 删除在售产品
     *
     * @param id 编号
     */
    void deleteOnSaleProduct(Long id);

    /**
     * 获得在售产品
     *
     * @param id 编号
     * @return 在售产品
     */
    OnSaleProductDO getOnSaleProduct(Long id);

    /**
     * 获得在售产品分页
     *
     * @param pageReqVO 分页查询
     * @return 在售产品分页
     */
    PageResult<OnSaleProductDO> getOnSaleProductPage(OnSaleProductPageReqVO pageReqVO);

}