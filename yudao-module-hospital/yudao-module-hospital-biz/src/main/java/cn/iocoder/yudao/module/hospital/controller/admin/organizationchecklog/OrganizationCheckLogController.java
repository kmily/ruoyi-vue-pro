package cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog;

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

import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organizationchecklog.OrganizationCheckLogDO;
import cn.iocoder.yudao.module.hospital.convert.organizationchecklog.OrganizationCheckLogConvert;
import cn.iocoder.yudao.module.hospital.service.organizationchecklog.OrganizationCheckLogService;

@Tag(name = "管理后台 - 组织审核记录")
@RestController
@RequestMapping("/hospital/organization-check-log")
@Validated
public class OrganizationCheckLogController {

    @Resource
    private OrganizationCheckLogService organizationCheckLogService;

    @PostMapping("/create")
    @Operation(summary = "创建组织审核记录")
    @PreAuthorize("@ss.hasPermission('hospital:organization-check-log:create')")
    public CommonResult<Long> createOrganizationCheckLog(@Valid @RequestBody OrganizationCheckLogCreateReqVO createReqVO) {
        return success(organizationCheckLogService.createOrganizationCheckLog(createReqVO));
    }


    @GetMapping("/list")
    @Operation(summary = "获得组织审核记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:organization-check-log:query')")
    public CommonResult<List<OrganizationCheckLogRespVO>> getOrganizationCheckLogList(@RequestParam("ids") Collection<Long> ids) {
        List<OrganizationCheckLogDO> list = organizationCheckLogService.getOrganizationCheckLogList(ids);
        return success(OrganizationCheckLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得组织审核记录分页")
    @PreAuthorize("@ss.hasPermission('hospital:organization-check-log:query')")
    public CommonResult<PageResult<OrganizationCheckLogRespVO>> getOrganizationCheckLogPage(@Valid OrganizationCheckLogPageReqVO pageVO) {
        PageResult<OrganizationCheckLogDO> pageResult = organizationCheckLogService.getOrganizationCheckLogPage(pageVO);
        return success(OrganizationCheckLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出组织审核记录 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:organization-check-log:export')")
    @OperateLog(type = EXPORT)
    public void exportOrganizationCheckLogExcel(@Valid OrganizationCheckLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OrganizationCheckLogDO> list = organizationCheckLogService.getOrganizationCheckLogList(exportReqVO);
        // 导出 Excel
        List<OrganizationCheckLogExcelVO> datas = OrganizationCheckLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "组织审核记录.xls", "数据", OrganizationCheckLogExcelVO.class, datas);
    }

}
