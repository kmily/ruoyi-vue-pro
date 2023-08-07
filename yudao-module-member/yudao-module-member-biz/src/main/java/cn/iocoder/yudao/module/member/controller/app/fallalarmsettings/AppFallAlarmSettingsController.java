package cn.iocoder.yudao.module.member.controller.app.fallalarmsettings;

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

import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.fallalarmsettings.FallAlarmSettingsDO;
import cn.iocoder.yudao.module.member.convert.fallalarmsettings.FallAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.service.fallalarmsettings.FallAlarmSettingsService;

@Tag(name = "用户 APP - 跌倒雷达设置")
@RestController
@RequestMapping("/member/fall-alarm-settings")
@Validated
public class AppFallAlarmSettingsController {

    @Resource
    private FallAlarmSettingsService fallAlarmSettingsService;

   /* @PostMapping("/create")
    @Operation(summary = "创建跌倒雷达设置")

    public CommonResult<Long> createFallAlarmSettings(@Valid @RequestBody AppFallAlarmSettingsCreateReqVO createReqVO) {
        return success(fallAlarmSettingsService.createFallAlarmSettings(createReqVO));
    }
*/
    @PutMapping("/update")
    @Operation(summary = "更新跌倒雷达设置")

    public CommonResult<Boolean> updateFallAlarmSettings(@Valid @RequestBody AppFallAlarmSettingsUpdateReqVO updateReqVO) {
        fallAlarmSettingsService.updateFallAlarmSettings(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除跌倒雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true)

    public CommonResult<Boolean> deleteFallAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        fallAlarmSettingsService.deleteFallAlarmSettings(deviceId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得跌倒雷达设置")
    @Parameter(name = "deviceId", description = "设备Id", required = true, example = "1024")

    public CommonResult<AppFallAlarmSettingsRespVO> getFallAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        FallAlarmSettingsDO fallAlarmSettings = fallAlarmSettingsService.getFallAlarmSettings(deviceId);
        return success(FallAlarmSettingsConvert.INSTANCE.convert(fallAlarmSettings));
    }
/*
    @GetMapping("/list")
    @Operation(summary = "获得跌倒雷达设置列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")

    public CommonResult<List<AppFallAlarmSettingsRespVO>> getFallAlarmSettingsList(@RequestParam("ids") Collection<Long> ids) {
        List<FallAlarmSettingsDO> list = fallAlarmSettingsService.getFallAlarmSettingsList(ids);
        return success(FallAlarmSettingsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得跌倒雷达设置分页")

    public CommonResult<PageResult<AppFallAlarmSettingsRespVO>> getFallAlarmSettingsPage(@Valid AppFallAlarmSettingsPageReqVO pageVO) {
        PageResult<FallAlarmSettingsDO> pageResult = fallAlarmSettingsService.getFallAlarmSettingsPage(pageVO);
        return success(FallAlarmSettingsConvert.INSTANCE.convertPage(pageResult));
    }*/


}
