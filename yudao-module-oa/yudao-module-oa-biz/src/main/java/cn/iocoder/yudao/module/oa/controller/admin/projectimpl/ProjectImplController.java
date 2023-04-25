package cn.iocoder.yudao.module.oa.controller.admin.projectimpl;

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

import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;
import cn.iocoder.yudao.module.oa.convert.projectimpl.ProjectImplConvert;
import cn.iocoder.yudao.module.oa.service.projectimpl.ProjectImplService;

@Tag(name = "管理后台 - 工程实施列")
@RestController
@RequestMapping("/oa/project-impl")
@Validated
public class ProjectImplController {

    @Resource
    private ProjectImplService projectImplService;

    @PostMapping("/create")
    @Operation(summary = "创建工程实施列")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:create')")
    public CommonResult<Long> createProjectImpl(@Valid @RequestBody ProjectImplCreateReqVO createReqVO) {
        return success(projectImplService.createProjectImpl(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工程实施列")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:update')")
    public CommonResult<Boolean> updateProjectImpl(@Valid @RequestBody ProjectImplUpdateReqVO updateReqVO) {
        projectImplService.updateProjectImpl(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工程实施列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:project-impl:delete')")
    public CommonResult<Boolean> deleteProjectImpl(@RequestParam("id") Long id) {
        projectImplService.deleteProjectImpl(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工程实施列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:query')")
    public CommonResult<ProjectImplRespVO> getProjectImpl(@RequestParam("id") Long id) {
        ProjectImplDO projectImpl = projectImplService.getProjectImpl(id);
        return success(ProjectImplConvert.INSTANCE.convert(projectImpl));
    }

    @GetMapping("/list")
    @Operation(summary = "获得工程实施列列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:query')")
    public CommonResult<List<ProjectImplRespVO>> getProjectImplList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectImplDO> list = projectImplService.getProjectImplList(ids);
        return success(ProjectImplConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工程实施列分页")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:query')")
    public CommonResult<PageResult<ProjectImplRespVO>> getProjectImplPage(@Valid ProjectImplPageReqVO pageVO) {
        PageResult<ProjectImplDO> pageResult = projectImplService.getProjectImplPage(pageVO);
        return success(ProjectImplConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工程实施列 Excel")
    @PreAuthorize("@ss.hasPermission('oa:project-impl:export')")
    @OperateLog(type = EXPORT)
    public void exportProjectImplExcel(@Valid ProjectImplExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectImplDO> list = projectImplService.getProjectImplList(exportReqVO);
        // 导出 Excel
        List<ProjectImplExcelVO> datas = ProjectImplConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "工程实施列.xls", "数据", ProjectImplExcelVO.class, datas);
    }

}
