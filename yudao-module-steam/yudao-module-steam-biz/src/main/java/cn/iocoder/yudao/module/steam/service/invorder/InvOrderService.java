package cn.iocoder.yudao.module.steam.service.invorder;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * steam订单 Service 接口
 *
 * @author 芋道源码
 */
public interface InvOrderService {

    /**
     * 创建steam订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInvOrder(@Valid InvOrderSaveReqVO createReqVO);

    /**
     * 更新steam订单
     *
     * @param updateReqVO 更新信息
     */
    void updateInvOrder(@Valid InvOrderSaveReqVO updateReqVO);

    /**
     * 删除steam订单
     *
     * @param id 编号
     */
    void deleteInvOrder(Long id);

    /**
     * 获得steam订单
     *
     * @param id 编号
     * @return steam订单
     */
    InvOrderDO getInvOrder(Long id);

    /**
     * 获得steam订单分页
     *
     * @param pageReqVO 分页查询
     * @return steam订单分页
     */
    PageResult<InvOrderDO> getInvOrderPage(InvOrderPageReqVO pageReqVO);

}