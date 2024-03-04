package cn.iocoder.yudao.module.steam.controller.admin.devaccount;

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

import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 开放平台用户")
@RestController
@RequestMapping("/steam/dev-account")
@Validated
public class DevAccountController {

    @Resource
    private DevAccountService devAccountService;

    @PostMapping("/create")
    @Operation(summary = "创建开放平台用户")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:create')")
    public CommonResult<Long> createDevAccount(@Valid @RequestBody DevAccountSaveReqVO createReqVO) {
        return success(devAccountService.createDevAccount(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新开放平台用户")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:update')")
    public CommonResult<Boolean> updateDevAccount(@Valid @RequestBody DevAccountSaveReqVO updateReqVO) {
        devAccountService.updateDevAccount(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除开放平台用户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:dev-account:delete')")
    public CommonResult<Boolean> deleteDevAccount(@RequestParam("id") Long id) {
        devAccountService.deleteDevAccount(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得开放平台用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:query')")
    public CommonResult<DevAccountRespVO> getDevAccount(@RequestParam("id") Long id) {
        DevAccountDO devAccount = devAccountService.getDevAccount(id);
        return success(BeanUtils.toBean(devAccount, DevAccountRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得开放平台用户分页")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:query')")
    public CommonResult<PageResult<DevAccountRespVO>> getDevAccountPage(@Valid DevAccountPageReqVO pageReqVO) {
        PageResult<DevAccountDO> pageResult = devAccountService.getDevAccountPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DevAccountRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出开放平台用户 Excel")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:export')")
    @OperateLog(type = EXPORT)
    public void exportDevAccountExcel(@Valid DevAccountPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DevAccountDO> list = devAccountService.getDevAccountPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "开放平台用户.xls", "数据", DevAccountRespVO.class,
                        BeanUtils.toBean(list, DevAccountRespVO.class));
    }

}