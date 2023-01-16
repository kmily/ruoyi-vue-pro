package cn.iocoder.yudao.module.scan.controller.admin.report;

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

import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.module.scan.convert.report.ReportConvert;
import cn.iocoder.yudao.module.scan.service.report.ReportService;

@Api(tags = "管理后台 - 报告")
@RestController
@RequestMapping("/scan/report")
@Validated
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/create")
    @ApiOperation("创建报告")
    @PreAuthorize("@ss.hasPermission('scan:report:create')")
    public CommonResult<Long> createReport(@Valid @RequestBody ReportCreateReqVO createReqVO) {
        return success(reportService.createReport(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新报告")
    @PreAuthorize("@ss.hasPermission('scan:report:update')")
    public CommonResult<Boolean> updateReport(@Valid @RequestBody ReportUpdateReqVO updateReqVO) {
        reportService.updateReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除报告")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:report:delete')")
    public CommonResult<Boolean> deleteReport(@RequestParam("id") Long id) {
        reportService.deleteReport(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得报告")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:report:query')")
    public CommonResult<ReportRespVO> getReport(@RequestParam("id") Long id) {
        ReportDO report = reportService.getReport(id);
        return success(ReportConvert.INSTANCE.convert(report));
    }

    @GetMapping("/list")
    @ApiOperation("获得报告列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:report:query')")
    public CommonResult<List<ReportRespVO>> getReportList(@RequestParam("ids") Collection<Long> ids) {
        List<ReportDO> list = reportService.getReportList(ids);
        return success(ReportConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得报告分页")
    @PreAuthorize("@ss.hasPermission('scan:report:query')")
    public CommonResult<PageResult<ReportRespVO>> getReportPage(@Valid ReportPageReqVO pageVO) {
        PageResult<ReportDO> pageResult = reportService.getReportPage(pageVO);
        return success(ReportConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出报告 Excel")
    @PreAuthorize("@ss.hasPermission('scan:report:export')")
    @OperateLog(type = EXPORT)
    public void exportReportExcel(@Valid ReportExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ReportDO> list = reportService.getReportList(exportReqVO);
        // 导出 Excel
        List<ReportExcelVO> datas = ReportConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "报告.xls", "数据", ReportExcelVO.class, datas);
    }

}
