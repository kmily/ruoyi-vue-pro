package cn.iocoder.yudao.module.steam.controller.admin.selrarity;

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

import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import cn.iocoder.yudao.module.steam.service.selrarity.SelRarityService;

@Tag(name = "Steam后台 - 品质选择")
@RestController
@RequestMapping("/steam/sel-rarity")
@Validated
public class SelRarityController {

    @Resource
    private SelRarityService selRarityService;

    @PostMapping("/create")
    @Operation(summary = "创建品质选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:create')")
    public CommonResult<Long> createSelRarity(@Valid @RequestBody SelRaritySaveReqVO createReqVO) {
        return success(selRarityService.createSelRarity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新品质选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:update')")
    public CommonResult<Boolean> updateSelRarity(@Valid @RequestBody SelRaritySaveReqVO updateReqVO) {
        selRarityService.updateSelRarity(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除品质选择")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:delete')")
    public CommonResult<Boolean> deleteSelRarity(@RequestParam("id") Long id) {
        selRarityService.deleteSelRarity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得品质选择")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:query')")
    public CommonResult<SelRarityRespVO> getSelRarity(@RequestParam("id") Long id) {
        SelRarityDO selRarity = selRarityService.getSelRarity(id);
        return success(BeanUtils.toBean(selRarity, SelRarityRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得品质选择分页")
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:query')")
    public CommonResult<PageResult<SelRarityRespVO>> getSelRarityPage(@Valid SelRarityPageReqVO pageReqVO) {
        PageResult<SelRarityDO> pageResult = selRarityService.getSelRarityPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SelRarityRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出品质选择 Excel")
    @PreAuthorize("@ss.hasPermission('steam:sel-rarity:export')")
    @OperateLog(type = EXPORT)
    public void exportSelRarityExcel(@Valid SelRarityPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SelRarityDO> list = selRarityService.getSelRarityPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "品质选择.xls", "数据", SelRarityRespVO.class,
                        BeanUtils.toBean(list, SelRarityRespVO.class));
    }

}