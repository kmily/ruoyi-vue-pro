package cn.iocoder.yudao.module.member.controller.app.devicenotice;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

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


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;
import cn.iocoder.yudao.module.member.convert.devicenotice.DeviceNoticeConvert;
import cn.iocoder.yudao.module.member.service.devicenotice.DeviceNoticeService;

@Tag(name = "用户 APP - 设备通知")
@RestController
@RequestMapping("/member/device-notice")
@Validated
public class AppDeviceNoticeController {

    @Resource
    private DeviceNoticeService deviceNoticeService;

    @PostMapping("/create")
    @Operation(summary = "创建设备通知")

    public CommonResult<Long> createDeviceNotice(@Valid @RequestBody AppDeviceNoticeCreateReqVO createReqVO) {
        return success(deviceNoticeService.createDeviceNotice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备通知")

    public CommonResult<Boolean> updateDeviceNotice(@Valid @RequestBody AppDeviceNoticeUpdateReqVO updateReqVO) {
        deviceNoticeService.updateDeviceNotice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备通知")
    @Parameter(name = "id", description = "编号", required = true)

    public CommonResult<Boolean> deleteDeviceNotice(@RequestParam("id") Long id) {
        deviceNoticeService.deleteDeviceNotice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备通知")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")

    public CommonResult<AppDeviceNoticeRespVO> getDeviceNotice(@RequestParam("id") Long id) {
        DeviceNoticeDO deviceNotice = deviceNoticeService.getDeviceNotice(id);
        return success(DeviceNoticeConvert.INSTANCE.convert(deviceNotice));
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备通知列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")

    public CommonResult<List<AppDeviceNoticeRespVO>> getDeviceNoticeList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceNoticeDO> list = deviceNoticeService.getDeviceNoticeList(ids);
        return success(DeviceNoticeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备通知分页")

    public CommonResult<PageResult<AppDeviceNoticeRespVO>> getDeviceNoticePage(@Valid AppDeviceNoticePageReqVO pageVO) {
        PageResult<DeviceNoticeDO> pageResult = deviceNoticeService.getDeviceNoticePage(pageVO);
        return success(DeviceNoticeConvert.INSTANCE.convertPage(pageResult));
    }



}
