package cn.iocoder.yudao.module.steam.controller.admin.youyouorder;

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

import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.service.youyouorder.YouyouOrderService;

@Tag(name = "管理后台 - steam订单")
@RestController
@RequestMapping("/steam/youyou-order")
@Validated
public class YouyouOrderController {

    @Resource
    private YouyouOrderService youyouOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建steam订单")
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:create')")
    public CommonResult<Long> createYouyouOrder(@Valid @RequestBody YouyouOrderSaveReqVO createReqVO) {
        return success(youyouOrderService.createYouyouOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新steam订单")
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:update')")
    public CommonResult<Boolean> updateYouyouOrder(@Valid @RequestBody YouyouOrderSaveReqVO updateReqVO) {
        youyouOrderService.updateYouyouOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除steam订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:delete')")
    public CommonResult<Boolean> deleteYouyouOrder(@RequestParam("id") Long id) {
        youyouOrderService.deleteYouyouOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得steam订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:query')")
    public CommonResult<YouyouOrderRespVO> getYouyouOrder(@RequestParam("id") Long id) {
        YouyouOrderDO youyouOrder = youyouOrderService.getYouyouOrder(id);
        return success(BeanUtils.toBean(youyouOrder, YouyouOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得steam订单分页")
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:query')")
    public CommonResult<PageResult<YouyouOrderRespVO>> getYouyouOrderPage(@Valid YouyouOrderPageReqVO pageReqVO) {
        PageResult<YouyouOrderDO> pageResult = youyouOrderService.getYouyouOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, YouyouOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出steam订单 Excel")
    @PreAuthorize("@ss.hasPermission('steam:youyou-order:export')")
    @OperateLog(type = EXPORT)
    public void exportYouyouOrderExcel(@Valid YouyouOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<YouyouOrderDO> list = youyouOrderService.getYouyouOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "steam订单.xls", "数据", YouyouOrderRespVO.class,
                        BeanUtils.toBean(list, YouyouOrderRespVO.class));
    }

}