package cn.iocoder.yudao.module.member.controller.app.deviceuser;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.controller.app.room.vo.RoomExportReqVO;
import cn.iocoder.yudao.module.member.controller.app.room.vo.RoomRespVO;
import cn.iocoder.yudao.module.member.convert.room.RoomConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import cn.iocoder.yudao.module.member.service.room.RoomService;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.module.member.convert.deviceuser.DeviceUserConvert;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;

@Tag(name = "用户 APP - 设备和用户绑定")
@RestController
@RequestMapping("/member/device-user")
@Validated
public class AppDeviceUserController {

    @Resource
    private DeviceUserService deviceUserService;

    @Resource
    private RoomService roomService;

    @Resource
    private DeviceApi deviceApi;


    @PostMapping("/create")
    @Operation(summary = "创建设备和用户绑定")
    @PreAuthenticated
    public CommonResult<Long> createDeviceUser(@Valid @RequestBody AppDeviceUserCreateReqVO createReqVO) {
        createReqVO.setUserId(getLoginUserId());
        return success(deviceUserService.createDeviceUser(createReqVO));
    }


    @PutMapping("/update")
    @Operation(summary = "更新设备和用户绑定")
    @PreAuthenticated
    public CommonResult<Boolean> updateDeviceUser(@Valid @RequestBody AppDeviceUserUpdateReqVO updateReqVO) {
        deviceUserService.updateDeviceUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备和用户绑定")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteDeviceUser(@RequestParam("id") Long id) {
        deviceUserService.deleteDeviceUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备和用户绑定")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppDeviceUserRespVO> getDeviceUser(@RequestParam("id") Long id) {
        DeviceUserDO deviceUser = deviceUserService.getDeviceUser(id);
        return success(DeviceUserConvert.INSTANCE.convert(deviceUser));
    }

   /* @GetMapping("/list")
    @Operation(summary = "获得设备和用户绑定列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")

    public CommonResult<List<AppDeviceUserRespVO>> getDeviceUserList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceUserDO> list = deviceUserService.getDeviceUserList(ids);
        return success(DeviceUserConvert.INSTANCE.convertList(list));
    }*/

    @GetMapping("/room-devices")
    @Operation(summary = "获得用户房间和设备列表")
    @Parameter(name = "familyId", description = "家庭ID", required = true, example = "123")
    @PreAuthenticated
    public CommonResult<List<AppDeviceUserRespVO>> getRoomAndDeviceList(@RequestParam(value = "familyId") Long familyId) {
        List<DeviceUserDO> list = deviceUserService.getDeviceUserList(new AppDeviceUserExportReqVO().setUserId(getLoginUserId()).setFamilyId(familyId));
        if(list.isEmpty()){
            return success(new ArrayList<>());
        }

        List<RoomDO> roomList = roomService.getRoomList(new RoomExportReqVO().setFamilyId(familyId));

        Map<Long, String> roomMap = roomList.stream().collect(Collectors.toMap(RoomDO::getId, RoomDO::getName));

        List<DeviceDTO> deviceDTOS = deviceApi.getByIds(list.stream().map(DeviceUserDO::getDeviceId).collect(Collectors.toSet()));

        Map<Long, DeviceDTO> deviceDTOMap = deviceDTOS.stream().collect(Collectors.toMap(DeviceDTO::getId, Function.identity()));
        Map<Long, List<DeviceUserDO>> roomDevice = list.stream().collect(Collectors.groupingBy(DeviceUserDO::getRoomId));
        List<AppDeviceUserRespVO> respVOS = new ArrayList<>();
        roomDevice.forEach((room, devices) -> {
            AppDeviceUserRespVO vo = new AppDeviceUserRespVO()
                    .setId(room).setName(roomMap.get(room));
            List<AppDeviceRespVO> deviceRespVOS = devices.stream().map(item -> {
                DeviceDTO deviceDTO = deviceDTOMap.get(item.getDeviceId());
                return DeviceUserConvert.INSTANCE.convert(deviceDTO)
                        .setCustomName(item.getCustomName())
                        .setSn(deviceDTO.getSn())
                        .setId(item.getId());
            }).collect(Collectors.toList());
            vo.setDeviceRespVOList(deviceRespVOS);
            respVOS.add(vo);
        });
        return success(respVOS);
    }


    @GetMapping("/devices")
    @Operation(summary = "获得用户设备信息")
    @Parameter(name = "familyId", description = "家庭ID", required = true, example = "123")
    @Parameter(name = "type", description = "设备类型 1-体征雷达，2-存在感知雷达，3-跌倒雷达，4-人体检测雷达", example = "1")
    @PreAuthenticated
    public CommonResult<List<AppDeviceUserVO>> getDeviceList(@RequestParam(value = "familyId") Long familyId,
                                                             @RequestParam(value = "type",required = false) Integer type) {
        List<DeviceUserDO> list = deviceUserService.getDeviceUserList(new AppDeviceUserExportReqVO().setUserId(getLoginUserId()).setFamilyId(familyId));
        List<RoomDO> roomList = roomService.getRoomList(new RoomExportReqVO().setFamilyId(familyId));
        Map<Long, String> roomMap = roomList.stream().collect(Collectors.toMap(RoomDO::getId, RoomDO::getName));
        List<DeviceDTO> deviceDTOS = deviceApi.getByIds(list.stream().map(DeviceUserDO::getDeviceId).collect(Collectors.toSet()));
        Map<Long, DeviceDTO> deviceDTOMap = deviceDTOS.stream().collect(Collectors.toMap(DeviceDTO::getId, Function.identity()));
        List<AppDeviceUserVO> userVOS = list.stream().map(item -> new AppDeviceUserVO()
                .setRoomId(item.getRoomId())
                .setId(item.getId())
                .setRoomName(roomMap.get(item.getRoomId()))
                .setDeviceName(deviceDTOMap.get(item.getDeviceId()).getName())
                .setCustomName(item.getCustomName())
                .setDeviceId(item.getDeviceId())
                .setSn(deviceDTOMap.get(item.getDeviceId()).getSn())
                .setType(deviceDTOMap.get(item.getDeviceId()).getType())).collect(Collectors.toList());
        if(Objects.nonNull(type)){
            userVOS = userVOS.stream().filter(item -> Objects.equals(type, item.getType())).collect(Collectors.toList());
        }
        return success(userVOS);
    }


    @GetMapping("/device-count")
    @Operation(summary = "获得用户设备数量")
    @PreAuthenticated
    public CommonResult<Long> queryDeviceCount(){
        Long count = deviceUserService.getDeviceCount(getLoginUserId());
        return success(count);
    }


   /* @GetMapping("/page")
    @Operation(summary = "获得设备和用户绑定分页")

    public CommonResult<PageResult<AppDeviceUserRespVO>> getDeviceUserPage(@Valid AppDeviceUserPageReqVO pageVO) {
        PageResult<DeviceUserDO> pageResult = deviceUserService.getDeviceUserPage(pageVO);
        return success(DeviceUserConvert.INSTANCE.convertPage(pageResult));
    }*/

}
