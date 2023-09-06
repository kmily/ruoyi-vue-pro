package cn.iocoder.yudao.module.member.controller.app.existalarmsettings;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.radar.api.device.DeviceApi;
import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
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

import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.existalarmsettings.ExistAlarmSettingsDO;
import cn.iocoder.yudao.module.member.convert.existalarmsettings.ExistAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.service.existalarmsettings.ExistAlarmSettingsService;

@Tag(name = "用户 APP - 人员存在感知雷达设置")
@RestController
@RequestMapping("/member/exist-alarm-settings")
@Validated
public class AppExistAlarmSettingsController {

    @Resource
    private ExistAlarmSettingsService existAlarmSettingsService;

    @Resource
    private DeviceApi deviceApi;
/*
    @PostMapping("/create")
    @Operation(summary = "创建人员存在感知雷达设置")
    @PreAuthenticated
    public CommonResult<Long> createExistAlarmSettings(@Valid @RequestBody AppExistAlarmSettingsCreateReqVO createReqVO) {
        return success(existAlarmSettingsService.createExistAlarmSettings(createReqVO));
    }*/

    @PutMapping("/update")
    @Operation(summary = "更新人员存在感知雷达设置")
    @PreAuthenticated
    public CommonResult<Boolean> updateExistAlarmSettings(@Valid @RequestBody AppExistAlarmSettingsUpdateReqVO updateReqVO) {
        existAlarmSettingsService.updateExistAlarmSettings(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除人员存在感知雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteExistAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        existAlarmSettingsService.deleteExistAlarmSettings(deviceId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得人员存在感知雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppExistAlarmSettingsRespVO> getExistAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        ExistAlarmSettingsDO existAlarmSettings = existAlarmSettingsService.getExistAlarmSettings(deviceId);
        AppExistAlarmSettingsRespVO convert = ExistAlarmSettingsConvert.INSTANCE.convert(existAlarmSettings);
        List<DeviceDTO> deviceDTOS = deviceApi.getByIds(Collections.singleton(existAlarmSettings.getDeviceId()));
        if(!deviceDTOS.isEmpty()){
            convert.setSn(deviceDTOS.get(0).getSn());
        }
        return success(convert);
    }
/*
    @GetMapping("/list")
    @Operation(summary = "获得人员存在感知雷达设置列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<AppExistAlarmSettingsRespVO>> getExistAlarmSettingsList(@RequestParam("ids") Collection<Long> ids) {
        List<ExistAlarmSettingsDO> list = existAlarmSettingsService.getExistAlarmSettingsList(ids);
        return success(ExistAlarmSettingsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得人员存在感知雷达设置分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppExistAlarmSettingsRespVO>> getExistAlarmSettingsPage(@Valid AppExistAlarmSettingsPageReqVO pageVO) {
        PageResult<ExistAlarmSettingsDO> pageResult = existAlarmSettingsService.getExistAlarmSettingsPage(pageVO);
        return success(ExistAlarmSettingsConvert.INSTANCE.convertPage(pageResult));
    }*/


}
