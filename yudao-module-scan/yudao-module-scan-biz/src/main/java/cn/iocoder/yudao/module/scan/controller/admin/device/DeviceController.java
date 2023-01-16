package cn.iocoder.yudao.module.scan.controller.admin.device;

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

import cn.iocoder.yudao.module.scan.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.scan.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.scan.service.device.DeviceService;

@Api(tags = "管理后台 - 设备")
@RestController
@RequestMapping("/scan/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @PostMapping("/create")
    @ApiOperation("创建设备")
    @PreAuthorize("@ss.hasPermission('scan:device:create')")
    public CommonResult<Long> createDevice(@Valid @RequestBody DeviceCreateReqVO createReqVO) {
        return success(deviceService.createDevice(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新设备")
    @PreAuthorize("@ss.hasPermission('scan:device:update')")
    public CommonResult<Boolean> updateDevice(@Valid @RequestBody DeviceUpdateReqVO updateReqVO) {
        deviceService.updateDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除设备")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:device:delete')")
    public CommonResult<Boolean> deleteDevice(@RequestParam("id") Long id) {
        deviceService.deleteDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得设备")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:device:query')")
    public CommonResult<DeviceRespVO> getDevice(@RequestParam("id") Long id) {
        DeviceDO device = deviceService.getDevice(id);
        return success(DeviceConvert.INSTANCE.convert(device));
    }

    @GetMapping("/list")
    @ApiOperation("获得设备列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:device:query')")
    public CommonResult<List<DeviceRespVO>> getDeviceList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceDO> list = deviceService.getDeviceList(ids);
        return success(DeviceConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得设备分页")
    @PreAuthorize("@ss.hasPermission('scan:device:query')")
    public CommonResult<PageResult<DeviceRespVO>> getDevicePage(@Valid DevicePageReqVO pageVO) {
        PageResult<DeviceDO> pageResult = deviceService.getDevicePage(pageVO);
        return success(DeviceConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出设备 Excel")
    @PreAuthorize("@ss.hasPermission('scan:device:export')")
    @OperateLog(type = EXPORT)
    public void exportDeviceExcel(@Valid DeviceExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DeviceDO> list = deviceService.getDeviceList(exportReqVO);
        // 导出 Excel
        List<DeviceExcelVO> datas = DeviceConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "设备.xls", "数据", DeviceExcelVO.class, datas);
    }

}
