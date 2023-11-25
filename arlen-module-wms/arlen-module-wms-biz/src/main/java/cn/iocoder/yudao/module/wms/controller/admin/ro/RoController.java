package cn.iocoder.yudao.module.wms.controller.admin.ro;

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

import cn.iocoder.yudao.module.wms.controller.admin.ro.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoLpnDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoMtrlDO;
import cn.iocoder.yudao.module.wms.service.ro.RoService;

@Tag(name = "管理后台 - 收料单")
@RestController
@RequestMapping("/wms/ro")
@Validated
public class RoController {

    @Resource
    private RoService roService;

    @PostMapping("/create")
    @Operation(summary = "创建收料单")
    @PreAuthorize("@ss.hasPermission('wms:ro:create')")
    public CommonResult<String> createRo(@Valid @RequestBody RoSaveReqVO createReqVO) {
        return success(roService.createRo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收料单")
    @PreAuthorize("@ss.hasPermission('wms:ro:update')")
    public CommonResult<Boolean> updateRo(@Valid @RequestBody RoSaveReqVO updateReqVO) {
        roService.updateRo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收料单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:ro:delete')")
    public CommonResult<Boolean> deleteRo(@RequestParam("id") String id) {
        roService.deleteRo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收料单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:ro:query')")
    public CommonResult<RoRespVO> getRo(@RequestParam("id") String id) {
        RoDO ro = roService.getRo(id);
        return success(BeanUtils.toBean(ro, RoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收料单分页")
    @PreAuthorize("@ss.hasPermission('wms:ro:query')")
    public CommonResult<PageResult<RoRespVO>> getRoPage(@Valid RoPageReqVO pageReqVO) {
        PageResult<RoDO> pageResult = roService.getRoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收料单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:ro:export')")
    @OperateLog(type = EXPORT)
    public void exportRoExcel(@Valid RoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RoDO> list = roService.getRoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收料单.xls", "数据", RoRespVO.class,
                        BeanUtils.toBean(list, RoRespVO.class));
    }

    // ==================== 子表（收料单LPN明细） ====================

    @GetMapping("/ro-lpn/list-by-ro-id")
    @Operation(summary = "获得收料单LPN明细列表")
    @Parameter(name = "roId", description = "收料单ID")
    @PreAuthorize("@ss.hasPermission('wms:ro:query')")
    public CommonResult<List<RoLpnDO>> getRoLpnListByRoId(@RequestParam("roId") String roId) {
        return success(roService.getRoLpnListByRoId(roId));
    }

    // ==================== 子表（收料单物料明细） ====================

    @GetMapping("/ro-mtrl/list-by-ro-id")
    @Operation(summary = "获得收料单物料明细列表")
    @Parameter(name = "roId", description = "收料单ID")
    @PreAuthorize("@ss.hasPermission('wms:ro:query')")
    public CommonResult<List<RoMtrlDO>> getRoMtrlListByRoId(@RequestParam("roId") String roId) {
        return success(roService.getRoMtrlListByRoId(roId));
    }

}