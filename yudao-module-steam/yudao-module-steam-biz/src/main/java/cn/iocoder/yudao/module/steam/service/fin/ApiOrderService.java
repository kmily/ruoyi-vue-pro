package cn.iocoder.yudao.module.steam.service.fin;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.Io661OrderInfoResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.OrderCancelVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiSummaryByHashName;

import javax.validation.Valid;
import java.util.List;

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
    ApiOrderDO createInvOrder(LoginUser loginUser, @Valid ApiQueryCommodityReqVo createReqVO);

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
    ApiOrderDO payInvOrder(LoginUser loginUser, @Valid Long invOrderId);
    /**
     * 获得订单详情
     * 订单是以买家身份进行查询
     * @param loginUser 订单用户
     * @param queryOrderReqVo 订单号
     * @return 买家的订单
     */
    ApiOrderDO getOrder(LoginUser loginUser, QueryOrderReqVo queryOrderReqVo);
    /**
     * 获取订单详情
     * @param reqVo 订单入参
     * @param loginUser 用户
     * @return
     */
    Io661OrderInfoResp getOrderInfo(QueryOrderReqVo reqVo, LoginUser loginUser);

    /**
     * 买家取消订单.
     *
     *      * 取消结果：
     *      * 1、取消成功；
     *      * 2、处理中；
     *      * 3、取消失败。
     *
     * @param loginUser
     * @param id
     * @param userIp
     * @param cancelReason 取消原因
     * @return
     */
    Integer orderCancel(LoginUser loginUser, OrderCancelVo id, String userIp,String cancelReason);


    void processNotify(String jsonData, PlatCodeEnum platCodeEnum,String msgNo);

    /**
     * 推送到栽种
     * @param notifyId
     */
    void pushRemote(Long notifyId);
    /**
     * 关闭订单,
     * 用于未支持的订单进行关闭,并释放库存
     *
     * @param invOrderId InvOrderId
     */
    void checkTransfer(Long invOrderId);
    void closeUnPayInvOrder(Long invOrderId);

    /**
     * 批量查询模板在售信息
     * @param marketHashName
     * @return
     */
    List<ApiSummaryByHashName> summaryByHashName(LoginUser loginUser, List<String> marketHashName);
}
