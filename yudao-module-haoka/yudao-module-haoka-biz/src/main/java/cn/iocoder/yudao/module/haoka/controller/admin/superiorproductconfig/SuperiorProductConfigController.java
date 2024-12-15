package cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig;

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

import cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.module.haoka.service.superiorproductconfig.SuperiorProductConfigService;

@Tag(name = "管理后台 - 产品对接上游配置")
@RestController
@RequestMapping("/haoka/superior-product-config")
@Validated
public class SuperiorProductConfigController {

    @Resource
    private SuperiorProductConfigService superiorProductConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:create')")
    public CommonResult<Long> createSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigSaveReqVO createReqVO) {
        return success(superiorProductConfigService.createSuperiorProductConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品对接上游配置")
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:update')")
    public CommonResult<Boolean> updateSuperiorProductConfig(@Valid @RequestBody SuperiorProductConfigSaveReqVO updateReqVO) {
        superiorProductConfigService.updateSuperiorProductConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品对接上游配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:delete')")
    public CommonResult<Boolean> deleteSuperiorProductConfig(@RequestParam("id") Long id) {
        superiorProductConfigService.deleteSuperiorProductConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品对接上游配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:query')")
    public CommonResult<SuperiorProductConfigRespVO> getSuperiorProductConfig(@RequestParam("id") Long id) {
        SuperiorProductConfigDO superiorProductConfig = superiorProductConfigService.getSuperiorProductConfig(id);
        return success(BeanUtils.toBean(superiorProductConfig, SuperiorProductConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品对接上游配置分页")
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:query')")
    public CommonResult<PageResult<SuperiorProductConfigRespVO>> getSuperiorProductConfigPage(@Valid SuperiorProductConfigPageReqVO pageReqVO) {
        PageResult<SuperiorProductConfigDO> pageResult = superiorProductConfigService.getSuperiorProductConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SuperiorProductConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品对接上游配置 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:superior-product-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSuperiorProductConfigExcel(@Valid SuperiorProductConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SuperiorProductConfigDO> list = superiorProductConfigService.getSuperiorProductConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品对接上游配置.xls", "数据", SuperiorProductConfigRespVO.class,
                        BeanUtils.toBean(list, SuperiorProductConfigRespVO.class));
    }

}