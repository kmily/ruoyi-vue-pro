package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductChannelDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品的渠道 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductChannelMapper extends BaseMapperX<ProductChannelDO> {

    default PageResult<ProductChannelDO> selectPage(ProductChannelPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductChannelDO>()
                .likeIfPresent(ProductChannelDO::getName, reqVO.getName())
                .betweenIfPresent(ProductChannelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductChannelDO::getId));
    }

}