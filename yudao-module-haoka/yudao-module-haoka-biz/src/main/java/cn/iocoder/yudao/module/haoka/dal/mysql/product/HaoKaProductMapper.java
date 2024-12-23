package cn.iocoder.yudao.module.haoka.dal.mysql.product;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.HaoKaProductDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;

/**
 * 产品/渠道 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HaoKaProductMapper extends BaseMapperX<HaoKaProductDO> {

    default PageResult<HaoKaProductDO> selectPage(HaoKaProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HaoKaProductDO>()
                .eqIfPresent(HaoKaProductDO::getOperator, reqVO.getOperator())
                .likeIfPresent(HaoKaProductDO::getSku, reqVO.getSku())
                .likeIfPresent(HaoKaProductDO::getName, reqVO.getName())
                .eqIfPresent(HaoKaProductDO::getHaokaProductTypeId, reqVO.getHaokaProductTypeId())
                .eqIfPresent(HaoKaProductDO::getBelongAreaCode, reqVO.getBelongAreaCode())
                .eqIfPresent(HaoKaProductDO::getHaokaProductChannelId, reqVO.getHaokaProductChannelId())
                .eqIfPresent(HaoKaProductDO::getHaokaProductLimitId, reqVO.getHaokaProductLimitId())
                .eqIfPresent(HaoKaProductDO::getIdCardNumVerify, reqVO.getIdCardNumVerify())
                .eqIfPresent(HaoKaProductDO::getIdCardImgVerify, reqVO.getIdCardImgVerify())
                .eqIfPresent(HaoKaProductDO::getProduceAddress, reqVO.getProduceAddress())
                .eqIfPresent(HaoKaProductDO::getNeedBlacklistFilter, reqVO.getNeedBlacklistFilter())
                .eqIfPresent(HaoKaProductDO::getEnableStockLimit, reqVO.getEnableStockLimit())
                .eqIfPresent(HaoKaProductDO::getStockNum, reqVO.getStockNum())
                .eqIfPresent(HaoKaProductDO::getStockWarnNum, reqVO.getStockWarnNum())
                .eqIfPresent(HaoKaProductDO::getProduceRemarks, reqVO.getProduceRemarks())
                .eqIfPresent(HaoKaProductDO::getSettlementRules, reqVO.getSettlementRules())
                .eqIfPresent(HaoKaProductDO::getEstimatedRevenue, reqVO.getEstimatedRevenue())
                .eqIfPresent(HaoKaProductDO::getOnSale, reqVO.getOnSale())
                .eqIfPresent(HaoKaProductDO::getIsTop, reqVO.getIsTop())
                .betweenIfPresent(HaoKaProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HaoKaProductDO::getId));
    }

}