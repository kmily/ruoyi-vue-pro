package cn.iocoder.yudao.module.steam.controller.admin.invdesc;

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

import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;

@Tag(name = "Steam后台 - 库存信息详情")
@RestController
@RequestMapping("/steam-admin/inv-desc")
@Validated
public class InvDescController {

    @Resource
    private InvDescService invDescService;

    @PostMapping("/create")
    @Operation(summary = "创建库存信息详情")
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:create')")
    public CommonResult<Long> createInvDesc(@Valid @RequestBody InvDescSaveReqVO createReqVO) {
        return success(invDescService.createInvDesc(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存信息详情")
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:update')")
    public CommonResult<Boolean> updateInvDesc(@Valid @RequestBody InvDescSaveReqVO updateReqVO) {
        invDescService.updateInvDesc(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存信息详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:delete')")
    public CommonResult<Boolean> deleteInvDesc(@RequestParam("id") Long id) {
        invDescService.deleteInvDesc(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存信息详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:query')")
    public CommonResult<InvDescRespVO> getInvDesc(@RequestParam("id") Long id) {
        InvDescDO invDesc = invDescService.getInvDesc(id);
        return success(BeanUtils.toBean(invDesc, InvDescRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存信息详情分页")
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:query')")
    public CommonResult<PageResult<InvDescRespVO>> getInvDescPage(@Valid InvDescPageReqVO pageReqVO) {
        PageResult<InvDescDO> pageResult = invDescService.getInvDescPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InvDescRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存信息详情 Excel")
    @PreAuthorize("@ss.hasPermission('steam:inv-desc:export')")
    @OperateLog(type = EXPORT)
    public void exportInvDescExcel(@Valid InvDescPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InvDescDO> list = invDescService.getInvDescPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存信息详情.xls", "数据", InvDescRespVO.class,
                        BeanUtils.toBean(list, InvDescRespVO.class));
    }

}