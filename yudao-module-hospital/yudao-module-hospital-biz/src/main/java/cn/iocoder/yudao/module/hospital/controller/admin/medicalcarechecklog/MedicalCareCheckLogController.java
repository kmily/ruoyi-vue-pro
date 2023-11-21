package cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog;

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

import cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;
import cn.iocoder.yudao.module.hospital.convert.medicalcarechecklog.MedicalCareCheckLogConvert;
import cn.iocoder.yudao.module.hospital.service.medicalcarechecklog.MedicalCareCheckLogService;

@Tag(name = "管理后台 - 医护审核记录")
@RestController
@RequestMapping("/hospital/medical-care-check-log")
@Validated
public class MedicalCareCheckLogController {

    @Resource
    private MedicalCareCheckLogService medicalCareCheckLogService;

    @PostMapping("/create")
    @Operation(summary = "创建医护审核记录")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:create')")
    public CommonResult<Long> createMedicalCareCheckLog(@Valid @RequestBody MedicalCareCheckLogCreateReqVO createReqVO) {
        return success(medicalCareCheckLogService.createMedicalCareCheckLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新医护审核记录")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:update')")
    public CommonResult<Boolean> updateMedicalCareCheckLog(@Valid @RequestBody MedicalCareCheckLogUpdateReqVO updateReqVO) {
        medicalCareCheckLogService.updateMedicalCareCheckLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除医护审核记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:delete')")
    public CommonResult<Boolean> deleteMedicalCareCheckLog(@RequestParam("id") Long id) {
        medicalCareCheckLogService.deleteMedicalCareCheckLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得医护审核记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:query')")
    public CommonResult<MedicalCareCheckLogRespVO> getMedicalCareCheckLog(@RequestParam("id") Long id) {
        MedicalCareCheckLogDO medicalCareCheckLog = medicalCareCheckLogService.getMedicalCareCheckLog(id);
        return success(MedicalCareCheckLogConvert.INSTANCE.convert(medicalCareCheckLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得医护审核记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:query')")
    public CommonResult<List<MedicalCareCheckLogRespVO>> getMedicalCareCheckLogList(@RequestParam("ids") Collection<Long> ids) {
        List<MedicalCareCheckLogDO> list = medicalCareCheckLogService.getMedicalCareCheckLogList(ids);
        return success(MedicalCareCheckLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得医护审核记录分页")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:query')")
    public CommonResult<PageResult<MedicalCareCheckLogRespVO>> getMedicalCareCheckLogPage(@Valid MedicalCareCheckLogPageReqVO pageVO) {
        PageResult<MedicalCareCheckLogDO> pageResult = medicalCareCheckLogService.getMedicalCareCheckLogPage(pageVO);
        return success(MedicalCareCheckLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出医护审核记录 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care-check-log:export')")
    @OperateLog(type = EXPORT)
    public void exportMedicalCareCheckLogExcel(@Valid MedicalCareCheckLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MedicalCareCheckLogDO> list = medicalCareCheckLogService.getMedicalCareCheckLogList(exportReqVO);
        // 导出 Excel
        List<MedicalCareCheckLogExcelVO> datas = MedicalCareCheckLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "医护审核记录.xls", "数据", MedicalCareCheckLogExcelVO.class, datas);
    }

}
