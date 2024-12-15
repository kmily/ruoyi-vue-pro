package cn.iocoder.yudao.module.haoka.controller.admin.product;

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

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.module.haoka.service.product.ProductService;

@Tag(name = "管理后台 - 产品/渠道")
@RestController
@RequestMapping("/haoka/product")
@Validated
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/create")
    @Operation(summary = "创建产品/渠道")
    @PreAuthorize("@ss.hasPermission('haoka:product:create')")
    public CommonResult<Long> createProduct(@Valid @RequestBody ProductSaveReqVO createReqVO) {
        return success(productService.createProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品/渠道")
    @PreAuthorize("@ss.hasPermission('haoka:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody ProductSaveReqVO updateReqVO) {
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品/渠道")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品/渠道")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:product:query')")
    public CommonResult<ProductRespVO> getProduct(@RequestParam("id") Long id) {
        ProductDO product = productService.getProduct(id);
        return success(BeanUtils.toBean(product, ProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品/渠道分页")
    @PreAuthorize("@ss.hasPermission('haoka:product:query')")
    public CommonResult<PageResult<ProductRespVO>> getProductPage(@Valid ProductPageReqVO pageReqVO) {
        PageResult<ProductDO> pageResult = productService.getProductPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品/渠道 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductExcel(@Valid ProductPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductDO> list = productService.getProductPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品/渠道.xls", "数据", ProductRespVO.class,
                        BeanUtils.toBean(list, ProductRespVO.class));
    }

    // ==================== 子表（产品对接上游配置） ====================

    @GetMapping("/superior-product-config/page")
    @Operation(summary = "获得产品对接上游配置分页")
    @Parameter(name = "haokaProductId", description = "产品ID")
    @PreAuthorize("@ss.hasPermission('haoka:product:query')")
    public CommonResult<PageResult<SuperiorProductConfigDO>> getSuperiorProductConfigPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaProductId") Long haokaProductId) {
        return success(productService.getSuperiorProductConfigPage(pageReqVO, haokaProductId));
    }

    @PostMapping("/superior-product-config/create")
    @Operation(summary = "创建产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:product:create')")
    public CommonResult<Long> createSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        return success(productService.createSuperiorProductConfig(superiorProductConfig));
    }

    @PutMapping("/superior-product-config/update")
    @Operation(summary = "更新产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:product:update')")
    public CommonResult<Boolean> updateSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        productService.updateSuperiorProductConfig(superiorProductConfig);
        return success(true);
    }

    @DeleteMapping("/superior-product-config/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:product:delete')")
    public CommonResult<Boolean> deleteSuperiorProductConfig(@RequestParam("id") Long id) {
        productService.deleteSuperiorProductConfig(id);
        return success(true);
    }

	@GetMapping("/superior-product-config/get")
	@Operation(summary = "获得产品对接上游配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product:query')")
	public CommonResult<SuperiorProductConfigDO> getSuperiorProductConfig(@RequestParam("id") Long id) {
	    return success(productService.getSuperiorProductConfig(id));
	}

}