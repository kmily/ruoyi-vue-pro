package cn.iocoder.yudao.module.radar.controller.admin.lineruledata;

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

import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import cn.iocoder.yudao.module.radar.convert.lineruledata.LineRuleDataConvert;
import cn.iocoder.yudao.module.radar.service.lineruledata.LineRuleDataService;

@Tag(name = "管理后台 - 绊线数据")
@RestController
@RequestMapping("/radar/line-rule-data")
@Validated
public class LineRuleDataController {

    @Resource
    private LineRuleDataService lineRuleDataService;

    @PostMapping("/create")
    @Operation(summary = "创建绊线数据")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:create')")
    public CommonResult<Long> createLineRuleData(@Valid @RequestBody LineRuleDataCreateReqVO createReqVO) {
        return success(lineRuleDataService.createLineRuleData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新绊线数据")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:update')")
    public CommonResult<Boolean> updateLineRuleData(@Valid @RequestBody LineRuleDataUpdateReqVO updateReqVO) {
        lineRuleDataService.updateLineRuleData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除绊线数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:delete')")
    public CommonResult<Boolean> deleteLineRuleData(@RequestParam("id") Long id) {
        lineRuleDataService.deleteLineRuleData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得绊线数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:query')")
    public CommonResult<LineRuleDataRespVO> getLineRuleData(@RequestParam("id") Long id) {
        LineRuleDataDO lineRuleData = lineRuleDataService.getLineRuleData(id);
        return success(LineRuleDataConvert.INSTANCE.convert(lineRuleData));
    }

    @GetMapping("/list")
    @Operation(summary = "获得绊线数据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:query')")
    public CommonResult<List<LineRuleDataRespVO>> getLineRuleDataList(@RequestParam("ids") Collection<Long> ids) {
        List<LineRuleDataDO> list = lineRuleDataService.getLineRuleDataList(ids);
        return success(LineRuleDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得绊线数据分页")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:query')")
    public CommonResult<PageResult<LineRuleDataRespVO>> getLineRuleDataPage(@Valid LineRuleDataPageReqVO pageVO) {
        PageResult<LineRuleDataDO> pageResult = lineRuleDataService.getLineRuleDataPage(pageVO);
        return success(LineRuleDataConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出绊线数据 Excel")
    @PreAuthorize("@ss.hasPermission('radar:line-rule-data:export')")
    @OperateLog(type = EXPORT)
    public void exportLineRuleDataExcel(@Valid LineRuleDataExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<LineRuleDataDO> list = lineRuleDataService.getLineRuleDataList(exportReqVO);
        // 导出 Excel
        List<LineRuleDataExcelVO> datas = LineRuleDataConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "绊线数据.xls", "数据", LineRuleDataExcelVO.class, datas);
    }

}
