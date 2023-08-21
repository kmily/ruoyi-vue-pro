package cn.iocoder.yudao.module.member.service.deviceuser;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.member.api.deviceuser.dto.DeviceUserDTO;
import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.service.deviceuser.dto.FamilyAndRoomDeviceDTO;

/**
 * 设备和用户绑定 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceUserService {

    /**
     * 创建设备和用户绑定
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDeviceUser(@Valid AppDeviceUserCreateReqVO createReqVO);

    /**
     * 更新设备和用户绑定
     *
     * @param updateReqVO 更新信息
     */
    void updateDeviceUser(@Valid AppDeviceUserUpdateReqVO updateReqVO);

    /**
     * 删除设备和用户绑定
     *
     * @param id 编号
     */
    void deleteDeviceUser(Long id);

    /**
     * 获得设备和用户绑定
     *
     * @param id 编号
     * @return 设备和用户绑定
     */
    DeviceUserDO getDeviceUser(Long id);

    /**
     * 获得设备和用户绑定列表
     *
     * @param ids 编号
     * @return 设备和用户绑定列表
     */
    List<DeviceUserDO> getDeviceUserList(Collection<Long> ids);

    /**
     * 获得设备和用户绑定分页
     *
     * @param pageReqVO 分页查询
     * @return 设备和用户绑定分页
     */
    PageResult<DeviceUserDO> getDeviceUserPage(AppDeviceUserPageReqVO pageReqVO);

    /**
     * 获得设备和用户绑定列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备和用户绑定列表
     */
    List<DeviceUserDO> getDeviceUserList(AppDeviceUserExportReqVO exportReqVO);

    /**
     * 查询用户下所有的绑定设备
     * @param userId 用户ID
     * @return
     */
    Long getDeviceCount(Long userId);

    /**
     * 查询某个房间下的所有设备
     * @param familyId 房间号
     * @param type 设备类型
     * @return
     */
    List<AppDeviceUserVO> getDevicesOfFamily(Long familyId, Integer type);

    /**
     * 根据 设备查询关联
     * @param device 设备ID
     * @return
     */
    DeviceUserDO getDeviceUserByDevice(Long device);

    /**
     * 批量查询
     * @param deviceIds
     * @return
     */
    List<DeviceUserDTO> getDeviceUserListByDevices(Collection<Long> deviceIds);

    List<FamilyAndRoomDeviceDTO> selectFamilyAndRoom(Collection<Long> deviceIds);

}
