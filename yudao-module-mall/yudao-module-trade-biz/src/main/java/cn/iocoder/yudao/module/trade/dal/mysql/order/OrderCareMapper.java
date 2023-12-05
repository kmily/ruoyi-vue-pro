package cn.iocoder.yudao.module.trade.dal.mysql.order;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCarePageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.*;

/**
 * 订单医护映射 Mapper
 *
 * @author 管理人
 */
@Mapper
public interface OrderCareMapper extends BaseMapperX<OrderCareDO>,BaseMapper<OrderCareDO> {


    default PageResult<OrderCareDO> getPage(AppTradeOrderCarePageReqVO reqVO){

        return selectPage(reqVO, new LambdaQueryWrapperX<OrderCareDO>()
                .eqIfPresent(OrderCareDO::getCareId, reqVO.getCareId())
                .inIfPresent(OrderCareDO::getStatus, reqVO.queryStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime()));
    }

    default void updateByOrderIdAndStatus(Long orderId, Long careId, Integer beforeStatus, OrderCareDO orderCareDO){
        update(orderCareDO, new LambdaUpdateWrapper<OrderCareDO>().eq(OrderCareDO::getOrderId, orderId)
                .eq(OrderCareDO::getCareId, careId).eq(OrderCareDO::getStatus, beforeStatus));
    }
}
