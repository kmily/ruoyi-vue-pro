package cn.iocoder.yudao.module.haoka.service.ordersource;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceLiveDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单来源配置 Service 接口
 *
 * @author xiongxiong
 */
public interface OrderSourceService {

    /**
     * 创建订单来源配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrderSource(@Valid OrderSourceSaveReqVO createReqVO);

    /**
     * 更新订单来源配置
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderSource(@Valid OrderSourceSaveReqVO updateReqVO);

    /**
     * 删除订单来源配置
     *
     * @param id 编号
     */
    void deleteOrderSource(Long id);

    /**
     * 获得订单来源配置
     *
     * @param id 编号
     * @return 订单来源配置
     */
    OrderSourceDO getOrderSource(Long id);

    /**
     * 获得订单来源配置分页
     *
     * @param pageReqVO 分页查询
     * @return 订单来源配置分页
     */
    PageResult<OrderSourceDO> getOrderSourcePage(OrderSourcePageReqVO pageReqVO);

    // ==================== 子表（订单来源-直播间配置） ====================

    /**
     * 获得订单来源-直播间配置分页
     *
     * @param pageReqVO 分页查询
     * @param sourceId 来源ID
     * @return 订单来源-直播间配置分页
     */
    PageResult<OrderSourceLiveDO> getOrderSourceLivePage(PageParam pageReqVO, Long sourceId);

    /**
     * 创建订单来源-直播间配置
     *
     * @param orderSourceLive 创建信息
     * @return 编号
     */
    Long createOrderSourceLive(@Valid OrderSourceLiveDO orderSourceLive);

    /**
     * 更新订单来源-直播间配置
     *
     * @param orderSourceLive 更新信息
     */
    void updateOrderSourceLive(@Valid OrderSourceLiveDO orderSourceLive);

    /**
     * 删除订单来源-直播间配置
     *
     * @param id 编号
     */
    void deleteOrderSourceLive(Long id);

	/**
	 * 获得订单来源-直播间配置
	 *
	 * @param id 编号
     * @return 订单来源-直播间配置
	 */
    OrderSourceLiveDO getOrderSourceLive(Long id);

}