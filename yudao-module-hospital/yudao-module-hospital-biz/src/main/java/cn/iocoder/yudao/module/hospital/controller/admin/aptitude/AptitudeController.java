package cn.iocoder.yudao.module.hospital.controller.admin.aptitude;

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

import cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;
import cn.iocoder.yudao.module.hospital.convert.aptitude.AptitudeConvert;
import cn.iocoder.yudao.module.hospital.service.aptitude.AptitudeService;

@Tag(name = "管理后台 - 资质信息")
@RestController
@RequestMapping("/hospital/aptitude")
@Validated
public class AptitudeController {

    @Resource
    private AptitudeService aptitudeService;

    @PostMapping("/create")
    @Operation(summary = "创建资质信息")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:create')")
    public CommonResult<Long> createAptitude(@Valid @RequestBody AptitudeCreateReqVO createReqVO) {
        return success(aptitudeService.createAptitude(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新资质信息")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:update')")
    public CommonResult<Boolean> updateAptitude(@Valid @RequestBody AptitudeUpdateReqVO updateReqVO) {
        aptitudeService.updateAptitude(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除资质信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:delete')")
    public CommonResult<Boolean> deleteAptitude(@RequestParam("id") Long id) {
        aptitudeService.deleteAptitude(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得资质信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:query')")
    public CommonResult<AptitudeRespVO> getAptitude(@RequestParam("id") Long id) {
        AptitudeDO aptitude = aptitudeService.getAptitude(id);
        return success(AptitudeConvert.INSTANCE.convert(aptitude));
    }

    @GetMapping("/list")
    @Operation(summary = "获得资质信息列表")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:query')")
    public CommonResult<List<AptitudeRespVO>> getAptitudeList() {
        List<AptitudeDO> list = aptitudeService.getAptitudeList();
        return success(AptitudeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得资质信息分页")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:query')")
    public CommonResult<PageResult<AptitudeRespVO>> getAptitudePage(@Valid AptitudePageReqVO pageVO) {
        PageResult<AptitudeDO> pageResult = aptitudeService.getAptitudePage(pageVO);
        return success(AptitudeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出资质信息 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:aptitude:export')")
    @OperateLog(type = EXPORT)
    public void exportAptitudeExcel(@Valid AptitudeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AptitudeDO> list = aptitudeService.getAptitudeList(exportReqVO);
        // 导出 Excel
        List<AptitudeExcelVO> datas = AptitudeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "资质信息.xls", "数据", AptitudeExcelVO.class, datas);
    }

}
