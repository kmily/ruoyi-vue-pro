package cn.iocoder.yudao.module.trade.controller.admin.order;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.hospital.api.medicalcare.MedicalCareApi;
import cn.iocoder.yudao.module.hospital.api.medicalcare.dto.MedicalCareRepsDTO;
import cn.iocoder.yudao.module.member.api.serverperson.ServerPersonApi;
import cn.iocoder.yudao.module.member.api.serverperson.dto.ServerPersonRespDTO;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.trade.convert.order.OrderCareConvert;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.OrderCareDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderLogDO;
import cn.iocoder.yudao.module.trade.service.order.OrderCareService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderLogService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 交易订单")
@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class TradeOrderController {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private TradeOrderLogService tradeOrderLogService;

    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private ServerPersonApi serverPersonApi;

    @Resource
    private MedicalCareApi medicalCareApi;

    @Resource
    private OrderCareService orderCareService;


    @GetMapping("/page")
    @Operation(summary = "获得交易订单分页")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<PageResult<TradeOrderPageItemRespVO>> getOrderPage(TradeOrderPageReqVO reqVO) {
        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getOrderPage(reqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 查询用户信息
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(convertSet(pageResult.getList(), TradeOrderDO::getUserId));;
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(pageResult.getList(), TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertPage(pageResult, orderItems, userMap));
    }


    @GetMapping("/page-care")
    @Operation(summary = "获得交易订单分页")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<PageResult<TradeOrderPageItemRespVO>> getOrderCarePage(TradeOrderCarePageReqVO reqVO) {
        // 查询订单
        PageResult<OrderCareDO> pageResult = orderCareService.getOrderPage(OrderCareConvert.INSTANCE.convert(reqVO));
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        List<TradeOrderDO> orderList = tradeOrderQueryService.getOrderList(convertSet(pageResult.getList(), OrderCareDO::getOrderId));

        Map<Long, OrderCareDO> careDOMap = convertMap(pageResult.getList(), OrderCareDO::getOrderId);

        // 医护端查询的状态按 OrderCare 表中的状态为准
        orderList.forEach(item -> {
            item.setStatus(careDOMap.get(item.getId()).getStatus());
        });

       // Map<Long, MedicalCareRepsDTO> careMap = medicalCareApi.getMedicalCareMap(convertSet(orderList, TradeOrderDO::getCareId));

        // 查询用户信息
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(convertSet(orderList, TradeOrderDO::getUserId));;
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(orderList, TradeOrderDO::getId));


        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertPage(new PageResult<TradeOrderDO>().setTotal(pageResult.getTotal()).setList(orderList), orderItems, userMap));
    }



    @GetMapping("/get-detail")
    @Operation(summary = "获得交易订单详情")
    @Parameter(name = "id", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderDetailRespVO> getOrderDetail(@RequestParam("id") Long id) {
        // 查询订单
        TradeOrderDO order = tradeOrderQueryService.getOrder(id);
        if (order == null) {
            return success(null);
        }
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(id);

        // 拼接数据
        MemberUserRespDTO user = memberUserApi.getUser(order.getUserId());
        MemberUserRespDTO brokerageUser = order.getBrokerageUserId() != null ?
                memberUserApi.getUser(order.getBrokerageUserId()) : null;
        List<TradeOrderLogDO> orderLogs = tradeOrderLogService.getOrderLogListByOrderId(id);
        return success(TradeOrderConvert.INSTANCE.convert(order, orderItems, orderLogs, user, brokerageUser));
    }

    @GetMapping("/get-express-track-list")
    @Operation(summary = "获得交易订单的物流轨迹")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<List<?>> getOrderExpressTrackList(@RequestParam("id") Long id) {
        return success(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id)));
    }

    @PutMapping("/delivery")
    @Operation(summary = "订单发货")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> deliveryOrder(@RequestBody TradeOrderDeliveryReqVO deliveryReqVO) {
        tradeOrderUpdateService.deliveryOrder(deliveryReqVO);
        return success(true);
    }



    @PutMapping("/update-remark")
    @Operation(summary = "订单备注")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderRemark(@RequestBody TradeOrderRemarkReqVO reqVO) {
        tradeOrderUpdateService.updateOrderRemark(reqVO);
        return success(true);
    }

    @PutMapping("/update-price")
    @Operation(summary = "订单调价")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderPrice(@RequestBody TradeOrderUpdatePriceReqVO reqVO) {
        tradeOrderUpdateService.updateOrderPrice(reqVO);
        return success(true);
    }

    @PutMapping("/update-address")
    @Operation(summary = "修改订单收货地址")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderAddress(@RequestBody TradeOrderUpdateAddressReqVO reqVO) {
        tradeOrderUpdateService.updateOrderAddress(reqVO);
        return success(true);
    }

    @PutMapping("/pick-up")
    @Operation(summary = "订单核销")
    @PreAuthorize("@ss.hasPermission('trade:order:pick-up')")
    public CommonResult<Boolean> pickUpOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.pickUpOrder(id);
        return success(true);
    }


    @GetMapping("/unassign")
    @Operation(summary = "查询未分配订单")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<List<TradeOrderUnAssignRespVO>> getUnAssignOrder(){
        // 查询订单
        List<TradeOrderDO> tradeOrderDOList = tradeOrderQueryService.getUnAssignOrder();
        if (CollUtil.isEmpty(tradeOrderDOList)) {
            return success(new ArrayList<>());
        }
        // 查询用户信息
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(convertSet(tradeOrderDOList, TradeOrderDO::getUserId));

        Map<Long, ServerPersonRespDTO> personMap = serverPersonApi.getServerPersonMap(convertSet(tradeOrderDOList, TradeOrderDO::getServicePersonId));

        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(tradeOrderDOList, TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertList(tradeOrderDOList, orderItems, userMap, personMap));
    }


    @PutMapping("/assign")
    @Operation(summary = "订单分派")
    @PreAuthorize("@ss.hasPermission('trade:order:assign')")
    public CommonResult<Boolean> assignOrder(@RequestBody TradeOrderAssignReqVO assignReqVO) {
        tradeOrderUpdateService.assignOrder(assignReqVO);
        return success(true);
    }


}
