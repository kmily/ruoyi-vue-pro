package cn.iocoder.yudao.module.steam.controller.admin.adblock;

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

import cn.iocoder.yudao.module.steam.controller.admin.adblock.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.adblock.AdBlockDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;
import cn.iocoder.yudao.module.steam.service.adblock.AdBlockService;

@Tag(name = "管理后台 - 广告位")
@RestController
@RequestMapping("/steam/ad-block")
@Validated
public class AdBlockController {

    @Resource
    private AdBlockService adBlockService;

    @PostMapping("/create")
    @Operation(summary = "创建广告位")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:create')")
    public CommonResult<Long> createAdBlock(@Valid @RequestBody AdBlockSaveReqVO createReqVO) {
        return success(adBlockService.createAdBlock(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新广告位")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:update')")
    public CommonResult<Boolean> updateAdBlock(@Valid @RequestBody AdBlockSaveReqVO updateReqVO) {
        adBlockService.updateAdBlock(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除广告位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:ad-block:delete')")
    public CommonResult<Boolean> deleteAdBlock(@RequestParam("id") Long id) {
        adBlockService.deleteAdBlock(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得广告位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:query')")
    public CommonResult<AdBlockRespVO> getAdBlock(@RequestParam("id") Long id) {
        AdBlockDO adBlock = adBlockService.getAdBlock(id);
        return success(BeanUtils.toBean(adBlock, AdBlockRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得广告位分页")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:query')")
    public CommonResult<PageResult<AdBlockRespVO>> getAdBlockPage(@Valid AdBlockPageReqVO pageReqVO) {
        PageResult<AdBlockDO> pageResult = adBlockService.getAdBlockPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AdBlockRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出广告位 Excel")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:export')")
    @OperateLog(type = EXPORT)
    public void exportAdBlockExcel(@Valid AdBlockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AdBlockDO> list = adBlockService.getAdBlockPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "广告位.xls", "数据", AdBlockRespVO.class,
                        BeanUtils.toBean(list, AdBlockRespVO.class));
    }

    // ==================== 子表（广告） ====================

    @GetMapping("/ad/list-by-block-id")
    @Operation(summary = "获得广告列表")
    @Parameter(name = "blockId", description = "adID")
    @PreAuthorize("@ss.hasPermission('steam:ad-block:query')")
    public CommonResult<List<AdDO>> getAdListByBlockId(@RequestParam("blockId") Long blockId) {
        return success(adBlockService.getAdListByBlockId(blockId));
    }

}