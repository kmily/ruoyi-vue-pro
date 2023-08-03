package cn.iocoder.yudao.module.radar.controller.admin.deviceuser;

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

import cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.module.radar.convert.deviceuser.DeviceUserConvert;
import cn.iocoder.yudao.module.radar.service.deviceuser.DeviceUserService;

@Tag(name = "管理后台 - 设备和用户绑定")
@RestController
@RequestMapping("/radar/device-user")
@Validated
public class DeviceUserController {

    @Resource
    private DeviceUserService deviceUserService;

    @PostMapping("/create")
    @Operation(summary = "创建设备和用户绑定")
    @PreAuthorize("@ss.hasPermission('radar:device-user:create')")
    public CommonResult<Long> createDeviceUser(@Valid @RequestBody DeviceUserCreateReqVO createReqVO) {
        return success(deviceUserService.createDeviceUser(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备和用户绑定")
    @PreAuthorize("@ss.hasPermission('radar:device-user:update')")
    public CommonResult<Boolean> updateDeviceUser(@Valid @RequestBody DeviceUserUpdateReqVO updateReqVO) {
        deviceUserService.updateDeviceUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备和用户绑定")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('radar:device-user:delete')")
    public CommonResult<Boolean> deleteDeviceUser(@RequestParam("id") Long id) {
        deviceUserService.deleteDeviceUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备和用户绑定")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('radar:device-user:query')")
    public CommonResult<DeviceUserRespVO> getDeviceUser(@RequestParam("id") Long id) {
        DeviceUserDO deviceUser = deviceUserService.getDeviceUser(id);
        return success(DeviceUserConvert.INSTANCE.convert(deviceUser));
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备和用户绑定列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('radar:device-user:query')")
    public CommonResult<List<DeviceUserRespVO>> getDeviceUserList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceUserDO> list = deviceUserService.getDeviceUserList(ids);
        return success(DeviceUserConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备和用户绑定分页")
    @PreAuthorize("@ss.hasPermission('radar:device-user:query')")
    public CommonResult<PageResult<DeviceUserRespVO>> getDeviceUserPage(@Valid DeviceUserPageReqVO pageVO) {
        PageResult<DeviceUserDO> pageResult = deviceUserService.getDeviceUserPage(pageVO);
        return success(DeviceUserConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备和用户绑定 Excel")
    @PreAuthorize("@ss.hasPermission('radar:device-user:export')")
    @OperateLog(type = EXPORT)
    public void exportDeviceUserExcel(@Valid DeviceUserExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DeviceUserDO> list = deviceUserService.getDeviceUserList(exportReqVO);
        // 导出 Excel
        List<DeviceUserExcelVO> datas = DeviceUserConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "设备和用户绑定.xls", "数据", DeviceUserExcelVO.class, datas);
    }

}
