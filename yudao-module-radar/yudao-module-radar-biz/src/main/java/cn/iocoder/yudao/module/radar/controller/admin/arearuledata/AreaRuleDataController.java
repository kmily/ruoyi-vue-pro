package cn.iocoder.yudao.module.radar.controller.admin.arearuledata;

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

import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata.AreaRuleDataDO;
import cn.iocoder.yudao.module.radar.convert.arearuledata.AreaRuleDataConvert;
import cn.iocoder.yudao.module.radar.service.arearuledata.AreaRuleDataService;

@Tag(name = "管理后台 - 区域数据")
@RestController
@RequestMapping("/radar/area-rule-data")
@Validated
public class AreaRuleDataController {

    @Resource
    private AreaRuleDataService areaRuleDataService;

    @PostMapping("/create")
    @Operation(summary = "创建区域数据")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:create')")
    public CommonResult<Long> createAreaRuleData(@Valid @RequestBody AreaRuleDataCreateReqVO createReqVO) {
        return success(areaRuleDataService.createAreaRuleData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新区域数据")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:update')")
    public CommonResult<Boolean> updateAreaRuleData(@Valid @RequestBody AreaRuleDataUpdateReqVO updateReqVO) {
        areaRuleDataService.updateAreaRuleData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除区域数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:delete')")
    public CommonResult<Boolean> deleteAreaRuleData(@RequestParam("id") Long id) {
        areaRuleDataService.deleteAreaRuleData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得区域数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:query')")
    public CommonResult<AreaRuleDataRespVO> getAreaRuleData(@RequestParam("id") Long id) {
        AreaRuleDataDO areaRuleData = areaRuleDataService.getAreaRuleData(id);
        return success(AreaRuleDataConvert.INSTANCE.convert(areaRuleData));
    }

    @GetMapping("/list")
    @Operation(summary = "获得区域数据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:query')")
    public CommonResult<List<AreaRuleDataRespVO>> getAreaRuleDataList(@RequestParam("ids") Collection<Long> ids) {
        List<AreaRuleDataDO> list = areaRuleDataService.getAreaRuleDataList(ids);
        return success(AreaRuleDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得区域数据分页")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:query')")
    public CommonResult<PageResult<AreaRuleDataRespVO>> getAreaRuleDataPage(@Valid AreaRuleDataPageReqVO pageVO) {
        PageResult<AreaRuleDataDO> pageResult = areaRuleDataService.getAreaRuleDataPage(pageVO);
        return success(AreaRuleDataConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出区域数据 Excel")
    @PreAuthorize("@ss.hasPermission('radar:area-rule-data:export')")
    @OperateLog(type = EXPORT)
    public void exportAreaRuleDataExcel(@Valid AreaRuleDataExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AreaRuleDataDO> list = areaRuleDataService.getAreaRuleDataList(exportReqVO);
        // 导出 Excel
        List<AreaRuleDataExcelVO> datas = AreaRuleDataConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "区域数据.xls", "数据", AreaRuleDataExcelVO.class, datas);
    }

}
