package cn.iocoder.yudao.module.haoka.dal.mysql.orders;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.orders.OrdersDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.orders.vo.*;

/**
 * 订单 Mapper
 *
 * @author xiongxiong
 */
@Mapper
public interface OrdersMapper extends BaseMapperX<OrdersDO> {

    default PageResult<OrdersDO> selectPage(OrdersPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrdersDO>()
                .likeIfPresent(OrdersDO::getSupplierProductName, reqVO.getSupplierProductName())
                .eqIfPresent(OrdersDO::getSupplierProductSku, reqVO.getSupplierProductSku())
                .eqIfPresent(OrdersDO::getSourceId, reqVO.getSourceId())
                .eqIfPresent(OrdersDO::getProductSku, reqVO.getProductSku())
                .eqIfPresent(OrdersDO::getSourceSku, reqVO.getSourceSku())
                .eqIfPresent(OrdersDO::getIdCardNum, reqVO.getIdCardNum())
                .eqIfPresent(OrdersDO::getAddressMobile, reqVO.getAddressMobile())
                .eqIfPresent(OrdersDO::getTrackingNumber, reqVO.getTrackingNumber())
                .eqIfPresent(OrdersDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OrdersDO::getFlag, reqVO.getFlag())
                .eqIfPresent(OrdersDO::getSource, reqVO.getSource())
                .eqIfPresent(OrdersDO::getOrderedAt, reqVO.getOrderedAt())
                .eqIfPresent(OrdersDO::getProducedAt, reqVO.getProducedAt())
                .eqIfPresent(OrdersDO::getDeliveredAt, reqVO.getDeliveredAt())
                .eqIfPresent(OrdersDO::getActivatedAt, reqVO.getActivatedAt())
                .eqIfPresent(OrdersDO::getRechargedAt, reqVO.getRechargedAt())
                .eqIfPresent(OrdersDO::getStatusUpdatedAt, reqVO.getStatusUpdatedAt())
                .eqIfPresent(OrdersDO::getRefundStatus, reqVO.getRefundStatus())
                .eqIfPresent(OrdersDO::getActiveStatus, reqVO.getActiveStatus())
                .eqIfPresent(OrdersDO::getIccid, reqVO.getIccid())
                .eqIfPresent(OrdersDO::getRealSourceId, reqVO.getRealSourceId())
                .likeIfPresent(OrdersDO::getMerchantName, reqVO.getMerchantName())
                .eqIfPresent(OrdersDO::getUpStatus, reqVO.getUpStatus())
                .eqIfPresent(OrdersDO::getUpstreamOrderId, reqVO.getUpstreamOrderId())
                .likeIfPresent(OrdersDO::getStatusName, reqVO.getStatusName())
                .orderByDesc(OrdersDO::getId));
    }

}