package cn.iocoder.yudao.module.hospital.controller.admin.careaptitude;

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

import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;
import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeCheckLogConvert;
import cn.iocoder.yudao.module.hospital.service.careaptitude.CareAptitudeCheckLogService;

@Tag(name = "管理后台 - 医护资质审核记录")
@RestController
@RequestMapping("/hospital/care-aptitude-check-log")
@Validated
public class CareAptitudeCheckLogController {

    @Resource
    private CareAptitudeCheckLogService careAptitudeCheckLogService;

    @GetMapping("/get")
    @Operation(summary = "获得医护资质审核记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:care-aptitude-check-log:query')")
    public CommonResult<CareAptitudeCheckLogRespVO> getCareAptitudeCheckLog(@RequestParam("id") Long id) {
        CareAptitudeCheckLogDO careAptitudeCheckLog = careAptitudeCheckLogService.getCareAptitudeCheckLog(id);
        return success(CareAptitudeCheckLogConvert.INSTANCE.convert(careAptitudeCheckLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得医护资质审核记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:care-aptitude-check-log:query')")
    public CommonResult<List<CareAptitudeCheckLogRespVO>> getCareAptitudeCheckLogList(@RequestParam("ids") Collection<Long> ids) {
        List<CareAptitudeCheckLogDO> list = careAptitudeCheckLogService.getCareAptitudeCheckLogList(ids);
        return success(CareAptitudeCheckLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得医护资质审核记录分页")
    @PreAuthorize("@ss.hasPermission('hospital:care-aptitude-check-log:query')")
    public CommonResult<PageResult<CareAptitudeCheckLogRespVO>> getCareAptitudeCheckLogPage(@Valid CareAptitudeCheckLogPageReqVO pageVO) {
        PageResult<CareAptitudeCheckLogDO> pageResult = careAptitudeCheckLogService.getCareAptitudeCheckLogPage(pageVO);
        return success(CareAptitudeCheckLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出医护资质审核记录 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:care-aptitude-check-log:export')")
    @OperateLog(type = EXPORT)
    public void exportCareAptitudeCheckLogExcel(@Valid CareAptitudeCheckLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CareAptitudeCheckLogDO> list = careAptitudeCheckLogService.getCareAptitudeCheckLogList(exportReqVO);
        // 导出 Excel
        List<CareAptitudeCheckLogExcelVO> datas = CareAptitudeCheckLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "医护资质审核记录.xls", "数据", CareAptitudeCheckLogExcelVO.class, datas);
    }

}
