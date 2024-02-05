package cn.iocoder.yudao.module.steam.controller.admin.binduser;

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

import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;

@Tag(name = "管理后台 -  steam用户绑定")
@RestController
@RequestMapping("/steam-admin/bind-user")
@Validated
public class BindUserController {

    @Resource
    private BindUserService bindUserService;

    @PostMapping("/create")
    @Operation(summary = "创建 steam用户绑定")
    @PreAuthorize("@ss.hasPermission('steam:bind-user:create')")
    public CommonResult<Integer> createBindUser(@Valid @RequestBody BindUserSaveReqVO createReqVO) {
        return success(bindUserService.createBindUser(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 steam用户绑定")
    @PreAuthorize("@ss.hasPermission('steam:bind-user:update')")
    public CommonResult<Boolean> updateBindUser(@Valid @RequestBody BindUserSaveReqVO updateReqVO) {
        bindUserService.updateBindUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 steam用户绑定")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:bind-user:delete')")
    public CommonResult<Boolean> deleteBindUser(@RequestParam("id") Integer id) {
        bindUserService.deleteBindUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 steam用户绑定")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:bind-user:query')")
    public CommonResult<BindUserRespVO> getBindUser(@RequestParam("id") Integer id) {
        BindUserDO bindUser = bindUserService.getBindUser(id);
        return success(BeanUtils.toBean(bindUser, BindUserRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得 steam用户绑定分页")
    @PreAuthorize("@ss.hasPermission('steam:bind-user:query')")
    public CommonResult<PageResult<BindUserRespVO>> getBindUserPage(@Valid BindUserPageReqVO pageReqVO) {
        PageResult<BindUserDO> pageResult = bindUserService.getBindUserPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BindUserRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出 steam用户绑定 Excel")
    @PreAuthorize("@ss.hasPermission('steam:bind-user:export')")
    @OperateLog(type = EXPORT)
    public void exportBindUserExcel(@Valid BindUserPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BindUserDO> list = bindUserService.getBindUserPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, " steam用户绑定.xls", "数据", BindUserRespVO.class,
                        BeanUtils.toBean(list, BindUserRespVO.class));
    }

}