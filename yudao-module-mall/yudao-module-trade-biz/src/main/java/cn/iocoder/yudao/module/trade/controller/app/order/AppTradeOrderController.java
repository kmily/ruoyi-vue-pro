package cn.iocoder.yudao.module.trade.controller.app.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.api.medicalcare.MedicalCareApi;
import cn.iocoder.yudao.module.hospital.api.medicalcare.dto.MedicalCareRepsDTO;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.*;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemCommentCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.yudao.module.trade.framework.order.config.TradeOrderProperties;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryExpressService;
import cn.iocoder.yudao.module.trade.service.order.OrderCareService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum.*;

@Tag(name = "用户 App - 交易订单")
@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class AppTradeOrderController {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private DeliveryExpressService deliveryExpressService;

    @Resource
    private TradeOrderProperties tradeOrderProperties;

    @Resource
    private OrderCareService orderCareService;

    @Resource
    private MedicalCareApi medicalCareApi;


    @GetMapping("/settlement")
    @Operation(summary = "获得订单结算信息")
    @PreAuthenticated
    public CommonResult<AppTradeOrderSettlementRespVO> settlementOrder(@Valid AppTradeOrderSettlementReqVO settlementReqVO) {
        return success(tradeOrderUpdateService.settlementOrder(getLoginUserId(), settlementReqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @PreAuthenticated
    public CommonResult<AppTradeOrderCreateRespVO> createOrder(@Valid @RequestBody AppTradeOrderCreateReqVO createReqVO) {
        TradeOrderDO order = tradeOrderUpdateService.createOrder(getLoginUserId(), getClientIP(), createReqVO);
        return success(new AppTradeOrderCreateRespVO().setId(order.getId()).setPayOrderId(order.getPayOrderId()));
    }

    @PostMapping("/update-paid")
    @Operation(summary = "更新订单为已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    public CommonResult<Boolean> updateOrderPaid(@RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        tradeOrderUpdateService.updateOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        return success(true);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得交易订单")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthenticated
    public CommonResult<AppTradeOrderDetailRespVO> getOrder(@RequestParam("id") Long id) {
        // 查询订单
        TradeOrderDO order = tradeOrderQueryService.getOrder(getLoginUserId(), id);
        if (order == null) {
            return success(null);
        }

        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(order.getId());
        // 查询物流公司
//        DeliveryExpressDO express = order.getLogisticsId() != null && order.getLogisticsId() > 0 ?
//                deliveryExpressService.getDeliveryExpress(order.getLogisticsId()) : null;
        // 最终组合
        AppTradeOrderDetailRespVO detailRespVO = TradeOrderConvert.INSTANCE.convert02(order, orderItems, tradeOrderProperties,  null);
        detailRespVO.setServerPerson(JSON.parseObject(order.getServicePerson(), AppServerPersonRespVO.class));
        return success(detailRespVO);
    }

    @GetMapping("/get-express-track-list")
    @Operation(summary = "获得交易订单的物流轨迹")
    @Parameter(name = "id", description = "交易订单编号")
    public CommonResult<List<?>> getOrderExpressTrackList(@RequestParam("id") Long id) {
        return success(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id, getLoginUserId())));
    }

    @GetMapping("/page")
    @Operation(summary = "获得交易订单分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppTradeOrderPageItemRespVO>> getOrderPage(AppTradeOrderPageReqVO reqVO) {
        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getOrderPage(getLoginUserId(), reqVO);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(pageResult.getList(), TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertPage02(pageResult, orderItems));
    }


    @GetMapping("/page-care")
    @Operation(summary = "医护端-分页查询订单")
    @PreAuthenticated
    public CommonResult<PageResult<AppTradeOrderPageItemRespVO>> getOrderPageByCare(@Valid AppTradeOrderCarePageReqVO reqVO){

        //MedicalCareRepsDTO care = medicalCareApi.getMedicalCareByMember(SecurityFrameworkUtils.getLoginUserId());

        PageResult<OrderCareDO> pageResult = orderCareService.getOrderPage(reqVO.setCareId(getLoginUserId()));
        if(CollUtil.isEmpty(pageResult.getList())){
            return success(PageResult.empty());
        }

        Set<Long> orderIds = convertSet(pageResult.getList(), OrderCareDO::getOrderId);

        List<TradeOrderDO> orderList = tradeOrderQueryService.getOrderList(orderIds);
        Map<Long, OrderCareDO> careDOMap = convertMap(pageResult.getList(), OrderCareDO::getOrderId);
        orderList.forEach(item -> {
            item.setStatus(careDOMap.get(item.getId()).getStatus());
        });

        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(orderList, TradeOrderDO::getId));

        return success(TradeOrderConvert.INSTANCE.convertPage02(new PageResult<TradeOrderDO>().setList(orderList).setTotal(pageResult.getTotal()), orderItems));
    }

    @GetMapping("/get-count")
    @Operation(summary = "获得交易订单数量")
    @PreAuthenticated
    public CommonResult<Map<String, Long>> getOrderCount() {
        Map<String, Long> orderCount = Maps.newLinkedHashMapWithExpectedSize(5);
        // 全部
        orderCount.put("allCount", tradeOrderQueryService.getOrderCount(getLoginUserId(), null, null));
        // 待付款（未支付）
        orderCount.put("unpaidCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.UNPAID.getStatus(), null));
        // 派单中
        orderCount.put("unassignCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                Arrays.asList(TradeOrderStatusEnum.UNABSORBED.getStatus(), UNRECEIVE.getStatus())));
        // 服务中
        orderCount.put("deliveredCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                Arrays.asList(NOSTART.getStatus(), TradeOrderStatusEnum.UNSERVER.getStatus(),
                        TradeOrderStatusEnum.SERVERING.getStatus())));
        // 待评价
        orderCount.put("uncommentedCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.COMPLETED.getStatus(), false));


        orderCount.put("cancelCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.CANCELED.getStatus(), false));

        return success(orderCount);
    }

    @PutMapping("/receive")
    @Operation(summary = "确认交易订单收货")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> receiveOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.receiveOrderByMember(getLoginUserId(), id);
        return success(true);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消交易订单")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> cancelOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.cancelOrderByMember(getLoginUserId(), id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除交易订单")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> deleteOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.deleteOrder(getLoginUserId(), id);
        return success(true);
    }

    // ========== 订单项 ==========

    @GetMapping("/item/get")
    @Operation(summary = "获得交易订单项")
    @Parameter(name = "id", description = "交易订单项编号")
    public CommonResult<AppTradeOrderItemRespVO> getOrderItem(@RequestParam("id") Long id) {
        TradeOrderItemDO item = tradeOrderQueryService.getOrderItem(getLoginUserId(), id);
        return success(TradeOrderConvert.INSTANCE.convert03(item));
    }

    @PostMapping("/item/create-comment")
    @Operation(summary = "创建交易订单项的评价")
    public CommonResult<Long> createOrderItemComment(@RequestBody AppTradeOrderItemCommentCreateReqVO createReqVO) {
        return success(tradeOrderUpdateService.createOrderItemCommentByMember(getLoginUserId(), createReqVO));
    }


    @PutMapping("/accept/{id}")
    @Operation(summary = "护士端接受订单")
    @Parameter(description = "订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> acceptOrder(@PathVariable("id") Long orderId){
        tradeOrderUpdateService.updateStatus(orderId, UNRECEIVE.getStatus(), NOSTART.getStatus());
        return success(true);
    }

    @PutMapping("/set-out/{id}")
    @Operation(summary = "护士端 出发")
    @Parameter(description = "订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> setOutOrder(@PathVariable("id") Long orderId){
        tradeOrderUpdateService.updateStatus(orderId, NOSTART.getStatus(), UNSERVER.getStatus());
        return success(true);
    }

    @PutMapping("/start/{id}")
    @Operation(summary = "护士端 开始服务")
    @Parameter(description = "订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> startOrder(@PathVariable("id") Long orderId){
        tradeOrderUpdateService.updateStatus(orderId, UNSERVER.getStatus(), SERVERING.getStatus());
        return success(true);
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "护士端 完成服务")
    @Parameter(description = "订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> completeOrder(@PathVariable("id") Long orderId){
        tradeOrderUpdateService.updateStatus(orderId, SERVERING.getStatus(), COMPLETED.getStatus());
        return success(true);
    }

    @PutMapping("/refuse/{id}")
    @Operation(summary = "护士端 拒绝服务")
    @Parameter(description = "订单编号")
    @PreAuthenticated
    public CommonResult<Boolean> refuseOrder(@PathVariable("id") Long orderId,
                                             @RequestParam("reason") String reason){
        tradeOrderUpdateService.refuseOrder(orderId, reason);
        return success(true);
    }
}
