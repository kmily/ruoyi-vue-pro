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
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.HaoKaProductDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.module.haoka.service.product.HaoKaProductService;

@Tag(name = "管理后台 - 产品/渠道")
@RestController
@RequestMapping("/haoka/hao-ka-product")
@Validated
public class HaoKaProductController {

    @Resource
    private HaoKaProductService haoKaProductService;

    @PostMapping("/create")
    @Operation(summary = "创建产品/渠道")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:create')")
    public CommonResult<Long> createHaoKaProduct(@Valid @RequestBody HaoKaProductSaveReqVO createReqVO) {
        return success(haoKaProductService.createHaoKaProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品/渠道")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:update')")
    public CommonResult<Boolean> updateHaoKaProduct(@Valid @RequestBody HaoKaProductSaveReqVO updateReqVO) {
        haoKaProductService.updateHaoKaProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品/渠道")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:delete')")
    public CommonResult<Boolean> deleteHaoKaProduct(@RequestParam("id") Long id) {
        haoKaProductService.deleteHaoKaProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品/渠道")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:query')")
    public CommonResult<HaoKaProductRespVO> getHaoKaProduct(@RequestParam("id") Long id) {
        HaoKaProductDO haoKaProduct = haoKaProductService.getHaoKaProduct(id);
        return success(BeanUtils.toBean(haoKaProduct, HaoKaProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品/渠道分页")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:query')")
    public CommonResult<PageResult<HaoKaProductRespVO>> getHaoKaProductPage(@Valid HaoKaProductPageReqVO pageReqVO) {
        PageResult<HaoKaProductDO> pageResult = haoKaProductService.getHaoKaProductPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HaoKaProductRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品/渠道 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportHaoKaProductExcel(@Valid HaoKaProductPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<HaoKaProductDO> list = haoKaProductService.getHaoKaProductPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品/渠道.xls", "数据", HaoKaProductRespVO.class,
                        BeanUtils.toBean(list, HaoKaProductRespVO.class));
    }

    // ==================== 子表（产品对接上游配置） ====================

    @GetMapping("/superior-product-config/page")
    @Operation(summary = "获得产品对接上游配置分页")
    @Parameter(name = "haokaProductId", description = "产品ID")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:query')")
    public CommonResult<PageResult<SuperiorProductConfigDO>> getSuperiorProductConfigPage(PageParam pageReqVO,
                                                                                        @RequestParam("haokaProductId") Long haokaProductId) {
        return success(haoKaProductService.getSuperiorProductConfigPage(pageReqVO, haokaProductId));
    }

    @PostMapping("/superior-product-config/create")
    @Operation(summary = "创建产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:create')")
    public CommonResult<Long> createSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        return success(haoKaProductService.createSuperiorProductConfig(superiorProductConfig));
    }

    @PutMapping("/superior-product-config/update")
    @Operation(summary = "更新产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:update')")
    public CommonResult<Boolean> updateSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigDO superiorProductConfig) {
        haoKaProductService.updateSuperiorProductConfig(superiorProductConfig);
        return success(true);
    }

    @DeleteMapping("/superior-product-config/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:delete')")
    public CommonResult<Boolean> deleteSuperiorProductConfig(@RequestParam("id") Long id) {
        haoKaProductService.deleteSuperiorProductConfig(id);
        return success(true);
    }

	@GetMapping("/superior-product-config/get")
	@Operation(summary = "获得产品对接上游配置")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:hao-ka-product:query')")
	public CommonResult<SuperiorProductConfigDO> getSuperiorProductConfig(@RequestParam("id") Long id) {
	    return success(haoKaProductService.getSuperiorProductConfig(id));
	}

}