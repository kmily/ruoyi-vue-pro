package cn.iocoder.yudao.module.haoka.dal.mysql.onsaleproduct;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct.OnSaleProductDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo.*;

/**
 * 在售产品 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OnSaleProductMapper extends BaseMapperX<OnSaleProductDO> {

    default PageResult<OnSaleProductDO> selectPage(OnSaleProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OnSaleProductDO>()
                .eqIfPresent(OnSaleProductDO::getParentProductId, reqVO.getParentProductId())
                .likeIfPresent(OnSaleProductDO::getName, reqVO.getName())
                .likeIfPresent(OnSaleProductDO::getSku, reqVO.getSku())
                .eqIfPresent(OnSaleProductDO::getOnSale, reqVO.getOnSale())
                .betweenIfPresent(OnSaleProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OnSaleProductDO::getId));
    }

}