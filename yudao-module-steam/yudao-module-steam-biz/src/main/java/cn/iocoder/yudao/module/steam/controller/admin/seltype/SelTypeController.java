package cn.iocoder.yudao.module.steam.controller.admin.seltype;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selweapon.SelWeaponDO;
import cn.iocoder.yudao.module.steam.service.seltype.SelTypeService;

@Tag(name = "管理后台 - 类型选择")
@RestController
@RequestMapping("/steam/sel-type")
@Validated
public class SelTypeController {

    @Resource
    private SelTypeService selTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建类型选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:create')")
    public CommonResult<Long> createSelType(@Valid @RequestBody SelTypeSaveReqVO createReqVO) {
        return success(selTypeService.createSelType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新类型选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:update')")
    public CommonResult<Boolean> updateSelType(@Valid @RequestBody SelTypeSaveReqVO updateReqVO) {
        selTypeService.updateSelType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除类型选择")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-type:delete')")
    public CommonResult<Boolean> deleteSelType(@RequestParam("id") Long id) {
        selTypeService.deleteSelType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得类型选择")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:query')")
    public CommonResult<SelTypeRespVO> getSelType(@RequestParam("id") Long id) {
        SelTypeDO selType = selTypeService.getSelType(id);
        return success(BeanUtils.toBean(selType, SelTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得类型选择分页")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:query')")
    public CommonResult<PageResult<SelTypeRespVO>> getSelTypePage(@Valid SelTypePageReqVO pageReqVO) {
        PageResult<SelTypeDO> pageResult = selTypeService.getSelTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SelTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出类型选择 Excel")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:export')")
    @OperateLog(type = EXPORT)
    public void exportSelTypeExcel(@Valid SelTypePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SelTypeDO> list = selTypeService.getSelTypePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "类型选择.xls", "数据", SelTypeRespVO.class,
                        BeanUtils.toBean(list, SelTypeRespVO.class));
    }

    // ==================== 子表（武器选择） ====================

    @GetMapping("/sel-weapon/page")
    @Operation(summary = "获得武器选择分页")
    @Parameter(name = "typeId", description = "类型选择编号")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:query')")
    public CommonResult<PageResult<SelWeaponDO>> getSelWeaponPage(PageParam pageReqVO,
                                                                                        @RequestParam("typeId") Long typeId) {
        return success(selTypeService.getSelWeaponPage(pageReqVO, typeId));
    }

    @PostMapping("/sel-weapon/create")
    @Operation(summary = "创建武器选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:create')")
    public CommonResult<Long> createSelWeapon(@Valid @RequestBody SelWeaponDO selWeapon) {
        return success(selTypeService.createSelWeapon(selWeapon));
    }

    @PutMapping("/sel-weapon/update")
    @Operation(summary = "更新武器选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:update')")
    public CommonResult<Boolean> updateSelWeapon(@Valid @RequestBody SelWeaponDO selWeapon) {
        selTypeService.updateSelWeapon(selWeapon);
        return success(true);
    }

    @DeleteMapping("/sel-weapon/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除武器选择")
    @PreAuthorize("@ss.hasPermission('steam:sel-type:delete')")
    public CommonResult<Boolean> deleteSelWeapon(@RequestParam("id") Long id) {
        selTypeService.deleteSelWeapon(id);
        return success(true);
    }

	@GetMapping("/sel-weapon/get")
	@Operation(summary = "获得武器选择")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:sel-type:query')")
	public CommonResult<SelWeaponDO> getSelWeapon(@RequestParam("id") Long id) {
	    return success(selTypeService.getSelWeapon(id));
	}

}