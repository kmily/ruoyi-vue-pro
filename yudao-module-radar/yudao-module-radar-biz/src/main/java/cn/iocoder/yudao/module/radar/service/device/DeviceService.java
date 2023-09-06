package cn.iocoder.yudao.module.radar.service.device;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.radar.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.radar.controller.app.device.DeviceStatusVO;
import cn.iocoder.yudao.module.radar.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 设备信息 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceService {

    /**
     * 创建设备信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDevice(@Valid DeviceCreateReqVO createReqVO);

    /**
     * 更新设备信息
     *
     * @param updateReqVO 更新信息
     */
    void updateDevice(@Valid DeviceUpdateReqVO updateReqVO);

    /**
     * 删除设备信息
     *
     * @param id 编号
     */
    void deleteDevice(Long id);

    /**
     * 获得设备信息
     *
     * @param id 编号
     * @return 设备信息
     */
    DeviceDO getDevice(Long id);

    /**
     * 获得设备信息列表
     *
     * @param ids 编号
     * @return 设备信息列表
     */
    List<DeviceDO> getDeviceList(Collection<Long> ids);

    /**
     * 获得设备信息分页
     *
     * @param pageReqVO 分页查询
     * @return 设备信息分页
     */
    PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO);

    /**
     * 获得设备信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备信息列表
     */
    List<DeviceDO> getDeviceList(DeviceExportReqVO exportReqVO);

    /**
     * 更新保活时间
     * @param id
     * @param now
     */
    void updateKeepalive(Long id, LocalDateTime now);

    /**
     * 查询设备状态
     * @param ids
     * @return
     */
    List<DeviceStatusVO> getDeviceStatusList(Collection<Long> ids);


    /**
     * 更改绑定
     * @param bind
     * @param id
     */
    void updateBind(byte bind, Long id);
}
