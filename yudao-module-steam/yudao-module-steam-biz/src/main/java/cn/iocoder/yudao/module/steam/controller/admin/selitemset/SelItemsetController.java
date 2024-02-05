package cn.iocoder.yudao.module.steam.controller.admin.selitemset;

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

import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import cn.iocoder.yudao.module.steam.service.selitemset.SelItemsetService;

@Tag(name = "Steam后台 - 收藏品选择")
@RestController
@RequestMapping("/steam-admin/sel-itemset")
@Validated
public class SelItemsetController {

    @Resource
    private SelItemsetService selItemsetService;

    @PostMapping("/create")
    @Operation(summary = "创建收藏品选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:create')")
    public CommonResult<Long> createSelItemset(@Valid @RequestBody SelItemsetSaveReqVO createReqVO) {
        return success(selItemsetService.createSelItemset(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收藏品选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:update')")
    public CommonResult<Boolean> updateSelItemset(@Valid @RequestBody SelItemsetSaveReqVO updateReqVO) {
        selItemsetService.updateSelItemset(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收藏品选择")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:delete')")
    public CommonResult<Boolean> deleteSelItemset(@RequestParam("id") Long id) {
        selItemsetService.deleteSelItemset(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收藏品选择")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:query')")
    public CommonResult<SelItemsetRespVO> getSelItemset(@RequestParam("id") Long id) {
        SelItemsetDO selItemset = selItemsetService.getSelItemset(id);
        return success(BeanUtils.toBean(selItemset, SelItemsetRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得收藏品选择列表")
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:query')")
    public CommonResult<List<SelItemsetRespVO>> getSelItemsetList(@Valid SelItemsetListReqVO listReqVO) {
        List<SelItemsetDO> list = selItemsetService.getSelItemsetList(listReqVO);
        return success(BeanUtils.toBean(list, SelItemsetRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收藏品选择 Excel")
    @PreAuthorize("@ss.hasPermission('steam:sel-itemset:export')")
    @OperateLog(type = EXPORT)
    public void exportSelItemsetExcel(@Valid SelItemsetListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<SelItemsetDO> list = selItemsetService.getSelItemsetList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "收藏品选择.xls", "数据", SelItemsetRespVO.class,
                        BeanUtils.toBean(list, SelItemsetRespVO.class));
    }

}