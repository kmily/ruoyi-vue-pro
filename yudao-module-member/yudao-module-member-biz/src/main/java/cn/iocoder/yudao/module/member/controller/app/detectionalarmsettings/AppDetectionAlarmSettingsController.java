package cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings;

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

import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;
import cn.iocoder.yudao.module.member.convert.detectionalarmsettings.DetectionAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.service.detectionalarmsettings.DetectionAlarmSettingsService;

@Tag(name = "用户 APP - 人体检测雷达设置")
@RestController
@RequestMapping("/member/detection-alarm-settings")
@Validated
public class AppDetectionAlarmSettingsController {

    @Resource
    private DetectionAlarmSettingsService detectionAlarmSettingsService;

    /*@PostMapping("/create")
    @Operation(summary = "创建人体检测雷达设置")

    public CommonResult<Long> createDetectionAlarmSettings(@Valid @RequestBody AppDetectionAlarmSettingsCreateReqVO createReqVO) {
        return success(detectionAlarmSettingsService.createDetectionAlarmSettings(createReqVO));
    }
*/
    @PutMapping("/update")
    @Operation(summary = "更新人体检测雷达设置")

    public CommonResult<Boolean> updateDetectionAlarmSettings(@Valid @RequestBody AppDetectionAlarmSettingsUpdateReqVO updateReqVO) {
        detectionAlarmSettingsService.updateDetectionAlarmSettings(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除人体检测雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true)

    public CommonResult<Boolean> deleteDetectionAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        detectionAlarmSettingsService.deleteDetectionAlarmSettings(deviceId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得人体检测雷达设置")
    @Parameter(name = "deviceId", description = "设备ID", required = true, example = "1024")

    public CommonResult<AppDetectionAlarmSettingsRespVO> getDetectionAlarmSettings(@RequestParam("deviceId") Long deviceId) {
        DetectionAlarmSettingsDO detectionAlarmSettings = detectionAlarmSettingsService.getDetectionAlarmSettings(deviceId);
        return success(DetectionAlarmSettingsConvert.INSTANCE.convert(detectionAlarmSettings));
    }
/*

    @GetMapping("/list")
    @Operation(summary = "获得人体检测雷达设置列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")

    public CommonResult<List<AppDetectionAlarmSettingsRespVO>> getDetectionAlarmSettingsList(@RequestParam("ids") Collection<Long> ids) {
        List<DetectionAlarmSettingsDO> list = detectionAlarmSettingsService.getDetectionAlarmSettingsList(ids);
        return success(DetectionAlarmSettingsConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得人体检测雷达设置分页")

    public CommonResult<PageResult<AppDetectionAlarmSettingsRespVO>> getDetectionAlarmSettingsPage(@Valid AppDetectionAlarmSettingsPageReqVO pageVO) {
        PageResult<DetectionAlarmSettingsDO> pageResult = detectionAlarmSettingsService.getDetectionAlarmSettingsPage(pageVO);
        return success(DetectionAlarmSettingsConvert.INSTANCE.convertPage(pageResult));
    }
*/


}
