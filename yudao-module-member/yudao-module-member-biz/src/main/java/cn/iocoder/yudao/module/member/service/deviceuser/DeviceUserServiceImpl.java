package cn.iocoder.yudao.module.member.service.deviceuser;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.module.member.controller.app.room.vo.RoomExportReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import cn.iocoder.yudao.module.member.service.alarmsettings.AlarmSettingsService;
import cn.iocoder.yudao.module.member.service.family.FamilyService;
import cn.iocoder.yudao.module.member.service.room.RoomService;
import cn.iocoder.yudao.module.radar.api.device.DeviceApi;
import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import cn.iocoder.yudao.module.radar.enums.RadarType;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.deviceuser.DeviceUserConvert;
import cn.iocoder.yudao.module.member.dal.mysql.deviceuser.DeviceUserMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 设备和用户绑定 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceUserServiceImpl implements DeviceUserService {

    @Resource
    private DeviceUserMapper deviceUserMapper;

    @Resource
    private DeviceApi deviceApi;

    @Resource
    private FamilyService familyService;

    @Resource
    private RoomService roomService;

    @Resource
    private AlarmSettingsService alarmSettingsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createDeviceUser(AppDeviceUserCreateReqVO createReqVO) {
        DeviceDTO deviceDTO = deviceApi.queryBySn(createReqVO.getDeviceCode());
        if(deviceDTO == null){
            throw exception(DEVICE_NOT_EXISTS);
        }

        FamilyDO family = familyService.getFamily(createReqVO.getFamilyId());
        if(family == null){
            throw exception(FAMILY_NOT_EXISTS);
        }

        RoomDO room = roomService.getRoom(createReqVO.getRoomId());
        if(room == null){
            throw exception(ROOM_NOT_EXISTS);
        }

        if(!Objects.equals(family.getId(),  room.getFamilyId())){
            throw exception(ROOM_NOT_MATE_FAMILY);
        }

        // 插入
        DeviceUserDO deviceUser = DeviceUserConvert.INSTANCE.convert(createReqVO);
        deviceUser.setDeviceId(deviceDTO.getId());
        if(StrUtil.isEmpty(createReqVO.getCustomName())){
            createReqVO.setCustomName(deviceDTO.getName());
        }
        deviceUserMapper.insert(deviceUser);
        // 初始化雷达告警设置
        alarmSettingsService.createAlarmSettings(deviceDTO.getId(),  deviceDTO.getType());
        // 返回
        return deviceUser.getId();
    }

    @Override
    public void updateDeviceUser(AppDeviceUserUpdateReqVO updateReqVO) {
        // 校验存在
        validateDeviceUserExists(updateReqVO.getId());

        // 校验房间是否存在
        if(Objects.nonNull(updateReqVO.getRoomId())){
            RoomDO room = roomService.getRoom(updateReqVO.getRoomId());
            if(room == null){
                throw exception(ROOM_NOT_EXISTS);
            }
        }
        // 更新
        DeviceUserDO updateObj = DeviceUserConvert.INSTANCE.convert(updateReqVO);
        deviceUserMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceUser(Long id) {
        // 校验存在
        validateDeviceUserExists(id);
        // 删除
        deviceUserMapper.deleteById(id);
    }

    private void validateDeviceUserExists(Long id) {
        if (deviceUserMapper.selectById(id) == null) {
            throw exception(DEVICE_USER_NOT_EXISTS);
        }
    }

    @Override
    public DeviceUserDO getDeviceUser(Long id) {
        return deviceUserMapper.selectById(id);
    }

    @Override
    public List<DeviceUserDO> getDeviceUserList(Collection<Long> ids) {
        return deviceUserMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceUserDO> getDeviceUserPage(AppDeviceUserPageReqVO pageReqVO) {
        return deviceUserMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceUserDO> getDeviceUserList(AppDeviceUserExportReqVO exportReqVO) {
        return deviceUserMapper.selectList(exportReqVO);
    }

    @Override
    public Long getDeviceCount(Long userId) {
        return deviceUserMapper.selectCount(DeviceUserDO::getUserId, userId);

    }

    @Override
    public List<AppDeviceUserVO> getDevicesOfFamily(Long familyId, Integer type) {
        List<DeviceUserDO> list = this.getDeviceUserList(new AppDeviceUserExportReqVO().setUserId(getLoginUserId()).setFamilyId(familyId));
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
        return userVOS;
    }

}
