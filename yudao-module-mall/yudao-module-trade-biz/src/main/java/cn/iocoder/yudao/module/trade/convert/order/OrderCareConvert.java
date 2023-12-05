package cn.iocoder.yudao.module.trade.convert.order;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCarePageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;

/**
 * 订单医护映射 Convert
 *
 * @author 管理人
 */
@Mapper
public interface OrderCareConvert {

    OrderCareConvert INSTANCE = Mappers.getMapper(OrderCareConvert.class);

    @Mappings({
            @Mapping(source = "id", target = "orderId")
    })
    OrderCareDO convert(TradeOrderDO bean);


    AppTradeOrderCarePageReqVO convert(TradeOrderCarePageReqVO reqVO);
}
