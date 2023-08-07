package cn.iocoder.yudao.module.member.controller.app.healthalarmsettings;

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

import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;
import cn.iocoder.yudao.module.member.convert.healthalarmsettings.HealthAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.service.healthalarmsettings.HealthAlarmSettingsService;

@Tag(name = "用户 APP - 体征检测雷达设置")
@RestController
@RequestMapping("/member/health-alarm-settings")
@Validated
public class AppHealthAlarmSettingsController {

    @Resource
    private HealthAlarmSettingsService healthAlarmSettingsService;

   /* @PostMapping("/create")
    @Operation(summary = "创建体征检测雷达设置")

    public CommonResult<Long> createHealthAlarmSettings(@Valid @RequestBody AppHealthAlarmSettingsCreateReqVO createReqVO) {
        return success(healthAlarmSettingsService.createHealthAlarmSettings(createReqVO));
    }*/

    @PutMapping("/update")
    @Operation(summary = "更新体征检测雷达设置")

    public CommonResult<Boolean> updateHealthAlarmSettings(@Valid @RequestBody AppHealthAlarmSettingsUpdateReqVO updateReqVO) {
        healthAlarmSettingsService.updateHealthAlarmSettings(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体征检测雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true)

    public CommonResult<Boolean> deleteHealthAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        healthAlarmSettingsService.deleteHealthAlarmSettings(deviceId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体征检测雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true, example = "1024")

    public CommonResult<AppHealthAlarmSettingsRespVO> getHealthAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        HealthAlarmSettingsDO healthAlarmSettings = healthAlarmSettingsService.getHealthAlarmSettings(deviceId);
        return success(HealthAlarmSettingsConvert.INSTANCE.convert(healthAlarmSettings));
    }

 /*   @GetMapping("/list")
    @Operation(summary = "获得体征检测雷达设置列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")

    public CommonResult<List<AppHealthAlarmSettingsRespVO>> getHealthAlarmSettingsList(@RequestParam("ids") Collection<Long> ids) {
        List<HealthAlarmSettingsDO> list = healthAlarmSettingsService.getHealthAlarmSettingsList(ids);
        return success(HealthAlarmSettingsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体征检测雷达设置分页")

    public CommonResult<PageResult<AppHealthAlarmSettingsRespVO>> getHealthAlarmSettingsPage(@Valid AppHealthAlarmSettingsPageReqVO pageVO) {
        PageResult<HealthAlarmSettingsDO> pageResult = healthAlarmSettingsService.getHealthAlarmSettingsPage(pageVO);
        return success(HealthAlarmSettingsConvert.INSTANCE.convertPage(pageResult));
    }*/

}
