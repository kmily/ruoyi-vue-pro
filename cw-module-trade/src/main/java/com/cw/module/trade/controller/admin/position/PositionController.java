package com.cw.module.trade.controller.admin.position;

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

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import com.cw.module.trade.controller.admin.position.vo.*;
import com.cw.module.trade.dal.dataobject.position.PositionDO;
import com.cw.module.trade.convert.position.PositionConvert;
import com.cw.module.trade.service.position.PositionService;

@Tag(name = "管理后台 - 账户持仓信息")
@RestController
@RequestMapping("/account/position")
@Validated
public class PositionController {

    @Resource
    private PositionService positionService;

    @PostMapping("/create")
    @Operation(summary = "创建账户持仓信息")
    @PreAuthorize("@ss.hasPermission('account:position:create')")
    public CommonResult<Long> createPosition(@Valid @RequestBody PositionCreateReqVO createReqVO) {
        return success(positionService.createPosition(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新账户持仓信息")
    @PreAuthorize("@ss.hasPermission('account:position:update')")
    public CommonResult<Boolean> updatePosition(@Valid @RequestBody PositionUpdateReqVO updateReqVO) {
        positionService.updatePosition(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除账户持仓信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('account:position:delete')")
    public CommonResult<Boolean> deletePosition(@RequestParam("id") Long id) {
        positionService.deletePosition(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得账户持仓信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('account:position:query')")
    public CommonResult<PositionRespVO> getPosition(@RequestParam("id") Long id) {
        PositionDO position = positionService.getPosition(id);
        return success(PositionConvert.INSTANCE.convert(position));
    }

    @GetMapping("/list")
    @Operation(summary = "获得账户持仓信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('account:position:query')")
    public CommonResult<List<PositionRespVO>> getPositionList(@RequestParam("ids") Collection<Long> ids) {
        List<PositionDO> list = positionService.getPositionList(ids);
        return success(PositionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得账户持仓信息分页")
    @PreAuthorize("@ss.hasPermission('account:position:query')")
    public CommonResult<PageResult<PositionRespVO>> getPositionPage(@Valid PositionPageReqVO pageVO) {
        PageResult<PositionDO> pageResult = positionService.getPositionPage(pageVO);
        return success(PositionConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出账户持仓信息 Excel")
    @PreAuthorize("@ss.hasPermission('account:position:export')")
    @OperateLog(type = EXPORT)
    public void exportPositionExcel(@Valid PositionExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<PositionDO> list = positionService.getPositionList(exportReqVO);
        // 导出 Excel
        List<PositionExcelVO> datas = PositionConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "账户持仓信息.xls", "数据", PositionExcelVO.class, datas);
    }

}
