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
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductChannelDO;
import cn.iocoder.yudao.module.haoka.service.product.ProductChannelService;

@Tag(name = "管理后台 - 产品的渠道")
@RestController
@RequestMapping("/haoka/product-channel")
@Validated
public class ProductChannelController {

    @Resource
    private ProductChannelService productChannelService;

    @PostMapping("/create")
    @Operation(summary = "创建产品的渠道")
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:create')")
    public CommonResult<Long> createProductChannel(@Valid @RequestBody ProductChannelSaveReqVO createReqVO) {
        return success(productChannelService.createProductChannel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品的渠道")
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:update')")
    public CommonResult<Boolean> updateProductChannel(@Valid @RequestBody ProductChannelSaveReqVO updateReqVO) {
        productChannelService.updateProductChannel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品的渠道")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:delete')")
    public CommonResult<Boolean> deleteProductChannel(@RequestParam("id") Long id) {
        productChannelService.deleteProductChannel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品的渠道")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:query')")
    public CommonResult<ProductChannelRespVO> getProductChannel(@RequestParam("id") Long id) {
        ProductChannelDO productChannel = productChannelService.getProductChannel(id);
        return success(BeanUtils.toBean(productChannel, ProductChannelRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品的渠道分页")
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:query')")
    public CommonResult<PageResult<ProductChannelRespVO>> getProductChannelPage(@Valid ProductChannelPageReqVO pageReqVO) {
        PageResult<ProductChannelDO> pageResult = productChannelService.getProductChannelPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductChannelRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品的渠道 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:product-channel:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductChannelExcel(@Valid ProductChannelPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductChannelDO> list = productChannelService.getProductChannelPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品的渠道.xls", "数据", ProductChannelRespVO.class,
                        BeanUtils.toBean(list, ProductChannelRespVO.class));
    }

}