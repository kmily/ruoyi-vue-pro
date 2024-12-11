package cn.iocoder.yudao.module.system.controller.admin.codingrules;

import cn.iocoder.yudao.module.system.api.codingrules.CodingRulesApi;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrulesdetails.CodingRulesDetailsDO;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.system.controller.admin.codingrules.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrules.CodingRulesDO;
import cn.iocoder.yudao.module.system.service.codingrules.CodingRulesService;

@Tag(name = "管理后台 - 编码规则")
@RestController
@RequestMapping("/system/coding-rules")
@Validated
public class CodingRulesController {

    @Resource
    private CodingRulesService codingRulesService;

    @Resource
    private CodingRulesApi codingRulesApi;

    @PostMapping("/create")
    @Operation(summary = "创建编码规则表头")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:create')")
    public CommonResult<String> createCodingRules(@Valid @RequestBody CodingRulesSaveReqVO createReqVO) {
        return success(codingRulesService.createCodingRules(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码规则表头")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:update')")
    public CommonResult<Boolean> updateCodingRules(@Valid @RequestBody CodingRulesSaveReqVO updateReqVO) {
        codingRulesService.updateCodingRules(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateDetails")
    @Operation(summary = "更新编码规则明细")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:updatedetails')")
    public CommonResult<Boolean> updateDetails(@Valid @RequestBody CodingRulesDetailsSaveReqVO updateReqVO) {
        codingRulesService.updateDetails(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码规则表头")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:coding-rules:delete')")
    public CommonResult<Boolean> deleteCodingRules(@RequestParam("id") String id) {
        codingRulesService.deleteCodingRules(id);
        return success(true);
    }

    @DeleteMapping("/deleteDetails")
    @Operation(summary = "删除编码规则明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:coding-rules:deletedetails')")
    public CommonResult<Boolean> deleteDetails(@RequestParam("id") String id) {
        codingRulesService.deleteDetails(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码规则表头")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:query')")
    public CommonResult<CodingRulesRespVO> getCodingRules(@RequestParam("id") String id) {
        CodingRulesDO codingRules = codingRulesService.getCodingRules(id);
        return success(BeanUtils.toBean(codingRules, CodingRulesRespVO.class));
    }

    @GetMapping("/getDetails")
    @Operation(summary = "获取编码规则明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:details')")
    public CommonResult<List<CodingRulesDetailsRespVO>> getDetails(@RequestParam("id") String id) {
        List<CodingRulesDetailsDO> codingRulesDetails = codingRulesService.getDetails(id);
        return success(BeanUtils.toBean(codingRulesDetails, CodingRulesDetailsRespVO.class));
    }


    @PostMapping("/insertDetails")
    @Operation(summary = "新增编码规则明细")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:insertdetails')")
    public CommonResult<String> insertDetails(@Valid @RequestBody CodingRulesDetailsSaveReqVO codingRulesDetailsSaveReqVO) {
        return success(codingRulesService.insertDetails(codingRulesDetailsSaveReqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码规则表头分页")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:query')")
    public CommonResult<PageResult<CodingRulesRespVO>> getCodingRulesPage(@Valid CodingRulesPageReqVO pageReqVO) {
        PageResult<CodingRulesDO> pageResult = codingRulesService.getCodingRulesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CodingRulesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码规则表头 Excel")
    @PreAuthorize("@ss.hasPermission('system:coding-rules:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCodingRulesExcel(@Valid CodingRulesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CodingRulesDO> list = codingRulesService.getCodingRulesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码规则表头.xls", "数据", CodingRulesRespVO.class,
                        BeanUtils.toBean(list, CodingRulesRespVO.class));
    }

    @GetMapping("/genCodingRules")
    @Operation(summary = "生成编码规则")
    @Parameters({
            @Parameter(name = "code", description = "编码规则编号", required = true, example = "CRM_CONTRACT_NO_RULES"),
            @Parameter(name = "preview", description = "是否浏览模式-true/false", required = true, example = "true")
    })
    @PreAuthorize("@ss.hasPermission('system:coding-rules:gencodingrules')")
    public CommonResult<String> genCodingRules(@RequestParam("code") String code,@RequestParam("preview") Boolean preview) {
        String genCode = codingRulesApi.genCodingRules(code,preview);
        return success(genCode);
    }


}
