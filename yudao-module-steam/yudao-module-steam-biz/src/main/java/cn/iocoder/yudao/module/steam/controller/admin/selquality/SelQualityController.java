package cn.iocoder.yudao.module.steam.controller.admin.selquality;

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

import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import cn.iocoder.yudao.module.steam.service.selquality.SelQualityService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 类别选择")
@RestController
@RequestMapping("/steam/sel-quality")
@Validated
@Resource
public class SelQualityController {

    @Resource
    private SelQualityService selQualityService;

    @PostMapping("/create")
    @Operation(summary = "创建类别选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:create')")
    public CommonResult<Long> createSelQuality(@Valid @RequestBody SelQualitySaveReqVO createReqVO) {
        return success(selQualityService.createSelQuality(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新类别选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:update')")
    public CommonResult<Boolean> updateSelQuality(@Valid @RequestBody SelQualitySaveReqVO updateReqVO) {
        selQualityService.updateSelQuality(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除类别选择")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:delete')")
    public CommonResult<Boolean> deleteSelQuality(@RequestParam("id") Long id) {
        selQualityService.deleteSelQuality(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得类别选择")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:query')")
    public CommonResult<SelQualityRespVO> getSelQuality(@RequestParam("id") Long id) {
        SelQualityDO selQuality = selQualityService.getSelQuality(id);
        return success(BeanUtils.toBean(selQuality, SelQualityRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得类别选择分页")
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:query')")
    public CommonResult<PageResult<SelQualityRespVO>> getSelQualityPage(@Valid SelQualityPageReqVO pageReqVO) {
        PageResult<SelQualityDO> pageResult = selQualityService.getSelQualityPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SelQualityRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出类别选择 Excel")
    @PreAuthorize("@ss.hasPermission('steam:sel-quality:export')")
    @OperateLog(type = EXPORT)
    public void exportSelQualityExcel(@Valid SelQualityPageReqVO pageReqVO,
                                      HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SelQualityDO> list = selQualityService.getSelQualityPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "类别选择.xls", "数据", SelQualityRespVO.class,
                BeanUtils.toBean(list, SelQualityRespVO.class));
    }

}