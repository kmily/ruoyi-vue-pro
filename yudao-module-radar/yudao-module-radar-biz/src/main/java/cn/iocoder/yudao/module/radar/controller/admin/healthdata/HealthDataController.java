package cn.iocoder.yudao.module.radar.controller.admin.healthdata;

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

import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;

@Tag(name = "管理后台 - 体征数据")
@RestController
@RequestMapping("/radar/health-data")
@Validated
public class HealthDataController {

    @Resource
    private HealthDataService healthDataService;

    @PostMapping("/create")
    @Operation(summary = "创建体征数据")
    @PreAuthorize("@ss.hasPermission('radar:health-data:create')")
    public CommonResult<Long> createHealthData(@Valid @RequestBody HealthDataCreateReqVO createReqVO) {
        return success(healthDataService.createHealthData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新体征数据")
    @PreAuthorize("@ss.hasPermission('radar:health-data:update')")
    public CommonResult<Boolean> updateHealthData(@Valid @RequestBody HealthDataUpdateReqVO updateReqVO) {
        healthDataService.updateHealthData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体征数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('radar:health-data:delete')")
    public CommonResult<Boolean> deleteHealthData(@RequestParam("id") Long id) {
        healthDataService.deleteHealthData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体征数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('radar:health-data:query')")
    public CommonResult<HealthDataRespVO> getHealthData(@RequestParam("id") Long id) {
        HealthDataDO healthData = healthDataService.getHealthData(id);
        return success(HealthDataConvert.INSTANCE.convert(healthData));
    }

    @GetMapping("/list")
    @Operation(summary = "获得体征数据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('radar:health-data:query')")
    public CommonResult<List<HealthDataRespVO>> getHealthDataList(@RequestParam("ids") Collection<Long> ids) {
        List<HealthDataDO> list = healthDataService.getHealthDataList(ids);
        return success(HealthDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体征数据分页")
    @PreAuthorize("@ss.hasPermission('radar:health-data:query')")
    public CommonResult<PageResult<HealthDataRespVO>> getHealthDataPage(@Valid HealthDataPageReqVO pageVO) {
        PageResult<HealthDataDO> pageResult = healthDataService.getHealthDataPage(pageVO);
        return success(HealthDataConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出体征数据 Excel")
    @PreAuthorize("@ss.hasPermission('radar:health-data:export')")
    @OperateLog(type = EXPORT)
    public void exportHealthDataExcel(@Valid HealthDataExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<HealthDataDO> list = healthDataService.getHealthDataList(exportReqVO);
        // 导出 Excel
        List<HealthDataExcelVO> datas = HealthDataConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "体征数据.xls", "数据", HealthDataExcelVO.class, datas);
    }

}
