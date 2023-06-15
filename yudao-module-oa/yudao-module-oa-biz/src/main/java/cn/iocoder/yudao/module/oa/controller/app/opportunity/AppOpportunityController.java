package cn.iocoder.yudao.module.oa.controller.app.opportunity;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;
import cn.iocoder.yudao.module.oa.convert.opportunity.OpportunityConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;
import cn.iocoder.yudao.module.oa.service.opportunity.OpportunityService;
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

@Tag(name = "用户App - 商机")
@RestController
@RequestMapping("/oa/opportunity")
@Validated
public class AppOpportunityController {

    @Resource
    private OpportunityService opportunityService;

    @PostMapping("/create")
    @Operation(summary = "创建商机")
    @PreAuthenticated
    public CommonResult<Long> createOpportunity(@Valid @RequestBody OpportunityCreateReqVO createReqVO) {
        return success(opportunityService.createOpportunity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商机")
    @PreAuthenticated
    public CommonResult<Boolean> updateOpportunity(@Valid @RequestBody OpportunityUpdateReqVO updateReqVO) {
        opportunityService.updateOpportunity(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthenticated
    @Operation(summary = "删除商机")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteOpportunity(@RequestParam("id") Long id) {
        opportunityService.deleteOpportunity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商机")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OpportunityRespVO> getOpportunity(@RequestParam("id") Long id) {
        OpportunityDO opportunity = opportunityService.getOpportunity(id);
        return success(OpportunityConvert.INSTANCE.convert(opportunity));
    }

    @GetMapping("/list")
    @Operation(summary = "获得商机列表")
    @PreAuthenticated
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<OpportunityRespVO>> getOpportunityList(@RequestParam("ids") Collection<Long> ids) {
        List<OpportunityDO> list = opportunityService.getOpportunityList(ids);
        return success(OpportunityConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商机分页")
    @PreAuthenticated
    public CommonResult<PageResult<OpportunityRespVO>> getOpportunityPage(@Valid OpportunityPageReqVO pageVO) {
        PageResult<OpportunityDO> pageResult = opportunityService.getOpportunityPage(pageVO);
        return success(OpportunityConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商机 Excel")
    @OperateLog(type = EXPORT)
    @PreAuthenticated
    public void exportOpportunityExcel(@Valid OpportunityExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OpportunityDO> list = opportunityService.getOpportunityList(exportReqVO);
        // 导出 Excel
        List<OpportunityExcelVO> datas = OpportunityConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "商机.xls", "数据", OpportunityExcelVO.class, datas);
    }

}
