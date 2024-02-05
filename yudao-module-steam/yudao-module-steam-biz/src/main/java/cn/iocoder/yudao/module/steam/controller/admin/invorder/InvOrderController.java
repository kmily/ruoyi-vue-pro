package cn.iocoder.yudao.module.steam.controller.admin.invorder;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.service.invorder.InvOrderService;

@Tag(name = "管理后台 - steam订单")
@RestController
@RequestMapping("/steam/inv-order")
@Validated
public class InvOrderController {

    @Resource
    private InvOrderService invOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建steam订单")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:create')")
    public CommonResult<Long> createInvOrder(@Valid @RequestBody InvOrderSaveReqVO createReqVO) {
        return success(invOrderService.createInvOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新steam订单")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:update')")
    public CommonResult<Boolean> updateInvOrder(@Valid @RequestBody InvOrderSaveReqVO updateReqVO) {
        invOrderService.updateInvOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除steam订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:inv-order:delete')")
    public CommonResult<Boolean> deleteInvOrder(@RequestParam("id") Long id) {
        invOrderService.deleteInvOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得steam订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:query')")
    public CommonResult<InvOrderRespVO> getInvOrder(@RequestParam("id") Long id) {
        InvOrderDO invOrder = invOrderService.getInvOrder(id);
        return success(BeanUtils.toBean(invOrder, InvOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得steam订单分页")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:query')")
    public CommonResult<PageResult<InvOrderRespVO>> getInvOrderPage(@Valid InvOrderPageReqVO pageReqVO) {
        PageResult<InvOrderDO> pageResult = invOrderService.getInvOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InvOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出steam订单 Excel")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:export')")
    @OperateLog(type = EXPORT)
    public void exportInvOrderExcel(@Valid InvOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InvOrderDO> list = invOrderService.getInvOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "steam订单.xls", "数据", InvOrderRespVO.class,
                        BeanUtils.toBean(list, InvOrderRespVO.class));
    }

}