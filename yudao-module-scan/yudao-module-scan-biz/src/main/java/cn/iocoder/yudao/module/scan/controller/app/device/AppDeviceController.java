package cn.iocoder.yudao.module.scan.controller.app.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.scan.controller.admin.device.vo.DeviceRespVO;
import cn.iocoder.yudao.module.scan.controller.app.device.vo.*;
import cn.iocoder.yudao.module.scan.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.scan.service.device.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "扫描APP - 设备")
@RestController
@RequestMapping("/scan/device")
@Validated
public class AppDeviceController {

    @Resource
    private DeviceService deviceService;
//设备绑定  设备解绑
    @PostMapping("/bind")
    @ApiOperation("设备绑定")
    public CommonResult<Long> bind(@Valid @RequestBody AppDeviceCreateReqVO createReqVO) {

        return success(deviceService.bind(DeviceConvert.INSTANCE.convert(createReqVO)));
    }
    @PostMapping("/unbind")
    @ApiOperation("设备绑定")
    public CommonResult<Boolean> unBind(@Valid @RequestBody AppDeviceCreateReqVO createReqVO) {
        deviceService.unBind(createReqVO.getCode());
        return success(true);
    }
    @PutMapping("/modify")
    @ApiOperation("更新设备")
    public CommonResult<Boolean> modifyDevice(@Valid @RequestBody AppDeviceUpdateReqVO updateReqVO) {
        deviceService.updateDevice(DeviceConvert.INSTANCE.convert(updateReqVO));
        return success(true);
    }

    @GetMapping("/getbycode")
    @ApiOperation("获得设备")
    @ApiImplicitParam(name = "code", value = "设备编号", required = true, dataTypeClass = String.class)
    public CommonResult<AppDeviceRespVO> getByCode(@RequestParam("code") String code) {
        DeviceDO device = deviceService.getByCode(code);
        return success(DeviceConvert.INSTANCE.convert02(device));
    }


}
