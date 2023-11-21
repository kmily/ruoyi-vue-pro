package cn.iocoder.yudao.module.system.controller.admin.organizationpackage;

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

import cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;
import cn.iocoder.yudao.module.system.convert.organizationpackage.OrganizationPackageConvert;
import cn.iocoder.yudao.module.system.service.organizationpackage.OrganizationPackageService;

@Tag(name = "管理后台 - 机构套餐")
@RestController
@RequestMapping("/system/organization-package")
@Validated
public class OrganizationPackageController {

    @Resource
    private OrganizationPackageService organizationPackageService;

    @PostMapping("/create")
    @Operation(summary = "创建机构套餐")
    @PreAuthorize("@ss.hasPermission('system:organization-package:create')")
    public CommonResult<Long> createOrganizationPackage(@Valid @RequestBody OrganizationPackageCreateReqVO createReqVO) {
        return success(organizationPackageService.createOrganizationPackage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新机构套餐")
    @PreAuthorize("@ss.hasPermission('system:organization-package:update')")
    public CommonResult<Boolean> updateOrganizationPackage(@Valid @RequestBody OrganizationPackageUpdateReqVO updateReqVO) {
        organizationPackageService.updateOrganizationPackage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除机构套餐")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:organization-package:delete')")
    public CommonResult<Boolean> deleteOrganizationPackage(@RequestParam("id") Long id) {
        organizationPackageService.deleteOrganizationPackage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得机构套餐")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:organization-package:query')")
    public CommonResult<OrganizationPackageRespVO> getOrganizationPackage(@RequestParam("id") Long id) {
        OrganizationPackageDO organizationPackage = organizationPackageService.getOrganizationPackage(id);
        return success(OrganizationPackageConvert.INSTANCE.convert(organizationPackage));
    }

    @GetMapping("/list")
    @Operation(summary = "获得机构套餐列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:organization-package:query')")
    public CommonResult<List<OrganizationPackageRespVO>> getOrganizationPackageList(@RequestParam("ids") Collection<Long> ids) {
        List<OrganizationPackageDO> list = organizationPackageService.getOrganizationPackageList(ids);
        return success(OrganizationPackageConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得机构套餐分页")
    @PreAuthorize("@ss.hasPermission('system:organization-package:query')")
    public CommonResult<PageResult<OrganizationPackageRespVO>> getOrganizationPackagePage(@Valid OrganizationPackagePageReqVO pageVO) {
        PageResult<OrganizationPackageDO> pageResult = organizationPackageService.getOrganizationPackagePage(pageVO);
        return success(OrganizationPackageConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出机构套餐 Excel")
    @PreAuthorize("@ss.hasPermission('system:organization-package:export')")
    @OperateLog(type = EXPORT)
    public void exportOrganizationPackageExcel(@Valid OrganizationPackageExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OrganizationPackageDO> list = organizationPackageService.getOrganizationPackageList(exportReqVO);
        // 导出 Excel
        List<OrganizationPackageExcelVO> datas = OrganizationPackageConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "机构套餐.xls", "数据", OrganizationPackageExcelVO.class, datas);
    }

}
