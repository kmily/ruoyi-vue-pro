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
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.service.superiorapi.SuperiorApiSkuConfigService;

@Tag(name = "管理后台 - 上游API接口SKU要求配置")
@RestController
@RequestMapping("/haoka/superior-api-sku-config")
@Validated
public class SuperiorApiSkuConfigController {

    @Resource
    private SuperiorApiSkuConfigService superiorApiSkuConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建上游API接口SKU要求配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:create')")
    public CommonResult<Long> createSuperiorApiSkuConfig(@Valid @RequestBody SuperiorApiSkuConfigSaveReqVO createReqVO) {
        return success(superiorApiSkuConfigService.createSuperiorApiSkuConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新上游API接口SKU要求配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:update')")
    public CommonResult<Boolean> updateSuperiorApiSkuConfig(@Valid @RequestBody SuperiorApiSkuConfigSaveReqVO updateReqVO) {
        superiorApiSkuConfigService.updateSuperiorApiSkuConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除上游API接口SKU要求配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:delete')")
    public CommonResult<Boolean> deleteSuperiorApiSkuConfig(@RequestParam("id") Long id) {
        superiorApiSkuConfigService.deleteSuperiorApiSkuConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得上游API接口SKU要求配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:query')")
    public CommonResult<SuperiorApiSkuConfigRespVO> getSuperiorApiSkuConfig(@RequestParam("id") Long id) {
        SuperiorApiSkuConfigDO superiorApiSkuConfig = superiorApiSkuConfigService.getSuperiorApiSkuConfig(id);
        return success(BeanUtils.toBean(superiorApiSkuConfig, SuperiorApiSkuConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得上游API接口SKU要求配置分页")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:query')")
    public CommonResult<PageResult<SuperiorApiSkuConfigRespVO>> getSuperiorApiSkuConfigPage(@Valid SuperiorApiSkuConfigPageReqVO pageReqVO) {
        PageResult<SuperiorApiSkuConfigDO> pageResult = superiorApiSkuConfigService.getSuperiorApiSkuConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SuperiorApiSkuConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出上游API接口SKU要求配置 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api-sku-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSuperiorApiSkuConfigExcel(@Valid SuperiorApiSkuConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SuperiorApiSkuConfigDO> list = superiorApiSkuConfigService.getSuperiorApiSkuConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "上游API接口SKU要求配置.xls", "数据", SuperiorApiSkuConfigRespVO.class,
                        BeanUtils.toBean(list, SuperiorApiSkuConfigRespVO.class));
    }

}