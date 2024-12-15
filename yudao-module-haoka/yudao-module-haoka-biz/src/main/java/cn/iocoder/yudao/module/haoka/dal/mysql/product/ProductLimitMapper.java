package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品限制条件 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductLimitMapper extends BaseMapperX<ProductLimitDO> {

    default PageResult<ProductLimitDO> selectPage(ProductLimitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductLimitDO>()
                .likeIfPresent(ProductLimitDO::getName, reqVO.getName())
                .eqIfPresent(ProductLimitDO::getUseOnlySendArea, reqVO.getUseOnlySendArea())
                .eqIfPresent(ProductLimitDO::getUseNotSendArea, reqVO.getUseNotSendArea())
                .eqIfPresent(ProductLimitDO::getUseCardLimit, reqVO.getUseCardLimit())
                .eqIfPresent(ProductLimitDO::getAgeMax, reqVO.getAgeMax())
                .eqIfPresent(ProductLimitDO::getAgeMin, reqVO.getAgeMin())
                .eqIfPresent(ProductLimitDO::getPersonCardQuantityLimit, reqVO.getPersonCardQuantityLimit())
                .eqIfPresent(ProductLimitDO::getDetectionCycle, reqVO.getDetectionCycle())
                .betweenIfPresent(ProductLimitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductLimitDO::getId));
    }

}