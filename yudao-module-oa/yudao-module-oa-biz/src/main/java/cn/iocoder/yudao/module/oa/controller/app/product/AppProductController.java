package cn.iocoder.yudao.module.oa.controller.app.product;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.oa.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.oa.convert.product.ProductConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.product.ProductDO;
import cn.iocoder.yudao.module.oa.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "用户App - 产品")
@RestController
@RequestMapping("/oa/product")
@Validated
public class AppProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/get")
    @Operation(summary = "获得产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ProductRespVO> getProduct(@RequestParam("id") Long id) {
        ProductDO product = productService.getProduct(id);
        return success(ProductConvert.INSTANCE.convert(product));
    }

    @GetMapping("/list")
    @Operation(summary = "获得产品列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ProductRespVO>> getProductList(@RequestParam("ids") Collection<Long> ids) {
        List<ProductDO> list = productService.getProductList(ids);
        return success(ProductConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分页")
    public CommonResult<PageResult<ProductRespVO>> getProductPage(@Valid ProductPageReqVO pageVO) {
        PageResult<ProductDO> pageResult = productService.getProductPage(pageVO);
        return success(ProductConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品 Excel")
    @OperateLog(type = EXPORT)
    public void exportProductExcel(@Valid ProductExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProductDO> list = productService.getProductList(exportReqVO);
        // 导出 Excel
        List<ProductExcelVO> datas = ProductConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "产品.xls", "数据", ProductExcelVO.class, datas);
    }

}
