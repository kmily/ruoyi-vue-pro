package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品身份证限制 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductLimitCardMapper extends BaseMapperX<ProductLimitCardDO> {

    default PageResult<ProductLimitCardDO> selectPage(PageParam reqVO, Long haokaProductLimitId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductLimitCardDO>()
                .eq(ProductLimitCardDO::getHaokaProductLimitId, haokaProductLimitId)
                .orderByDesc(ProductLimitCardDO::getId));
    }

    default PageResult<ProductLimitCardDO> selectPage(ProductLimitCardPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<ProductLimitCardDO>()
                .eqIfPresent(ProductLimitCardDO::getHaokaProductLimitId, pageReqVO.getHaokaProductLimitId())
                .eqIfPresent(ProductLimitCardDO::getCardNum, pageReqVO.getCardNum())
                .betweenIfPresent(ProductLimitCardDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(ProductLimitCardDO::getId));
    }

    default void deleteByHaokaProductLimitId(Long haokaProductLimitId) {
        delete(ProductLimitCardDO::getHaokaProductLimitId, haokaProductLimitId);
    }

    default List<ProductLimitCardDO> selectListByHaokaProductLimitId(Long haokaProductLimitId) {
        return selectList(new LambdaQueryWrapperX<ProductLimitCardDO>()
                .eqIfPresent(ProductLimitCardDO::getHaokaProductLimitId, haokaProductLimitId));
    }
}
