package cn.iocoder.yudao.module.biz.controller.admin.calcinterestratedata;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.biz.controller.admin.calcinterestratedata.vo.*;
import cn.iocoder.yudao.module.biz.convert.calcinterestratedata.CalcInterestRateDataConvert;
import cn.iocoder.yudao.module.biz.dal.dataobject.calcinterestratedata.CalcInterestRateDataDO;
import cn.iocoder.yudao.module.biz.service.calcinterestratedata.CalcInterestRateDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 利率数据")
@RestController
@RequestMapping("/biz/calc-interest-rate-data")
@Validated
public class CalcInterestRateDataController {

    @Resource
    private CalcInterestRateDataService calcInterestRateDataService;

    @PostMapping("/create")
    @Operation(summary = "创建利率数据")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:create')")
    public CommonResult<Integer> createCalcInterestRateData(@Valid @RequestBody CalcInterestRateDataCreateReqVO createReqVO) {
        return success(calcInterestRateDataService.createCalcInterestRateData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新利率数据")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:update')")
    public CommonResult<Boolean> updateCalcInterestRateData(@Valid @RequestBody CalcInterestRateDataUpdateReqVO updateReqVO) {
        calcInterestRateDataService.updateCalcInterestRateData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除利率数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:delete')")
    public CommonResult<Boolean> deleteCalcInterestRateData(@RequestParam("id") Integer id) {
        calcInterestRateDataService.deleteCalcInterestRateData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得利率数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<CalcInterestRateDataRespVO> getCalcInterestRateData(@RequestParam("id") Integer id) {
        CalcInterestRateDataDO calcInterestRateData = calcInterestRateDataService.getCalcInterestRateData(id);
        return success(CalcInterestRateDataConvert.INSTANCE.convert(calcInterestRateData));
    }

    @GetMapping("/list")
    @Operation(summary = "获得利率数据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<List<CalcInterestRateDataRespVO>> getCalcInterestRateDataList(@RequestParam("ids") Collection<Integer> ids) {
        List<CalcInterestRateDataDO> list = calcInterestRateDataService.getCalcInterestRateDataList(ids);
        return success(CalcInterestRateDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得利率数据分页")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<PageResult<CalcInterestRateDataRespVO>> getCalcInterestRateDataPage(@Valid CalcInterestRateDataPageReqVO pageVO) {
        PageResult<CalcInterestRateDataDO> pageResult = calcInterestRateDataService.getCalcInterestRateDataPage(pageVO);
        return success(CalcInterestRateDataConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出利率数据 Excel")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:export')")
    public void exportCalcInterestRateDataExcel(@Valid CalcInterestRateDataExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CalcInterestRateDataDO> list = calcInterestRateDataService.getCalcInterestRateDataList(exportReqVO);
        // 导出 Excel
        List<CalcInterestRateDataExcelVO> datas = CalcInterestRateDataConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "利率数据.xls", "数据", CalcInterestRateDataExcelVO.class, datas);
    }

}
