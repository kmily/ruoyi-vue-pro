package cn.iocoder.yudao.module.scan.service.device;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.device.DeviceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 设备 Service 实现类
 *
 * @author lyz
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
        this.validateDeviceExists(updateReqVO.getId());
        // 更新
        DeviceDO updateObj = DeviceConvert.INSTANCE.convert(updateReqVO);
        deviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevice(Long id) {
        // 校验存在
        this.validateDeviceExists(id);
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
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public Long bind(DeviceCreateReqVO createReqVO) {

//        DeviceDO deviceDO=deviceMapper.selectOne(DeviceDO::getCode, createReqVO.getCode());
//        if(null !=deviceDO){
//            throw exception(DEVICE_BIND_EXISTS);
//        }
        deviceMapper.delete(new QueryWrapper<DeviceDO>().eq("code", createReqVO.getCode()));
        createReqVO.setPassword("10008888");//默认设备管理密码
        // 插入
        DeviceDO device = DeviceConvert.INSTANCE.convert(createReqVO);
        deviceMapper.insert(device);
        // 返回
        return device.getId();
    }
    @Override
    public void unBind(String code){
//        DeviceDO deviceDO=deviceMapper.selectOne(DeviceDO::getCode,code);
//        if(null ==deviceDO){
//            throw exception(DEVICE_NOT_EXISTS);
//        }
        deviceMapper.delete(new QueryWrapper<DeviceDO>().eq("code", code));
//        deviceMapper.deleteById(deviceDO.getId());
    }
    @Override
    public void modifyDevice(@Valid DeviceUpdateReqVO updateReqVO){
        // 校验存在
        DeviceDO deviceDO=deviceMapper.selectOne(DeviceDO::getCode,updateReqVO.getCode());
        if(null ==deviceDO){
            throw exception(DEVICE_NOT_EXISTS);
        }
        updateReqVO.setId(deviceDO.getId());
        updateReqVO.setPassword(deviceDO.getPassword());

        // 更新
        DeviceDO updateObj = DeviceConvert.INSTANCE.convert(updateReqVO);
        deviceMapper.updateById(updateObj);
    }
    @Override
    public DeviceDO getByCode(String code) {
        DeviceDO deviceDO=deviceMapper.selectOne(DeviceDO::getCode,code);
        if(null ==deviceDO){
            throw exception(DEVICE_NOT_EXISTS);
        }
        return deviceDO;
    }
}
