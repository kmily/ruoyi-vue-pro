package cn.iocoder.yudao.module.oa.controller.app.opportunityfollowlog;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.convert.opportunityfollowlog.OpportunityFollowLogConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import cn.iocoder.yudao.module.oa.service.opportunityfollowlog.OpportunityFollowLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户App - 商机-跟进日志")
@RestController
@RequestMapping("/oa/opportunity-follow-log")
@Validated
public class AppOpportunityFollowLogController {




        @Resource
        private OpportunityFollowLogService opportunityFollowLogService;

        @PostMapping("/create")
        @Operation(summary = "创建商机-跟进日志")
        @PreAuthenticated
        public CommonResult<Long> createOpportunityFollowLog(@Valid @RequestBody OpportunityFollowLogCreateReqVO createReqVO) {
            return success(opportunityFollowLogService.createOpportunityFollowLog(createReqVO));
        }

        @PutMapping("/update")
        @Operation(summary = "更新商机-跟进日志")
        @PreAuthenticated
        public CommonResult<Boolean> updateOpportunityFollowLog(@Valid @RequestBody OpportunityFollowLogUpdateReqVO updateReqVO) {
            opportunityFollowLogService.updateOpportunityFollowLog(updateReqVO);
            return success(true);
        }

        @DeleteMapping("/delete")
        @Operation(summary = "删除商机-跟进日志")
        @Parameter(name = "id", description = "编号", required = true)
        @PreAuthenticated
        public CommonResult<Boolean> deleteOpportunityFollowLog(@RequestParam("id") Long id) {
            opportunityFollowLogService.deleteOpportunityFollowLog(id);
            return success(true);
        }

        @GetMapping("/get")
        @Operation(summary = "获得商机-跟进日志")
        @PreAuthenticated
        @Parameter(name = "id", description = "编号", required = true, example = "1024")
        public CommonResult<OpportunityFollowLogRespVO> getOpportunityFollowLog(@RequestParam("id") Long id) {
            OpportunityFollowLogDO opportunityFollowLog = opportunityFollowLogService.getOpportunityFollowLog(id);
            return success(OpportunityFollowLogConvert.INSTANCE.convert(opportunityFollowLog));
        }

        @GetMapping("/list")
        @Operation(summary = "获得商机-跟进日志列表")
        @PreAuthenticated
        @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
        public CommonResult<List<OpportunityFollowLogRespVO>> getOpportunityFollowLogList(@RequestParam("ids") Collection<Long> ids) {
            List<OpportunityFollowLogDO> list = opportunityFollowLogService.getOpportunityFollowLogList(ids);
            return success(OpportunityFollowLogConvert.INSTANCE.convertList(list));
        }

        @GetMapping("/page")
        @Operation(summary = "获得商机-跟进日志分页")
        @PreAuthenticated
        public CommonResult<PageResult<OpportunityFollowLogRespVO>> getOpportunityFollowLogPage(@Valid OpportunityFollowLogPageReqVO pageVO) {
            PageResult<OpportunityFollowLogDO> pageResult = opportunityFollowLogService.getOpportunityFollowLogPage(pageVO);
            return success(OpportunityFollowLogConvert.INSTANCE.convertPage(pageResult));
        }

        @GetMapping("/export-excel")
        @Operation(summary = "导出商机-跟进日志 Excel")
        @OperateLog(type = EXPORT)
        @PreAuthenticated
        public void exportOpportunityFollowLogExcel(@Valid OpportunityFollowLogExportReqVO exportReqVO,
                                                    HttpServletResponse response) throws IOException {
            List<OpportunityFollowLogDO> list = opportunityFollowLogService.getOpportunityFollowLogList(exportReqVO);
            // 导出 Excel
            List<OpportunityFollowLogExcelVO> datas = OpportunityFollowLogConvert.INSTANCE.convertList02(list);
            ExcelUtils.write(response, "商机-跟进日志.xls", "数据", OpportunityFollowLogExcelVO.class, datas);
        }

}
