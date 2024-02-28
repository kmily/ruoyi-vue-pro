package cn.iocoder.yudao.module.steam.controller.admin.selling;

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

import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.service.selling.SellingService;

@Tag(name = "管理后台 - 在售饰品")
@RestController
@RequestMapping("/steam/selling")
@Validated
public class SellingController {

    @Resource
    private SellingService sellingService;

    @PostMapping("/create")
    @Operation(summary = "创建在售饰品")
    @PreAuthorize("@ss.hasPermission('steam:selling:create')")
    public CommonResult<Long> createSelling(@Valid @RequestBody SellingSaveReqVO createReqVO) {
        return success(sellingService.createSelling(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新在售饰品")
    @PreAuthorize("@ss.hasPermission('steam:selling:update')")
    public CommonResult<Boolean> updateSelling(@Valid @RequestBody SellingSaveReqVO updateReqVO) {
        sellingService.updateSelling(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除在售饰品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:selling:delete')")
    public CommonResult<Boolean> deleteSelling(@RequestParam("id") Long id) {
        sellingService.deleteSelling(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得在售饰品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:selling:query')")
    public CommonResult<SellingRespVO> getSelling(@RequestParam("id") Long id) {
        SellingDO selling = sellingService.getSelling(id);
        return success(BeanUtils.toBean(selling, SellingRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得在售饰品分页")
    @PreAuthorize("@ss.hasPermission('steam:selling:query')")
    public CommonResult<PageResult<SellingRespVO>> getSellingPage(@Valid SellingPageReqVO pageReqVO) {
        PageResult<SellingDO> pageResult = sellingService.getSellingPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SellingRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出在售饰品 Excel")
    @PreAuthorize("@ss.hasPermission('steam:selling:export')")
    @OperateLog(type = EXPORT)
    public void exportSellingExcel(@Valid SellingPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SellingDO> list = sellingService.getSellingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "在售饰品.xls", "数据", SellingRespVO.class,
                        BeanUtils.toBean(list, SellingRespVO.class));
    }

}