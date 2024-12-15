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
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import cn.iocoder.yudao.module.haoka.service.product.ProductLimitAreaService;

@Tag(name = "管理后台 - 产品区域配置")
@RestController
@RequestMapping("/haoka/product-limit-area")
@Validated
public class ProductLimitAreaController {

    @Resource
    private ProductLimitAreaService productLimitAreaService;

    @PostMapping("/create")
    @Operation(summary = "创建产品区域配置")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:create')")
    public CommonResult<Long> createProductLimitArea(@Valid @RequestBody ProductLimitAreaSaveReqVO createReqVO) {
        return success(productLimitAreaService.createProductLimitArea(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品区域配置")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:update')")
    public CommonResult<Boolean> updateProductLimitArea(@Valid @RequestBody ProductLimitAreaSaveReqVO updateReqVO) {
        productLimitAreaService.updateProductLimitArea(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品区域配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:delete')")
    public CommonResult<Boolean> deleteProductLimitArea(@RequestParam("id") Long id) {
        productLimitAreaService.deleteProductLimitArea(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品区域配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:query')")
    public CommonResult<ProductLimitAreaRespVO> getProductLimitArea(@RequestParam("id") Long id) {
        ProductLimitAreaDO productLimitArea = productLimitAreaService.getProductLimitArea(id);
        return success(BeanUtils.toBean(productLimitArea, ProductLimitAreaRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品区域配置分页")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:query')")
    public CommonResult<PageResult<ProductLimitAreaRespVO>> getProductLimitAreaPage(@Valid ProductLimitAreaPageReqVO pageReqVO) {
        PageResult<ProductLimitAreaDO> pageResult = productLimitAreaService.getProductLimitAreaPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductLimitAreaRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品区域配置 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:product-limit-area:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductLimitAreaExcel(@Valid ProductLimitAreaPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductLimitAreaDO> list = productLimitAreaService.getProductLimitAreaPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品区域配置.xls", "数据", ProductLimitAreaRespVO.class,
                        BeanUtils.toBean(list, ProductLimitAreaRespVO.class));
    }

}