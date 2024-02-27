package cn.iocoder.yudao.module.steam.controller.admin.invpreview;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 饰品在售预览")
@RestController
@RequestMapping("/steam/inv-preview")
@Validated
public class InvPreviewController {

    @Resource
    private InvPreviewService invPreviewService;

    @PostMapping("/create")
    @Operation(summary = "创建饰品在售预览")
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:create')")
    public CommonResult<Long> createInvPreview(@Valid @RequestBody InvPreviewSaveReqVO createReqVO) {
        return success(invPreviewService.createInvPreview(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新饰品在售预览")
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:update')")
    public CommonResult<Boolean> updateInvPreview(@Valid @RequestBody InvPreviewSaveReqVO updateReqVO) {
        invPreviewService.updateInvPreview(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除饰品在售预览")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:delete')")
    public CommonResult<Boolean> deleteInvPreview(@RequestParam("id") Long id) {
        invPreviewService.deleteInvPreview(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得饰品在售预览")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:query')")
    public CommonResult<InvPreviewRespVO> getInvPreview(@RequestParam("id") Long id) {
        InvPreviewDO invPreview = invPreviewService.getInvPreview(id);
        return success(BeanUtils.toBean(invPreview, InvPreviewRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得饰品在售预览分页")
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:query')")
    public CommonResult<PageResult<InvPreviewRespVO>> getInvPreviewPage(@Valid InvPreviewPageReqVO pageReqVO) {
        PageResult<InvPreviewDO> pageResult = invPreviewService.getInvPreviewPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InvPreviewRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出饰品在售预览 Excel")
    @PreAuthorize("@ss.hasPermission('steam:inv-preview:export')")
    @OperateLog(type = EXPORT)
    public void exportInvPreviewExcel(@Valid InvPreviewPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InvPreviewDO> list = invPreviewService.getInvPreviewPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "饰品在售预览.xls", "数据", InvPreviewRespVO.class,
                        BeanUtils.toBean(list, InvPreviewRespVO.class));
    }

}