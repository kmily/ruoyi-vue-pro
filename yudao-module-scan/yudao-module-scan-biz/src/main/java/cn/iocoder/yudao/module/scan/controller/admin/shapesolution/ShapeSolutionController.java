package cn.iocoder.yudao.module.scan.controller.admin.shapesolution;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

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

import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import cn.iocoder.yudao.module.scan.convert.shapesolution.ShapeSolutionConvert;
import cn.iocoder.yudao.module.scan.service.shapesolution.ShapeSolutionService;

@Api(tags = "管理后台 - 体型解决方案")
@RestController
@RequestMapping("/scan/shape-solution")
@Validated
public class ShapeSolutionController {

    @Resource
    private ShapeSolutionService shapeSolutionService;

    @PostMapping("/create")
    @ApiOperation("创建体型解决方案")
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:create')")
    public CommonResult<Long> createShapeSolution(@Valid @RequestBody ShapeSolutionCreateReqVO createReqVO) {
        return success(shapeSolutionService.createShapeSolution(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新体型解决方案")
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:update')")
    public CommonResult<Boolean> updateShapeSolution(@Valid @RequestBody ShapeSolutionUpdateReqVO updateReqVO) {
        shapeSolutionService.updateShapeSolution(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除体型解决方案")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:delete')")
    public CommonResult<Boolean> deleteShapeSolution(@RequestParam("id") Long id) {
        shapeSolutionService.deleteShapeSolution(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得体型解决方案")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:query')")
    public CommonResult<ShapeSolutionRespVO> getShapeSolution(@RequestParam("id") Long id) {
        ShapeSolutionDO shapeSolution = shapeSolutionService.getShapeSolution(id);
        return success(ShapeSolutionConvert.INSTANCE.convert(shapeSolution));
    }

    @GetMapping("/list")
    @ApiOperation("获得体型解决方案列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:query')")
    public CommonResult<List<ShapeSolutionRespVO>> getShapeSolutionList(@RequestParam("ids") Collection<Long> ids) {
        List<ShapeSolutionDO> list = shapeSolutionService.getShapeSolutionList(ids);
        return success(ShapeSolutionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得体型解决方案分页")
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:query')")
    public CommonResult<PageResult<ShapeSolutionRespVO>> getShapeSolutionPage(@Valid ShapeSolutionPageReqVO pageVO) {
        PageResult<ShapeSolutionDO> pageResult = shapeSolutionService.getShapeSolutionPage(pageVO);
        return success(ShapeSolutionConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出体型解决方案 Excel")
    @PreAuthorize("@ss.hasPermission('scan:shape-solution:export')")
    @OperateLog(type = EXPORT)
    public void exportShapeSolutionExcel(@Valid ShapeSolutionExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ShapeSolutionDO> list = shapeSolutionService.getShapeSolutionList(exportReqVO);
        // 导出 Excel
        List<ShapeSolutionExcelVO> datas = ShapeSolutionConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "体型解决方案.xls", "数据", ShapeSolutionExcelVO.class, datas);
    }

}
