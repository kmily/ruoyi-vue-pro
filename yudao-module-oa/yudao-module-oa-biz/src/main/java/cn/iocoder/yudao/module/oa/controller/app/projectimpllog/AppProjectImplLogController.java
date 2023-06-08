package cn.iocoder.yudao.module.oa.controller.app.projectimpllog;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.*;
import cn.iocoder.yudao.module.oa.convert.projectimpllog.ProjectImplLogConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import cn.iocoder.yudao.module.oa.service.projectimpllog.ProjectImplLogService;
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

@Tag(name = "用户App - 工程日志列表")
@RestController
@RequestMapping("/oa/project-impl-log")
@Validated
public class AppProjectImplLogController {

    @Resource
    private ProjectImplLogService projectImplLogService;

    @PostMapping("/create")
    @Operation(summary = "创建工程日志列表")
    public CommonResult<Long> createProjectImplLog(@Valid @RequestBody ProjectImplLogCreateReqVO createReqVO) {
        return success(projectImplLogService.createProjectImplLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工程日志列表")
    public CommonResult<Boolean> updateProjectImplLog(@Valid @RequestBody ProjectImplLogUpdateReqVO updateReqVO) {
        projectImplLogService.updateProjectImplLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工程日志列表")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteProjectImplLog(@RequestParam("id") Long id) {
        projectImplLogService.deleteProjectImplLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工程日志列表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ProjectImplLogRespVO> getProjectImplLog(@RequestParam("id") Long id) {
        ProjectImplLogDO projectImplLog = projectImplLogService.getProjectImplLog(id);
        return success(ProjectImplLogConvert.INSTANCE.convert(projectImplLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得工程日志列表列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ProjectImplLogRespVO>> getProjectImplLogList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectImplLogDO> list = projectImplLogService.getProjectImplLogList(ids);
        return success(ProjectImplLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工程日志列表分页")
    public CommonResult<PageResult<ProjectImplLogRespVO>> getProjectImplLogPage(@Valid ProjectImplLogPageReqVO pageVO) {
        PageResult<ProjectImplLogDO> pageResult = projectImplLogService.getProjectImplLogPage(pageVO);
        return success(ProjectImplLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工程日志列表 Excel")
    @OperateLog(type = EXPORT)
    public void exportProjectImplLogExcel(@Valid ProjectImplLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectImplLogDO> list = projectImplLogService.getProjectImplLogList(exportReqVO);
        // 导出 Excel
        List<ProjectImplLogExcelVO> datas = ProjectImplLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "工程日志列表.xls", "数据", ProjectImplLogExcelVO.class, datas);
    }

}
