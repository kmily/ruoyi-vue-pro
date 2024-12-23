package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductTypeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品类型 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductTypeMapper extends BaseMapperX<ProductTypeDO> {

    default PageResult<ProductTypeDO> selectPage(ProductTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductTypeDO>()
                .likeIfPresent(ProductTypeDO::getName, reqVO.getName())
                .betweenIfPresent(ProductTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductTypeDO::getId));
    }

}