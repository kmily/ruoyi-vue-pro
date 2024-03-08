package cn.iocoder.yudao.module.steam.controller.admin.bindipaddress;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.module.steam.service.bindipaddress.BindIpaddressService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 绑定用户IP地址池")
@RestController
@RequestMapping("/steam/bind-ipaddress")
@Validated
public class BindIpaddressController {

    @Resource
    private BindIpaddressService bindIpaddressService;

    @PostMapping("/create")
    @Operation(summary = "创建绑定用户IP地址池")
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:create')")
    public CommonResult<Long> createBindIpaddress(@Valid @RequestBody BindIpaddressSaveReqVO createReqVO) {
        return success(bindIpaddressService.createBindIpaddress(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新绑定用户IP地址池")
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:update')")
    public CommonResult<Boolean> updateBindIpaddress(@Valid @RequestBody BindIpaddressSaveReqVO updateReqVO) {
        bindIpaddressService.updateBindIpaddress(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除绑定用户IP地址池")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:delete')")
    public CommonResult<Boolean> deleteBindIpaddress(@RequestParam("id") Long id) {
        bindIpaddressService.deleteBindIpaddress(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得绑定用户IP地址池")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:query')")
    public CommonResult<BindIpaddressRespVO> getBindIpaddress(@RequestParam("id") Long id) {
        BindIpaddressDO bindIpaddress = bindIpaddressService.getBindIpaddress(id);
        return success(BeanUtils.toBean(bindIpaddress, BindIpaddressRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得绑定用户IP地址池分页")
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:query')")
    public CommonResult<PageResult<BindIpaddressRespVO>> getBindIpaddressPage(@Valid BindIpaddressPageReqVO pageReqVO) {
        PageResult<BindIpaddressDO> pageResult = bindIpaddressService.getBindIpaddressPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BindIpaddressRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出绑定用户IP地址池 Excel")
    @PreAuthorize("@ss.hasPermission('steam:bind-ipaddress:export')")
    @OperateLog(type = EXPORT)
    public void exportBindIpaddressExcel(@Valid BindIpaddressPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BindIpaddressDO> list = bindIpaddressService.getBindIpaddressPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "绑定用户IP地址池.xls", "数据", BindIpaddressRespVO.class,
                        BeanUtils.toBean(list, BindIpaddressRespVO.class));
    }

}