package cn.iocoder.yudao.module.steam.service.youyouorder;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * steam订单 Service 接口
 *
 * @author 管理员
 */
public interface YouyouOrderService {

    /**
     * 创建steam订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createYouyouOrder(@Valid YouyouOrderSaveReqVO createReqVO);

    /**
     * 更新steam订单
     *
     * @param updateReqVO 更新信息
     */
    void updateYouyouOrder(@Valid YouyouOrderSaveReqVO updateReqVO);

    /**
     * 删除steam订单
     *
     * @param id 编号
     */
    void deleteYouyouOrder(Long id);

    /**
     * 获得steam订单
     *
     * @param id 编号
     * @return steam订单
     */
    YouyouOrderDO getYouyouOrder(Long id);

    /**
     * 获得steam订单分页
     *
     * @param pageReqVO 分页查询
     * @return steam订单分页
     */
    PageResult<YouyouOrderDO> getYouyouOrderPage(YouyouOrderPageReqVO pageReqVO);

}