package cn.iocoder.yudao.module.scan.controller.admin.shape;

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

import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import cn.iocoder.yudao.module.scan.convert.shape.ShapeConvert;
import cn.iocoder.yudao.module.scan.service.shape.ShapeService;

@Api(tags = "管理后台 - 体型")
@RestController
@RequestMapping("/scan/shape")
@Validated
public class ShapeController {

    @Resource
    private ShapeService shapeService;

    @PostMapping("/create")
    @ApiOperation("创建体型")
    @PreAuthorize("@ss.hasPermission('scan:shape:create')")
    public CommonResult<Long> createShape(@Valid @RequestBody ShapeCreateReqVO createReqVO) {
        return success(shapeService.createShape(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新体型")
    @PreAuthorize("@ss.hasPermission('scan:shape:update')")
    public CommonResult<Boolean> updateShape(@Valid @RequestBody ShapeUpdateReqVO updateReqVO) {
        shapeService.updateShape(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除体型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:shape:delete')")
    public CommonResult<Boolean> deleteShape(@RequestParam("id") Long id) {
        shapeService.deleteShape(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得体型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:shape:query')")
    public CommonResult<ShapeRespVO> getShape(@RequestParam("id") Long id) {
        ShapeDO shape = shapeService.getShape(id);
        return success(ShapeConvert.INSTANCE.convert(shape));
    }

    @GetMapping("/list")
    @ApiOperation("获得体型列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:shape:query')")
    public CommonResult<List<ShapeRespVO>> getShapeList(@RequestParam("ids") Collection<Long> ids) {
        List<ShapeDO> list = shapeService.getShapeList(ids);
        return success(ShapeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得体型分页")
    @PreAuthorize("@ss.hasPermission('scan:shape:query')")
    public CommonResult<PageResult<ShapeRespVO>> getShapePage(@Valid ShapePageReqVO pageVO) {
        PageResult<ShapeDO> pageResult = shapeService.getShapePage(pageVO);
        return success(ShapeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出体型 Excel")
    @PreAuthorize("@ss.hasPermission('scan:shape:export')")
    @OperateLog(type = EXPORT)
    public void exportShapeExcel(@Valid ShapeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ShapeDO> list = shapeService.getShapeList(exportReqVO);
        // 导出 Excel
        List<ShapeExcelVO> datas = ShapeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "体型.xls", "数据", ShapeExcelVO.class, datas);
    }

}
