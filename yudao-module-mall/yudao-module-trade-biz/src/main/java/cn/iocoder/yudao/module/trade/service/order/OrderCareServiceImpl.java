package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCarePageReqVO;
import cn.iocoder.yudao.module.trade.convert.order.OrderCareConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderAssignTypeEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.trade.dal.mysql.order.OrderCareMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 订单医护映射 Service 实现类
 *
 * @author 管理人
 */
@Service
@Validated
public class OrderCareServiceImpl extends ServiceImpl<OrderCareMapper, OrderCareDO> implements OrderCareService {

    @Resource
    private OrderCareMapper orderCareMapper;


    @Override
    public OrderCareDO getOrderCare(Long id) {
        return orderCareMapper.selectById(id);
    }

    @Override
    public List<OrderCareDO> getOrderCareList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return orderCareMapper.selectBatchIds(ids);
    }

    @Override
    public void save(TradeOrderDO order) {
        if(TradeOrderAssignTypeEnum.isUser(order.getAssignType())){
            this.save(OrderCareConvert.INSTANCE.convert(order));
        }
    }

    @Override
    public void updateByOrderIdAndCareId(Long orderId, Long careId, Integer status) {
        orderCareMapper.update(new OrderCareDO().setStatus(status), new LambdaUpdateWrapper<OrderCareDO>()
                .eq(OrderCareDO::getOrderId, orderId).eq(OrderCareDO::getCareId, careId));
    }

    @Override
    public PageResult<OrderCareDO> getOrderPage(AppTradeOrderCarePageReqVO reqVO) {
        return orderCareMapper.getPage(reqVO);
    }
}
