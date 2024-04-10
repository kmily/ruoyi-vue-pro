package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderCancelVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderInfoResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.service.uu.vo.CreateCommodityOrderReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.validation.Valid;

/**
 * 多平台下单接口
 *
 * @author 芋道源码
 */
public interface ApiOrderService {
    /**
     * 创建示例订单
     *
     * @param loginUser   用户
     * @param createReqVO 创建信息
     * @return 编号
     */
    ApiOrderDO createInvOrder(LoginUser loginUser, @Valid CreateCommodityOrderReqVo createReqVO);

    /**
     * 释放库存
     * @param invOrderId
     */
    void releaseInvOrder(Long invOrderId);
    /**
     * 支付订单
     * @param loginUser 前端用户
     * @param invOrderId 订单号
     * @return
     */
    YouyouOrderDO payInvOrder(LoginUser loginUser, @Valid Long invOrderId);
    /**
     * 获得订单详情
     * 订单是以买家身份进行查询
     * @param loginUser 订单用户
     * @param queryOrderReqVo 订单号
     * @return 买家的订单
     */
    ApiOrderDO getUUOrder(LoginUser loginUser, QueryOrderReqVo queryOrderReqVo);
    OrderInfoResp orderInfo(YouyouOrderDO youyouOrderDO, OpenApiReqVo<QueryOrderReqVo> openApiReqVo) throws JsonProcessingException;


    /**
     * 买家取消订单
     * @param loginUser
     * @param id
     * @param userIp
     * @param cancelReason 取消原因
     * @return
     */
    Integer orderCancel(LoginUser loginUser, OrderCancelVo id, String userIp,String cancelReason);


    void processNotify(NotifyReq notifyReq);
    void pushRemote(NotifyReq notifyReq);
    /**
     * 关闭订单,
     * 用于未支持的订单进行关闭,并释放库存
     *
     * @param invOrderId InvOrderId
     */
    void checkTransfer(Long invOrderId);
    void closeUnPayInvOrder(Long invOrderId);
}
