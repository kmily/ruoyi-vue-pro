package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品区域配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductLimitAreaMapper extends BaseMapperX<ProductLimitAreaDO> {

    default PageResult<ProductLimitAreaDO> selectPage(ProductLimitAreaPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductLimitAreaDO>()
                .eqIfPresent(ProductLimitAreaDO::getHaokaProductLimitId, reqVO.getHaokaProductLimitId())
                .eqIfPresent(ProductLimitAreaDO::getAddressCode, reqVO.getAddressCode())
                .likeIfPresent(ProductLimitAreaDO::getAddressName, reqVO.getAddressName())
                .eqIfPresent(ProductLimitAreaDO::getAllowed, reqVO.getAllowed())
                .betweenIfPresent(ProductLimitAreaDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductLimitAreaDO::getId));
    }

    default List<ProductLimitAreaDO> selectListByHaokaProductLimitId(Long haokaProductLimitId) {
        return selectList(new LambdaQueryWrapperX<ProductLimitAreaDO>()
                .eqIfPresent(ProductLimitAreaDO::getHaokaProductLimitId, haokaProductLimitId));
    }

    default void deleteByHaokaProductLimitId(Long haokaProductLimitId) {
        delete(ProductLimitAreaDO::getHaokaProductLimitId, haokaProductLimitId);
    }
}
