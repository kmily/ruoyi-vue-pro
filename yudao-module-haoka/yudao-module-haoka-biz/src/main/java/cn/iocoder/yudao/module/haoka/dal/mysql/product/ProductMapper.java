package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品/渠道 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductMapper extends BaseMapperX<ProductDO> {

    default PageResult<ProductDO> selectPage(ProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductDO>()
                .eqIfPresent(ProductDO::getOperator, reqVO.getOperator())
                .likeIfPresent(ProductDO::getSku, reqVO.getSku())
                .likeIfPresent(ProductDO::getName, reqVO.getName())
                .eqIfPresent(ProductDO::getHaokaProductTypeId, reqVO.getHaokaProductTypeId())
                .eqIfPresent(ProductDO::getBelongAreaCode, reqVO.getBelongAreaCode())
                .eqIfPresent(ProductDO::getHaokaProductChannelId, reqVO.getHaokaProductChannelId())
                .eqIfPresent(ProductDO::getHaokaProductLimitId, reqVO.getHaokaProductLimitId())
                .eqIfPresent(ProductDO::getIdCardNumVerify, reqVO.getIdCardNumVerify())
                .eqIfPresent(ProductDO::getIdCardImgVerify, reqVO.getIdCardImgVerify())
                .eqIfPresent(ProductDO::getProduceAddress, reqVO.getProduceAddress())
                .eqIfPresent(ProductDO::getNeedBlacklistFilter, reqVO.getNeedBlacklistFilter())
                .eqIfPresent(ProductDO::getEnableStockLimit, reqVO.getEnableStockLimit())
                .eqIfPresent(ProductDO::getStockNum, reqVO.getStockNum())
                .eqIfPresent(ProductDO::getStockWarnNum, reqVO.getStockWarnNum())
                .eqIfPresent(ProductDO::getProduceRemarks, reqVO.getProduceRemarks())
                .eqIfPresent(ProductDO::getSettlementRules, reqVO.getSettlementRules())
                .eqIfPresent(ProductDO::getEstimatedRevenue, reqVO.getEstimatedRevenue())
                .eqIfPresent(ProductDO::getOnSale, reqVO.getOnSale())
                .eqIfPresent(ProductDO::getIsTop, reqVO.getIsTop())
                .betweenIfPresent(ProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductDO::getId));
    }

}