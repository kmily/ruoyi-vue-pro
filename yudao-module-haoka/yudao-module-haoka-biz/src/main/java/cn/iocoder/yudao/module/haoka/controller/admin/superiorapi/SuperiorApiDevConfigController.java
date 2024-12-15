package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
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

import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.service.superiorapi.SuperiorApiDevConfigService;

@Tag(name = "管理后台 - 上游API接口开发配置")
@RestController
@RequestMapping("/haoka/superior-api-dev-config")
@Validated
public class SuperiorApiDevConfigController {

    @Resource
    private SuperiorApiDevConfigService superiorApiDevConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建上游API接口开发配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:create')")
    public CommonResult<Long> createSuperiorApiDevConfig(@Valid @RequestBody SuperiorApiDevConfigSaveReqVO createReqVO) {
        return success(superiorApiDevConfigService.createSuperiorApiDevConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新上游API接口开发配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:update')")
    public CommonResult<Boolean> updateSuperiorApiDevConfig(@Valid @RequestBody SuperiorApiDevConfigSaveReqVO updateReqVO) {
        superiorApiDevConfigService.updateSuperiorApiDevConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除上游API接口开发配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:delete')")
    public CommonResult<Boolean> deleteSuperiorApiDevConfig(@RequestParam("id") Long id) {
        superiorApiDevConfigService.deleteSuperiorApiDevConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得上游API接口开发配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:query')")
    public CommonResult<SuperiorApiDevConfigRespVO> getSuperiorApiDevConfig(@RequestParam("id") Long id) {
        SuperiorApiDevConfigDO superiorApiDevConfig = superiorApiDevConfigService.getSuperiorApiDevConfig(id);
        return success(BeanUtils.toBean(superiorApiDevConfig, SuperiorApiDevConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得上游API接口开发配置分页")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:query')")
    public CommonResult<PageResult<SuperiorApiDevConfigRespVO>> getSuperiorApiDevConfigPage(@Valid SuperiorApiDevConfigPageReqVO pageReqVO) {
        PageResult<SuperiorApiDevConfigDO> pageResult = superiorApiDevConfigService.getSuperiorApiDevConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SuperiorApiDevConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出上游API接口开发配置 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-dev-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSuperiorApiDevConfigExcel(@Valid SuperiorApiDevConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SuperiorApiDevConfigDO> list = superiorApiDevConfigService.getSuperiorApiDevConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "上游API接口开发配置.xls", "数据", SuperiorApiDevConfigRespVO.class,
                        BeanUtils.toBean(list, SuperiorApiDevConfigRespVO.class));
    }

}