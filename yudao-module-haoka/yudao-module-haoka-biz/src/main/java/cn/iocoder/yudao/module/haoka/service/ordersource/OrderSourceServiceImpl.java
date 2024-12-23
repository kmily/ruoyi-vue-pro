package cn.iocoder.yudao.module.haoka.service.ordersource;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceLiveDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.ordersource.OrderSourceMapper;
import cn.iocoder.yudao.module.haoka.dal.mysql.ordersource.OrderSourceLiveMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 订单来源配置 Service 实现类
 *
 * @author xiongxiong
 */
@Service
@Validated
public class OrderSourceServiceImpl implements OrderSourceService {

    @Resource
    private OrderSourceMapper orderSourceMapper;
    @Resource
    private OrderSourceLiveMapper orderSourceLiveMapper;

    @Override
    public Long createOrderSource(OrderSourceSaveReqVO createReqVO) {
        // 插入
        OrderSourceDO orderSource = BeanUtils.toBean(createReqVO, OrderSourceDO.class);
        orderSourceMapper.insert(orderSource);
        // 返回
        return orderSource.getId();
    }

    @Override
    public void updateOrderSource(OrderSourceSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderSourceExists(updateReqVO.getId());
        // 更新
        OrderSourceDO updateObj = BeanUtils.toBean(updateReqVO, OrderSourceDO.class);
        orderSourceMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderSource(Long id) {
        // 校验存在
        validateOrderSourceExists(id);
        // 删除
        orderSourceMapper.deleteById(id);

        // 删除子表
        deleteOrderSourceLiveBySourceId(id);
    }

    private void validateOrderSourceExists(Long id) {
        if (orderSourceMapper.selectById(id) == null) {
            throw exception(ORDER_SOURCE_NOT_EXISTS);
        }
    }

    @Override
    public OrderSourceDO getOrderSource(Long id) {
        return orderSourceMapper.selectById(id);
    }

    @Override
    public PageResult<OrderSourceDO> getOrderSourcePage(OrderSourcePageReqVO pageReqVO) {
        return orderSourceMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（订单来源-直播间配置） ====================

    @Override
    public PageResult<OrderSourceLiveDO> getOrderSourceLivePage(PageParam pageReqVO, Long sourceId) {
        return orderSourceLiveMapper.selectPage(pageReqVO, sourceId);
    }

    @Override
    public Long createOrderSourceLive(OrderSourceLiveDO orderSourceLive) {
        orderSourceLiveMapper.insert(orderSourceLive);
        return orderSourceLive.getId();
    }

    @Override
    public void updateOrderSourceLive(OrderSourceLiveDO orderSourceLive) {
        // 校验存在
        validateOrderSourceLiveExists(orderSourceLive.getId());
        // 更新
        orderSourceLive.setUpdater(null).setUpdateTime(null); // 解决更新情况下：updateTime 不更新
        orderSourceLiveMapper.updateById(orderSourceLive);
    }

    @Override
    public void deleteOrderSourceLive(Long id) {
        // 校验存在
        validateOrderSourceLiveExists(id);
        // 删除
        orderSourceLiveMapper.deleteById(id);
    }

    @Override
    public OrderSourceLiveDO getOrderSourceLive(Long id) {
        return orderSourceLiveMapper.selectById(id);
    }

    private void validateOrderSourceLiveExists(Long id) {
        if (orderSourceLiveMapper.selectById(id) == null) {
            throw exception(ORDER_SOURCE_LIVE_NOT_EXISTS);
        }
    }

    private void deleteOrderSourceLiveBySourceId(Long sourceId) {
        orderSourceLiveMapper.deleteBySourceId(sourceId);
    }

}