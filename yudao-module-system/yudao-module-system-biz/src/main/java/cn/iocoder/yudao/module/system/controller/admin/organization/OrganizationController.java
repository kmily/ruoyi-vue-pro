package cn.iocoder.yudao.module.system.controller.admin.organization;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.system.convert.organization.OrganizationConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.module.system.service.organization.OrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 组织机构")
@RestController
@RequestMapping("/system/organization")
@Validated
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;


    @PostMapping("/create")
    @Operation(summary = "创建组织机构")
    @PreAuthorize("@ss.hasPermission('hospital:organization:create')")
    public CommonResult<Long> createOrganization(@Valid @RequestBody OrganizationCreateReqVO createReqVO) {
        return success(organizationService.createOrganization(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新组织机构")
    @PreAuthorize("@ss.hasPermission('hospital:organization:update')")
    public CommonResult<Boolean> updateOrganization(@Valid @RequestBody OrganizationUpdateReqVO updateReqVO) {
        organizationService.updateOrganization(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除组织机构")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:organization:delete')")
    public CommonResult<Boolean> deleteOrganization(@RequestParam("id") Long id) {
        organizationService.deleteOrganization(id);
        return success(true);
    }

    @PutMapping("/close")
    @Operation(summary = "关闭组织机构")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:organization:close')")
    public CommonResult<Boolean> closeOrganization(@RequestParam("id") Long id) {
        organizationService.closeOrganization(id);
        return success(true);
    }

    @PutMapping("/open")
    @Operation(summary = "开启组织机构")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:organization:open')")
    public CommonResult<Boolean> openOrganization(@RequestParam("id") Long id) {
        organizationService.openOrganization(id);
        return success(true);
    }
    @PutMapping("/audit")
    @Operation(summary = "审核组织机构")
    @PreAuthorize("@ss.hasPermission('hospital:organization:audit')")
    public CommonResult<Boolean> auditOrganization(@RequestBody OrganizationAuditVO auditVO) {
        organizationService.auditOrganization(auditVO);
        return success(true);
    }

    @GetMapping("/{memberId}/member")
    @PreAuthorize("@ss.hasPermission('hospital:organization:query')")
    @Operation(summary = "根据会员Id查询机构信息")
    @Parameter(name = "memberId", description = "会员ID")
    public CommonResult<OrganizationRespVO> getByMemberId(@PathVariable("memberId") Long memberId){
        List<OrganizationDO> list = organizationService.list(new LambdaQueryWrapper<OrganizationDO>().eq(OrganizationDO::getUserId, memberId));
        if(CollUtil.isEmpty(list)){
            return success(null);
        }
        return success(OrganizationConvert.INSTANCE.convert(list.get(0)));
    }

    @GetMapping("/get")
    @Operation(summary = "获得组织机构")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:organization:query')")
    public CommonResult<OrganizationRespVO> getOrganization(@RequestParam("id") Long id) {
        OrganizationDO organization = organizationService.getOrganization(id);
        OrganizationRespVO respVO = OrganizationConvert.INSTANCE.convert(organization);
        return success(respVO);
    }

    @GetMapping("/list")
    @Operation(summary = "获得组织机构列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:organization:query')")
    public CommonResult<List<OrganizationRespVO>> getOrganizationList(@RequestParam("ids") Collection<Long> ids) {
        List<OrganizationDO> list = organizationService.getOrganizationList(ids);
        return success(OrganizationConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得组织机构分页")
    @PreAuthorize("@ss.hasPermission('hospital:organization:query')")
    public CommonResult<PageResult<OrganizationRespVO>> getOrganizationPage(@Valid OrganizationPageReqVO pageVO) {
        PageResult<OrganizationDO> pageResult = organizationService.getOrganizationPage(pageVO);
        return success(OrganizationConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出组织机构 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:organization:export')")
    @OperateLog(type = EXPORT)
    public void exportOrganizationExcel(@Valid OrganizationExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OrganizationDO> list = organizationService.getOrganizationList(exportReqVO);
        // 导出 Excel
        List<OrganizationExcelVO> datas = OrganizationConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "组织机构.xls", "数据", OrganizationExcelVO.class, datas);
    }

}
