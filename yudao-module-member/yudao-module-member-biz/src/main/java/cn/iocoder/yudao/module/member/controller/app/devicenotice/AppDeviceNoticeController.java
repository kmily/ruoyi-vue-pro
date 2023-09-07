package cn.iocoder.yudao.module.member.controller.app.devicenotice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.member.service.deviceuser.dto.FamilyAndRoomDeviceDTO;
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
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Resource
    private DeviceUserService deviceUserService;

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

    public CommonResult<PageResult<AppNoticeTimeGroupVO>> getDeviceNoticePage(@Valid AppDeviceNoticePageReqVO pageVO) {
        PageResult<DeviceNoticeDO> pageResult = deviceNoticeService.getDeviceNoticePage(pageVO);
        PageResult<AppDeviceNoticeRespVO> convertPage = DeviceNoticeConvert.INSTANCE.convertPage(pageResult);
        List<AppDeviceNoticeRespVO> pageResultList = convertPage.getList();
        Set<Long> deviceIds = pageResultList.stream().filter(item -> Objects.equals((byte) 0, item.getCategory()))
                .map(AppDeviceNoticeRespVO::getDeviceId).collect(Collectors.toSet());
        if(CollUtil.isNotEmpty(deviceIds)){
            List<FamilyAndRoomDeviceDTO> deviceUserDTOS = deviceUserService.selectFamilyAndRoom(deviceIds);
            Map<Long, FamilyAndRoomDeviceDTO> deviceDTOMap = deviceUserDTOS.stream().collect(Collectors.toMap(FamilyAndRoomDeviceDTO::getDeviceId, Function.identity(), (key1, key2) -> key2));
            pageResultList.forEach(item -> {
                Long deviceId = item.getDeviceId();
                FamilyAndRoomDeviceDTO roomDeviceDTO = deviceDTOMap.getOrDefault(deviceId, new FamilyAndRoomDeviceDTO());
                item.setFamilyName(roomDeviceDTO.getFamily())
                        .setRoomName(roomDeviceDTO.getRoom());
            });
        }

        List<AppDeviceNoticeRespVO> list = convertPage.getList();

        Map<String, List<AppDeviceNoticeRespVO>> collect = list.stream().collect(Collectors.groupingBy(item -> DateUtil.format(item.getHappenTime(), "yyyy-MM-dd HH:mm")));

        PageResult<AppNoticeTimeGroupVO> page = new PageResult<>();
        page.setTotal(convertPage.getTotal());

        List<AppNoticeTimeGroupVO> groupVOS = new ArrayList<>();
        collect.forEach((key, list1) -> {
            groupVOS.add(new AppNoticeTimeGroupVO().setTime(key).setRespVOS(list1));
        });
        groupVOS.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
        page.setList(groupVOS);
        return success(page);
    }


    @GetMapping("/unRead")
    @Operation(summary = "查询未读消息数量")
    @Parameter(name = "familyId", description = "家庭ID")
    @PreAuthenticated
    public CommonResult<Long> getUnRead(@RequestParam(value = "familyId", required = false) Long familyId) {
        Long count = deviceNoticeService.getUnRead(SecurityFrameworkUtils.getLoginUserId(), familyId);
        return success(count);
    }


    @PutMapping("/read")
    @Operation(summary = "更新为已读")
    @Parameter(name = "id", description = "消息ID", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<Boolean> setRead(@RequestParam("id") Long id){
        deviceNoticeService.updateToRead(id);
        return success(true);
    }

}
