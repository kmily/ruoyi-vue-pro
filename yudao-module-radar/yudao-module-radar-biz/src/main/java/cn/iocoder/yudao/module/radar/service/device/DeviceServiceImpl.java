package cn.iocoder.yudao.module.radar.service.device;

import cn.iocoder.yudao.module.radar.controller.app.device.DeviceStatusVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.radar.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.device.DeviceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 设备信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Long createDevice(DeviceCreateReqVO createReqVO) {
        // 插入
        DeviceDO device = DeviceConvert.INSTANCE.convert(createReqVO);
        deviceMapper.insert(device);
        // 返回
        return device.getId();
    }

    @Override
    public void updateDevice(DeviceUpdateReqVO updateReqVO) {
        // 校验存在
        validateDeviceExists(updateReqVO.getId());
        // 更新
        DeviceDO updateObj = DeviceConvert.INSTANCE.convert(updateReqVO);
        deviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevice(Long id) {
        // 校验存在
        validateDeviceExists(id);
        // 删除
        deviceMapper.deleteById(id);
    }

    private void validateDeviceExists(Long id) {
        if (deviceMapper.selectById(id) == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
    }

    @Override
    public DeviceDO getDevice(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public List<DeviceDO> getDeviceList(Collection<Long> ids) {
        return deviceMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceDO> getDeviceList(DeviceExportReqVO exportReqVO) {
        return deviceMapper.selectList(exportReqVO);
    }

    @Override
    public void updateKeepalive(Long id, LocalDateTime now) {
        deviceMapper.updateById(new DeviceDO().setKeepalive(now).setId(id));
    }

    @Override
    public List<DeviceStatusVO> getDeviceStatusList(Collection<Long> ids) {
        List<DeviceDO> deviceList = this.getDeviceList(ids);
        Map<Long, LocalDateTime> deviceTimeMap = deviceList.stream().collect(HashMap::new, (map, item) -> map.put(item.getId(), item.getKeepalive()),
                HashMap::putAll);
        LocalDateTime now = LocalDateTime.now();

        return ids.stream().map(id -> {
            LocalDateTime dateTime = deviceTimeMap.get(id);
            int status = 0;
            if (dateTime != null && Duration.between(dateTime, now).toMinutes() <= 5L) {
                status = 1;
            }
            return new DeviceStatusVO().setDeviceId(id).setStatus(status);
        }).collect(Collectors.toList());
    }

}
