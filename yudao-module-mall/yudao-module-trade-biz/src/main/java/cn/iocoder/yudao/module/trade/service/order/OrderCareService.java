package cn.iocoder.yudao.module.trade.service.order;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 订单医护映射 Service 接口
 *
 * @author 管理人
 */
public interface OrderCareService extends IService<OrderCareDO>{


    /**
     * 获得订单医护映射
     *
     * @param id 编号
     * @return 订单医护映射
     */
    OrderCareDO getOrderCare(Long id);

    /**
     * 获得订单医护映射列表
     *
     * @param ids 编号
     * @return 订单医护映射列表
     */
    List<OrderCareDO> getOrderCareList(Collection<Long> ids);

    /**
     * 保存映射
     * @param order
     */
    void save(TradeOrderDO order);

    /**
     * 更改状态
     * @param orderId 订单编号
     * @param careId 医护编号
     * @param status 状态
     */
    void updateByOrderIdAndCareId(Long orderId, Long careId, Integer status);
}
