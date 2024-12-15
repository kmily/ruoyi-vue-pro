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
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.module.haoka.service.superiorapi.SuperiorApiService;

@Tag(name = "管理后台 - 上游API接口")
@RestController
@RequestMapping("/haoka/superior-api")
@Validated
public class SuperiorApiController {

    @Resource
    private SuperiorApiService superiorApiService;

    @PostMapping("/create")
    @Operation(summary = "创建上游API接口")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:create')")
    public CommonResult<Long> createSuperiorApi(@Valid @RequestBody SuperiorApiSaveReqVO createReqVO) {
        return success(superiorApiService.createSuperiorApi(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新上游API接口")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:update')")
    public CommonResult<Boolean> updateSuperiorApi(@Valid @RequestBody SuperiorApiSaveReqVO updateReqVO) {
        superiorApiService.updateSuperiorApi(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除上游API接口")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:delete')")
    public CommonResult<Boolean> deleteSuperiorApi(@RequestParam("id") Long id) {
        superiorApiService.deleteSuperiorApi(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得上游API接口")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
    public CommonResult<SuperiorApiRespVO> getSuperiorApi(@RequestParam("id") Long id) {
        SuperiorApiDO superiorApi = superiorApiService.getSuperiorApi(id);
        return success(BeanUtils.toBean(superiorApi, SuperiorApiRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得上游API接口分页")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
    public CommonResult<PageResult<SuperiorApiRespVO>> getSuperiorApiPage(@Valid SuperiorApiPageReqVO pageReqVO) {
        PageResult<SuperiorApiDO> pageResult = superiorApiService.getSuperiorApiPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SuperiorApiRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出上游API接口 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSuperiorApiExcel(@Valid SuperiorApiPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SuperiorApiDO> list = superiorApiService.getSuperiorApiPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "上游API接口.xls", "数据", SuperiorApiRespVO.class,
                        BeanUtils.toBean(list, SuperiorApiRespVO.class));
    }

    // ==================== 子表（上游API接口开发配置） ====================

    @GetMapping("/superior-api-dev-config/page")
    @Operation(summary = "获得上游API接口开发配置分页")
    @Parameter(name = "haokaSuperiorApiId", description = "ID")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
    public CommonResult<PageResult<SuperiorApiDevConfigDO>> getSuperiorApiDevConfigPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaSuperiorApiId") Long haokaSuperiorApiId) {
        return success(superiorApiService.getSuperiorApiDevConfigPage(pageReqVO, haokaSuperiorApiId));
    }

    @PostMapping("/superior-api-dev-config/create")
    @Operation(summary = "创建上游API接口开发配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:create')")
    public CommonResult<Long> createSuperiorApiDevConfig(@Valid @RequestBody SuperiorApiDevConfigDO superiorApiDevConfig) {
        return success(superiorApiService.createSuperiorApiDevConfig(superiorApiDevConfig));
    }

    @PutMapping("/superior-api-dev-config/update")
    @Operation(summary = "更新上游API接口开发配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:update')")
    public CommonResult<Boolean> updateSuperiorApiDevConfig(@Valid @RequestBody SuperiorApiDevConfigDO superiorApiDevConfig) {
        superiorApiService.updateSuperiorApiDevConfig(superiorApiDevConfig);
        return success(true);
    }

    @DeleteMapping("/superior-api-dev-config/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除上游API接口开发配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:delete')")
    public CommonResult<Boolean> deleteSuperiorApiDevConfig(@RequestParam("id") Long id) {
        superiorApiService.deleteSuperiorApiDevConfig(id);
        return success(true);
    }

	@GetMapping("/superior-api-dev-config/get")
	@Operation(summary = "获得上游API接口开发配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
	public CommonResult<SuperiorApiDevConfigDO> getSuperiorApiDevConfig(@RequestParam("id") Long id) {
	    return success(superiorApiService.getSuperiorApiDevConfig(id));
	}

    // ==================== 子表（上游API接口SKU要求配置） ====================

    @GetMapping("/superior-api-sku-config/page")
    @Operation(summary = "获得上游API接口SKU要求配置分页")
    @Parameter(name = "haokaSuperiorApiId", description = "ID")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
    public CommonResult<PageResult<SuperiorApiSkuConfigDO>> getSuperiorApiSkuConfigPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaSuperiorApiId") Long haokaSuperiorApiId) {
        return success(superiorApiService.getSuperiorApiSkuConfigPage(pageReqVO, haokaSuperiorApiId));
    }

    @PostMapping("/superior-api-sku-config/create")
    @Operation(summary = "创建上游API接口SKU要求配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:create')")
    public CommonResult<Long> createSuperiorApiSkuConfig(@Valid @RequestBody SuperiorApiSkuConfigDO superiorApiSkuConfig) {
        return success(superiorApiService.createSuperiorApiSkuConfig(superiorApiSkuConfig));
    }

    @PutMapping("/superior-api-sku-config/update")
    @Operation(summary = "更新上游API接口SKU要求配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:update')")
    public CommonResult<Boolean> updateSuperiorApiSkuConfig(@Valid @RequestBody SuperiorApiSkuConfigDO superiorApiSkuConfig) {
        superiorApiService.updateSuperiorApiSkuConfig(superiorApiSkuConfig);
        return success(true);
    }

    @DeleteMapping("/superior-api-sku-config/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除上游API接口SKU要求配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:delete')")
    public CommonResult<Boolean> deleteSuperiorApiSkuConfig(@RequestParam("id") Long id) {
        superiorApiService.deleteSuperiorApiSkuConfig(id);
        return success(true);
    }

	@GetMapping("/superior-api-sku-config/get")
	@Operation(summary = "获得上游API接口SKU要求配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
	public CommonResult<SuperiorApiSkuConfigDO> getSuperiorApiSkuConfig(@RequestParam("id") Long id) {
	    return success(superiorApiService.getSuperiorApiSkuConfig(id));
	}

    // ==================== 子表（产品对接上游配置） ====================

    @GetMapping("/superior-product-config/page")
    @Operation(summary = "获得产品对接上游配置分页")
    @Parameter(name = "haokaSuperiorApiId", description = "上游接口ID")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
    public CommonResult<PageResult<SuperiorProductConfigDO>> getSuperiorProductConfigPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaSuperiorApiId") Long haokaSuperiorApiId) {
        return success(superiorApiService.getSuperiorProductConfigPage(pageReqVO, haokaSuperiorApiId));
    }

    @PostMapping("/superior-product-config/create")
    @Operation(summary = "创建产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:create')")
    public CommonResult<Long> createSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        return success(superiorApiService.createSuperiorProductConfig(superiorProductConfig));
    }

    @PutMapping("/superior-product-config/update")
    @Operation(summary = "更新产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:update')")
    public CommonResult<Boolean> updateSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        superiorApiService.updateSuperiorProductConfig(superiorProductConfig);
        return success(true);
    }

    @DeleteMapping("/superior-product-config/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:delete')")
    public CommonResult<Boolean> deleteSuperiorProductConfig(@RequestParam("id") Long id) {
        superiorApiService.deleteSuperiorProductConfig(id);
        return success(true);
    }

	@GetMapping("/superior-product-config/get")
	@Operation(summary = "获得产品对接上游配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-api:query')")
	public CommonResult<SuperiorProductConfigDO> getSuperiorProductConfig(@RequestParam("id") Long id) {
	    return success(superiorApiService.getSuperiorProductConfig(id));
	}

}