package cn.iocoder.yudao.module.steam.controller.admin.selexterior;

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

import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import cn.iocoder.yudao.module.steam.service.selexterior.SelExteriorService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 外观选择")
@RestController
@RequestMapping("/steam/sel-exterior")
@Validated
@Resource
@Valid
public class SelExteriorController {

    @Resource
    private SelExteriorService selExteriorService;

    @PostMapping("/create")
    @Operation(summary = "创建外观选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:create')")
    public CommonResult<Long> createSelExterior(@Valid @RequestBody SelExteriorSaveReqVO createReqVO) {
        return success(selExteriorService.createSelExterior(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新外观选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:update')")
    public CommonResult<Boolean> updateSelExterior(@Valid @RequestBody SelExteriorSaveReqVO updateReqVO) {
        selExteriorService.updateSelExterior(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除外观选择")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:delete')")
    public CommonResult<Boolean> deleteSelExterior(@RequestParam("id") Long id) {
        selExteriorService.deleteSelExterior(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得外观选择")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:query')")
    public CommonResult<SelExteriorRespVO> getSelExterior(@RequestParam("id") Long id) {
        SelExteriorDO selExterior = selExteriorService.getSelExterior(id);
        return success(BeanUtils.toBean(selExterior, SelExteriorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得外观选择分页")
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:query')")
    public CommonResult<PageResult<SelExteriorRespVO>> getSelExteriorPage(@Valid SelExteriorPageReqVO pageReqVO) {
        PageResult<SelExteriorDO> pageResult = selExteriorService.getSelExteriorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SelExteriorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出外观选择 Excel")
    @PreAuthorize("@ss.hasPermission('steam:sel-exterior:export')")
    @OperateLog(type = EXPORT)
    public void exportSelExteriorExcel(@Valid SelExteriorPageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SelExteriorDO> list = selExteriorService.getSelExteriorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "外观选择.xls", "数据", SelExteriorRespVO.class,
                BeanUtils.toBean(list, SelExteriorRespVO.class));
    }

}