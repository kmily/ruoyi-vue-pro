package cn.iocoder.yudao.module.scan.controller.admin.users;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

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

import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import cn.iocoder.yudao.module.scan.convert.users.UsersConvert;
import cn.iocoder.yudao.module.scan.service.users.UsersService;

@Api(tags = "管理后台 - 扫描用户")
@RestController
@RequestMapping("/scan/users")
@Validated
public class UsersController {

    @Resource
    private UsersService usersService;

    @PostMapping("/create")
    @ApiOperation("创建扫描用户")
    @PreAuthorize("@ss.hasPermission('scan:users:create')")
    public CommonResult<Long> createUsers(@Valid @RequestBody UsersCreateReqVO createReqVO) {
        return success(usersService.createUsers(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新扫描用户")
    @PreAuthorize("@ss.hasPermission('scan:users:update')")
    public CommonResult<Boolean> updateUsers(@Valid @RequestBody UsersUpdateReqVO updateReqVO) {
        usersService.updateUsers(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除扫描用户")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:users:delete')")
    public CommonResult<Boolean> deleteUsers(@RequestParam("id") Long id) {
        usersService.deleteUsers(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得扫描用户")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:users:query')")
    public CommonResult<UsersRespVO> getUsers(@RequestParam("id") Long id) {
        UsersDO users = usersService.getUsers(id);
        return success(UsersConvert.INSTANCE.convert(users));
    }

    @GetMapping("/list")
    @ApiOperation("获得扫描用户列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:users:query')")
    public CommonResult<List<UsersRespVO>> getUsersList(@RequestParam("ids") Collection<Long> ids) {
        List<UsersDO> list = usersService.getUsersList(ids);
        return success(UsersConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得扫描用户分页")
    @PreAuthorize("@ss.hasPermission('scan:users:query')")
    public CommonResult<PageResult<UsersRespVO>> getUsersPage(@Valid UsersPageReqVO pageVO) {
        PageResult<UsersDO> pageResult = usersService.getUsersPage(pageVO);
        return success(UsersConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出扫描用户 Excel")
    @PreAuthorize("@ss.hasPermission('scan:users:export')")
    @OperateLog(type = EXPORT)
    public void exportUsersExcel(@Valid UsersExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UsersDO> list = usersService.getUsersList(exportReqVO);
        // 导出 Excel
        List<UsersExcelVO> datas = UsersConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "扫描用户.xls", "数据", UsersExcelVO.class, datas);
    }

}
