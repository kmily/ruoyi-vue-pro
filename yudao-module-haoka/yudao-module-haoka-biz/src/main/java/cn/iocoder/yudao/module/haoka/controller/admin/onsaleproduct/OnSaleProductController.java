package cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct;

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

import cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct.OnSaleProductDO;
import cn.iocoder.yudao.module.haoka.service.onsaleproduct.OnSaleProductService;

@Tag(name = "管理后台 - 在售产品")
@RestController
@RequestMapping("/haoka/on-sale-product")
@Validated
public class OnSaleProductController {

    @Resource
    private OnSaleProductService onSaleProductService;

    @PostMapping("/create")
    @Operation(summary = "创建在售产品")
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:create')")
    public CommonResult<Long> createOnSaleProduct(@Valid @RequestBody OnSaleProductSaveReqVO createReqVO) {
        return success(onSaleProductService.createOnSaleProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新在售产品")
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:update')")
    public CommonResult<Boolean> updateOnSaleProduct(@Valid @RequestBody OnSaleProductSaveReqVO updateReqVO) {
        onSaleProductService.updateOnSaleProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除在售产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:delete')")
    public CommonResult<Boolean> deleteOnSaleProduct(@RequestParam("id") Long id) {
        onSaleProductService.deleteOnSaleProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得在售产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:query')")
    public CommonResult<OnSaleProductRespVO> getOnSaleProduct(@RequestParam("id") Long id) {
        OnSaleProductDO onSaleProduct = onSaleProductService.getOnSaleProduct(id);
        return success(BeanUtils.toBean(onSaleProduct, OnSaleProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得在售产品分页")
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:query')")
    public CommonResult<PageResult<OnSaleProductRespVO>> getOnSaleProductPage(@Valid OnSaleProductPageReqVO pageReqVO) {
        PageResult<OnSaleProductDO> pageResult = onSaleProductService.getOnSaleProductPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OnSaleProductRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出在售产品 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:on-sale-product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOnSaleProductExcel(@Valid OnSaleProductPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OnSaleProductDO> list = onSaleProductService.getOnSaleProductPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "在售产品.xls", "数据", OnSaleProductRespVO.class,
                        BeanUtils.toBean(list, OnSaleProductRespVO.class));
    }

}