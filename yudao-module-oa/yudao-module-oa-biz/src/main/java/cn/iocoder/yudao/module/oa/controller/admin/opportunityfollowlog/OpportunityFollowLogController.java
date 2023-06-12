package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog;

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

import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import cn.iocoder.yudao.module.oa.convert.opportunityfollowlog.OpportunityFollowLogConvert;
import cn.iocoder.yudao.module.oa.service.opportunityfollowlog.OpportunityFollowLogService;

@Tag(name = "管理后台 - 商机-跟进日志")
@RestController
@RequestMapping("/oa/opportunity-follow-log")
@Validated
public class OpportunityFollowLogController {

    @Resource
    private OpportunityFollowLogService opportunityFollowLogService;

    @PostMapping("/create")
    @Operation(summary = "创建商机-跟进日志")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:create')")
    public CommonResult<Long> createOpportunityFollowLog(@Valid @RequestBody OpportunityFollowLogCreateReqVO createReqVO) {
        return success(opportunityFollowLogService.createOpportunityFollowLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商机-跟进日志")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:update')")
    public CommonResult<Boolean> updateOpportunityFollowLog(@Valid @RequestBody OpportunityFollowLogUpdateReqVO updateReqVO) {
        opportunityFollowLogService.updateOpportunityFollowLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商机-跟进日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:delete')")
    public CommonResult<Boolean> deleteOpportunityFollowLog(@RequestParam("id") Long id) {
        opportunityFollowLogService.deleteOpportunityFollowLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商机-跟进日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:query')")
    public CommonResult<OpportunityFollowLogRespVO> getOpportunityFollowLog(@RequestParam("id") Long id) {
        OpportunityFollowLogDO opportunityFollowLog = opportunityFollowLogService.getOpportunityFollowLog(id);
        return success(OpportunityFollowLogConvert.INSTANCE.convert(opportunityFollowLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得商机-跟进日志列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:query')")
    public CommonResult<List<OpportunityFollowLogRespVO>> getOpportunityFollowLogList(@RequestParam("ids") Collection<Long> ids) {
        List<OpportunityFollowLogDO> list = opportunityFollowLogService.getOpportunityFollowLogList(ids);
        return success(OpportunityFollowLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商机-跟进日志分页")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:query')")
    public CommonResult<PageResult<OpportunityFollowLogRespVO>> getOpportunityFollowLogPage(@Valid OpportunityFollowLogPageReqVO pageVO) {
        PageResult<OpportunityFollowLogDO> pageResult = opportunityFollowLogService.getOpportunityFollowLogPage(pageVO);
        return success(OpportunityFollowLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商机-跟进日志 Excel")
    @PreAuthorize("@ss.hasPermission('oa:opportunity-follow-log:export')")
    @OperateLog(type = EXPORT)
    public void exportOpportunityFollowLogExcel(@Valid OpportunityFollowLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OpportunityFollowLogDO> list = opportunityFollowLogService.getOpportunityFollowLogList(exportReqVO);
        // 导出 Excel
        List<OpportunityFollowLogExcelVO> datas = OpportunityFollowLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "商机-跟进日志.xls", "数据", OpportunityFollowLogExcelVO.class, datas);
    }

}
