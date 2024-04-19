package cn.iocoder.yudao.module.steam.controller.admin.offlinerechange;

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

import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import cn.iocoder.yudao.module.steam.service.offlinerechange.OfflineRechangeService;

@Tag(name = "管理后台 - 线下人工充值")
@RestController
@RequestMapping("/steam/offline-rechange")
@Validated
public class OfflineRechangeController {

    @Resource
    private OfflineRechangeService offlineRechangeService;

    @PostMapping("/create")
    @Operation(summary = "创建线下人工充值")
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:create')")
    public CommonResult<Long> createOfflineRechange(@Valid @RequestBody OfflineRechangeSaveReqVO createReqVO) {
        return success(offlineRechangeService.createOfflineRechange(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新线下人工充值")
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:update')")
    public CommonResult<Boolean> updateOfflineRechange(@Valid @RequestBody OfflineRechangeSaveReqVO updateReqVO) {
        offlineRechangeService.updateOfflineRechange(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除线下人工充值")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:delete')")
    public CommonResult<Boolean> deleteOfflineRechange(@RequestParam("id") Long id) {
        offlineRechangeService.deleteOfflineRechange(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得线下人工充值")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:query')")
    public CommonResult<OfflineRechangeRespVO> getOfflineRechange(@RequestParam("id") Long id) {
        OfflineRechangeDO offlineRechange = offlineRechangeService.getOfflineRechange(id);
        return success(BeanUtils.toBean(offlineRechange, OfflineRechangeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得线下人工充值分页")
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:query')")
    public CommonResult<PageResult<OfflineRechangeRespVO>> getOfflineRechangePage(@Valid OfflineRechangePageReqVO pageReqVO) {
        PageResult<OfflineRechangeDO> pageResult = offlineRechangeService.getOfflineRechangePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OfflineRechangeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出线下人工充值 Excel")
    @PreAuthorize("@ss.hasPermission('steam:offline-rechange:export')")
    @OperateLog(type = EXPORT)
    public void exportOfflineRechangeExcel(@Valid OfflineRechangePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OfflineRechangeDO> list = offlineRechangeService.getOfflineRechangePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "线下人工充值.xls", "数据", OfflineRechangeRespVO.class,
                        BeanUtils.toBean(list, OfflineRechangeRespVO.class));
    }

}