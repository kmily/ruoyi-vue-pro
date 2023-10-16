package cn.iocoder.yudao.module.product.controller.admin.unit;

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

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.product.controller.admin.unit.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.unit.UnitDO;
import cn.iocoder.yudao.module.product.convert.unit.UnitConvert;
import cn.iocoder.yudao.module.product.service.unit.UnitService;

@Tag(name = "管理后台 - 计量单位")
@RestController
@RequestMapping("/product/unit")
@Validated
public class UnitController {

    @Resource
    private UnitService unitService;

    @PostMapping("/create")
    @Operation(summary = "创建计量单位")
    @PreAuthorize("@ss.hasPermission('product:unit:create')")
    public CommonResult<Long> createUnit(@Valid @RequestBody UnitCreateReqVO createReqVO) {
        return success(unitService.createUnit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新计量单位")
    @PreAuthorize("@ss.hasPermission('product:unit:update')")
    public CommonResult<Boolean> updateUnit(@Valid @RequestBody UnitUpdateReqVO updateReqVO) {
        unitService.updateUnit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除计量单位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('product:unit:delete')")
    public CommonResult<Boolean> deleteUnit(@RequestParam("id") Long id) {
        unitService.deleteUnit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得计量单位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('product:unit:query')")
    public CommonResult<UnitRespVO> getUnit(@RequestParam("id") Long id) {
        UnitDO unit = unitService.getUnit(id);
        return success(UnitConvert.INSTANCE.convert(unit));
    }

    @GetMapping("/list")
    @Operation(summary = "获得计量单位列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('product:unit:query')")
    public CommonResult<List<UnitRespVO>> getUnitList(@RequestParam("ids") Collection<Long> ids) {
        List<UnitDO> list = unitService.getUnitList(ids);
        return success(UnitConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得计量单位分页")
    @PreAuthorize("@ss.hasPermission('product:unit:query')")
    public CommonResult<PageResult<UnitRespVO>> getUnitPage(@Valid UnitPageReqVO pageVO) {
        PageResult<UnitDO> pageResult = unitService.getUnitPage(pageVO);
        return success(UnitConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出计量单位 Excel")
    @PreAuthorize("@ss.hasPermission('product:unit:export')")
    @OperateLog(type = EXPORT)
    public void exportUnitExcel(@Valid UnitExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UnitDO> list = unitService.getUnitList(exportReqVO);
        // 导出 Excel
        List<UnitExcelVO> datas = UnitConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "计量单位.xls", "数据", UnitExcelVO.class, datas);
    }

}
