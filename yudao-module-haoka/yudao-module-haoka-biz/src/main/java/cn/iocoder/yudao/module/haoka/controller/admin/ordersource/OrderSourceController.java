package cn.iocoder.yudao.module.haoka.controller.admin.ordersource;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceLiveDO;
import cn.iocoder.yudao.module.haoka.service.ordersource.OrderSourceService;

@Tag(name = "管理后台 - 订单来源配置")
@RestController
@RequestMapping("/haoka/order-source")
@Validated
public class OrderSourceController {

    @Resource
    private OrderSourceService orderSourceService;

    @PostMapping("/create")
    @Operation(summary = "创建订单来源配置")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:create')")
    public CommonResult<Long> createOrderSource(@Valid @RequestBody OrderSourceSaveReqVO createReqVO) {
        return success(orderSourceService.createOrderSource(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单来源配置")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:update')")
    public CommonResult<Boolean> updateOrderSource(@Valid @RequestBody OrderSourceSaveReqVO updateReqVO) {
        orderSourceService.updateOrderSource(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单来源配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:order-source:delete')")
    public CommonResult<Boolean> deleteOrderSource(@RequestParam("id") Long id) {
        orderSourceService.deleteOrderSource(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单来源配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:query')")
    public CommonResult<OrderSourceRespVO> getOrderSource(@RequestParam("id") Long id) {
        OrderSourceDO orderSource = orderSourceService.getOrderSource(id);
        return success(BeanUtils.toBean(orderSource, OrderSourceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单来源配置分页")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:query')")
    public CommonResult<PageResult<OrderSourceRespVO>> getOrderSourcePage(@Valid OrderSourcePageReqVO pageReqVO) {
        PageResult<OrderSourceDO> pageResult = orderSourceService.getOrderSourcePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrderSourceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单来源配置 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderSourceExcel(@Valid OrderSourcePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderSourceDO> list = orderSourceService.getOrderSourcePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单来源配置.xls", "数据", OrderSourceRespVO.class,
                        BeanUtils.toBean(list, OrderSourceRespVO.class));
    }

    // ==================== 子表（订单来源-直播间配置） ====================

    @GetMapping("/order-source-live/page")
    @Operation(summary = "获得订单来源-直播间配置分页")
    @Parameter(name = "sourceId", description = "来源ID")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:query')")
    public CommonResult<PageResult<OrderSourceLiveDO>> getOrderSourceLivePage(PageParam pageReqVO,
                                                                                        @RequestParam("sourceId") Long sourceId) {
        return success(orderSourceService.getOrderSourceLivePage(pageReqVO, sourceId));
    }

    @PostMapping("/order-source-live/create")
    @Operation(summary = "创建订单来源-直播间配置")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:create')")
    public CommonResult<Long> createOrderSourceLive(@Valid @RequestBody OrderSourceLiveDO orderSourceLive) {
        return success(orderSourceService.createOrderSourceLive(orderSourceLive));
    }

    @PutMapping("/order-source-live/update")
    @Operation(summary = "更新订单来源-直播间配置")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:update')")
    public CommonResult<Boolean> updateOrderSourceLive(@Valid @RequestBody OrderSourceLiveDO orderSourceLive) {
        orderSourceService.updateOrderSourceLive(orderSourceLive);
        return success(true);
    }

    @DeleteMapping("/order-source-live/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除订单来源-直播间配置")
    @PreAuthorize("@ss.hasPermission('haoka:order-source:delete')")
    public CommonResult<Boolean> deleteOrderSourceLive(@RequestParam("id") Long id) {
        orderSourceService.deleteOrderSourceLive(id);
        return success(true);
    }

	@GetMapping("/order-source-live/get")
	@Operation(summary = "获得订单来源-直播间配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:order-source:query')")
	public CommonResult<OrderSourceLiveDO> getOrderSourceLive(@RequestParam("id") Long id) {
	    return success(orderSourceService.getOrderSourceLive(id));
	}

}