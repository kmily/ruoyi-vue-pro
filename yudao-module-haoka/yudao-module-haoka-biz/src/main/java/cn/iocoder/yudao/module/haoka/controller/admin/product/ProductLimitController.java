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
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.module.haoka.service.product.ProductLimitService;

@Tag(name = "管理后台 - 产品限制条件")
@RestController
@RequestMapping("/haoka/product-limit")
@Validated
public class ProductLimitController {

    @Resource
    private ProductLimitService productLimitService;

    @PostMapping("/create")
    @Operation(summary = "创建产品限制条件")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:create')")
    public CommonResult<Long> createProductLimit(@Valid @RequestBody ProductLimitSaveReqVO createReqVO) {
        return success(productLimitService.createProductLimit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品限制条件")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:update')")
    public CommonResult<Boolean> updateProductLimit(@Valid @RequestBody ProductLimitSaveReqVO updateReqVO) {
        productLimitService.updateProductLimit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品限制条件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:delete')")
    public CommonResult<Boolean> deleteProductLimit(@RequestParam("id") Long id) {
        productLimitService.deleteProductLimit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品限制条件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:query')")
    public CommonResult<ProductLimitRespVO> getProductLimit(@RequestParam("id") Long id) {
        ProductLimitDO productLimit = productLimitService.getProductLimit(id);
        return success(BeanUtils.toBean(productLimit, ProductLimitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品限制条件分页")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:query')")
    public CommonResult<PageResult<ProductLimitRespVO>> getProductLimitPage(@Valid ProductLimitPageReqVO pageReqVO) {
        PageResult<ProductLimitDO> pageResult = productLimitService.getProductLimitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductLimitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品限制条件 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductLimitExcel(@Valid ProductLimitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductLimitDO> list = productLimitService.getProductLimitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品限制条件.xls", "数据", ProductLimitRespVO.class,
                        BeanUtils.toBean(list, ProductLimitRespVO.class));
    }

    // ==================== 子表（产品身份证限制） ====================

    @GetMapping("/product-limit-card/page")
    @Operation(summary = "获得产品身份证限制分页")
    @Parameter(name = "haokaProductLimitId", description = "产品限制ID")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:query')")
    public CommonResult<PageResult<ProductLimitCardDO>> getProductLimitCardPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaProductLimitId") Long haokaProductLimitId) {
        return success(productLimitService.getProductLimitCardPage(pageReqVO, haokaProductLimitId));
    }

    @PostMapping("/product-limit-card/create")
    @Operation(summary = "创建产品身份证限制")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:create')")
    public CommonResult<Long> createProductLimitCard(@Valid @RequestBody ProductLimitCardDO productLimitCard) {
        return success(productLimitService.createProductLimitCard(productLimitCard));
    }

    @PutMapping("/product-limit-card/update")
    @Operation(summary = "更新产品身份证限制")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:update')")
    public CommonResult<Boolean> updateProductLimitCard(@Valid @RequestBody ProductLimitCardDO productLimitCard) {
        productLimitService.updateProductLimitCard(productLimitCard);
        return success(true);
    }

    @DeleteMapping("/product-limit-card/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除产品身份证限制")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:delete')")
    public CommonResult<Boolean> deleteProductLimitCard(@RequestParam("id") Long id) {
        productLimitService.deleteProductLimitCard(id);
        return success(true);
    }

	@GetMapping("/product-limit-card/get")
	@Operation(summary = "获得产品身份证限制")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product-limit:query')")
	public CommonResult<ProductLimitCardDO> getProductLimitCard(@RequestParam("id") Long id) {
	    return success(productLimitService.getProductLimitCard(id));
	}

}