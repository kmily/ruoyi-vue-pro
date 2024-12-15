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
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.module.haoka.service.product.ProductLimitCardService;

@Tag(name = "管理后台 - 产品身份证限制")
@RestController
@RequestMapping("/haoka/product-limit-card")
@Validated
public class ProductLimitCardController {

    @Resource
    private ProductLimitCardService productLimitCardService;

    @PostMapping("/create")
    @Operation(summary = "创建产品身份证限制")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:create')")
    public CommonResult<Long> createProductLimitCard(@Valid @RequestBody ProductLimitCardSaveReqVO createReqVO) {
        return success(productLimitCardService.createProductLimitCard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品身份证限制")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:update')")
    public CommonResult<Boolean> updateProductLimitCard(@Valid @RequestBody ProductLimitCardSaveReqVO updateReqVO) {
        productLimitCardService.updateProductLimitCard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品身份证限制")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:delete')")
    public CommonResult<Boolean> deleteProductLimitCard(@RequestParam("id") Long id) {
        productLimitCardService.deleteProductLimitCard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品身份证限制")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:query')")
    public CommonResult<ProductLimitCardRespVO> getProductLimitCard(@RequestParam("id") Long id) {
        ProductLimitCardDO productLimitCard = productLimitCardService.getProductLimitCard(id);
        return success(BeanUtils.toBean(productLimitCard, ProductLimitCardRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品身份证限制分页")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:query')")
    public CommonResult<PageResult<ProductLimitCardRespVO>> getProductLimitCardPage(@Valid ProductLimitCardPageReqVO pageReqVO) {
        PageResult<ProductLimitCardDO> pageResult = productLimitCardService.getProductLimitCardPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductLimitCardRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品身份证限制 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-card:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductLimitCardExcel(@Valid ProductLimitCardPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductLimitCardDO> list = productLimitCardService.getProductLimitCardPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品身份证限制.xls", "数据", ProductLimitCardRespVO.class,
                        BeanUtils.toBean(list, ProductLimitCardRespVO.class));
    }

}