package cn.iocoder.yudao.module.haoka.service.orders;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.orders.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单 Service 接口
 *
 * @author xiongxiong
 */
public interface OrdersService {

    /**
     * 创建订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrders(@Valid OrdersSaveReqVO createReqVO);

    /**
     * 更新订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOrders(@Valid OrdersSaveReqVO updateReqVO);

    /**
     * 删除订单
     *
     * @param id 编号
     */
    void deleteOrders(Long id);

    /**
     * 获得订单
     *
     * @param id 编号
     * @return 订单
     */
    OrdersDO getOrders(Long id);

    /**
     * 获得订单分页
     *
     * @param pageReqVO 分页查询
     * @return 订单分页
     */
    PageResult<OrdersDO> getOrdersPage(OrdersPageReqVO pageReqVO);

}