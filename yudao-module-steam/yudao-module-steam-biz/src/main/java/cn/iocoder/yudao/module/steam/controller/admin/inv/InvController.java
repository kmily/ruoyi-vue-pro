package cn.iocoder.yudao.module.steam.controller.admin.inv;

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

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.service.inv.InvService;

@Tag(name = "管理后台 - steam用户库存储")
@RestController
@RequestMapping("/steam/inv")
@Validated
public class InvController {

    @Resource
    private InvService invService;

    @PostMapping("/create")
    @Operation(summary = "创建steam用户库存储")
    @PreAuthorize("@ss.hasPermission('steam:inv:create')")
    public CommonResult<Integer> createInv(@Valid @RequestBody InvSaveReqVO createReqVO) {
        return success(invService.createInv(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新steam用户库存储")
    @PreAuthorize("@ss.hasPermission('steam:inv:update')")
    public CommonResult<Boolean> updateInv(@Valid @RequestBody InvSaveReqVO updateReqVO) {
        invService.updateInv(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除steam用户库存储")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:inv:delete')")
    public CommonResult<Boolean> deleteInv(@RequestParam("id") Integer id) {
        invService.deleteInv(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得steam用户库存储")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:inv:query')")
    public CommonResult<InvRespVO> getInv(@RequestParam("id") Integer id) {
        InvDO inv = invService.getInv(id);
        return success(BeanUtils.toBean(inv, InvRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得steam用户库存储分页")
    @PreAuthorize("@ss.hasPermission('steam:inv:query')")
    public CommonResult<PageResult<InvRespVO>> getInvPage(@Valid InvPageReqVO pageReqVO) {
        PageResult<InvDO> pageResult = invService.getInvPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InvRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出steam用户库存储 Excel")
    @PreAuthorize("@ss.hasPermission('steam:inv:export')")
    @OperateLog(type = EXPORT)
    public void exportInvExcel(@Valid InvPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InvDO> list = invService.getInvPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "steam用户库存储.xls", "数据", InvRespVO.class,
                        BeanUtils.toBean(list, InvRespVO.class));
    }

}