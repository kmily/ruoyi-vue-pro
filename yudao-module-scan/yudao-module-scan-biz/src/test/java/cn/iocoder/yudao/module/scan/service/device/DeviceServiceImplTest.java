package cn.iocoder.yudao.module.scan.service.device;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.scan.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.scan.dal.mysql.device.DeviceMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link DeviceServiceImpl} 的单元测试类
*
* @author lyz
*/
@Import(DeviceServiceImpl.class)
public class DeviceServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DeviceServiceImpl deviceService;

    @Resource
    private DeviceMapper deviceMapper;

    @Test
    public void testCreateDevice_success() {
        // 准备参数
        DeviceCreateReqVO reqVO = randomPojo(DeviceCreateReqVO.class);

        // 调用
        Long deviceId = deviceService.createDevice(reqVO);
        // 断言
        assertNotNull(deviceId);
        // 校验记录的属性是否正确
        DeviceDO device = deviceMapper.selectById(deviceId);
        assertPojoEquals(reqVO, device);
    }

    @Test
    public void testUpdateDevice_success() {
        // mock 数据
        DeviceDO dbDevice = randomPojo(DeviceDO.class);
        deviceMapper.insert(dbDevice);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DeviceUpdateReqVO reqVO = randomPojo(DeviceUpdateReqVO.class, o -> {
            o.setId(dbDevice.getId()); // 设置更新的 ID
        });

        // 调用
        deviceService.updateDevice(reqVO);
        // 校验是否更新正确
        DeviceDO device = deviceMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, device);
    }

    @Test
    public void testUpdateDevice_notExists() {
        // 准备参数
        DeviceUpdateReqVO reqVO = randomPojo(DeviceUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> deviceService.updateDevice(reqVO), DEVICE_NOT_EXISTS);
    }

    @Test
    public void testDeleteDevice_success() {
        // mock 数据
        DeviceDO dbDevice = randomPojo(DeviceDO.class);
        deviceMapper.insert(dbDevice);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDevice.getId();

        // 调用
        deviceService.deleteDevice(id);
       // 校验数据不存在了
       assertNull(deviceMapper.selectById(id));
    }

    @Test
    public void testDeleteDevice_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> deviceService.deleteDevice(id), DEVICE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDevicePage() {
       // mock 数据
       DeviceDO dbDevice = randomPojo(DeviceDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setName(null);
           o.setCode(null);
           o.setContact(null);
           o.setPhone(null);
           o.setSerialNo(null);
           o.setProvinceName(null);
           o.setProvinceCode(null);
           o.setCityName(null);
           o.setCityCode(null);
           o.setAreaName(null);
           o.setAreaCode(null);
           o.setAddress(null);
           o.setPassword(null);
           o.setStoreName(null);
       });
       deviceMapper.insert(dbDevice);
       // 测试 createTime 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCreateTime(null)));
       // 测试 name 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setName(null)));
       // 测试 code 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCode(null)));
       // 测试 contact 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setContact(null)));
       // 测试 phone 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setPhone(null)));
       // 测试 serialNo 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setSerialNo(null)));
       // 测试 provinceName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setProvinceName(null)));
       // 测试 provinceCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setProvinceCode(null)));
       // 测试 cityName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCityName(null)));
       // 测试 cityCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCityCode(null)));
       // 测试 areaName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAreaName(null)));
       // 测试 areaCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAreaCode(null)));
       // 测试 address 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAddress(null)));
       // 测试 password 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setPassword(null)));
       // 测试 storeName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setStoreName(null)));
       // 准备参数
       DevicePageReqVO reqVO = new DevicePageReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setName(null);
       reqVO.setCode(null);
       reqVO.setContact(null);
       reqVO.setPhone(null);
       reqVO.setSerialNo(null);
       reqVO.setProvinceName(null);
       reqVO.setProvinceCode(null);
       reqVO.setCityName(null);
       reqVO.setCityCode(null);
       reqVO.setAreaName(null);
       reqVO.setAreaCode(null);
       reqVO.setAddress(null);
       reqVO.setPassword(null);
       reqVO.setStoreName(null);

       // 调用
       PageResult<DeviceDO> pageResult = deviceService.getDevicePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDevice, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDeviceList() {
       // mock 数据
       DeviceDO dbDevice = randomPojo(DeviceDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setName(null);
           o.setCode(null);
           o.setContact(null);
           o.setPhone(null);
           o.setSerialNo(null);
           o.setProvinceName(null);
           o.setProvinceCode(null);
           o.setCityName(null);
           o.setCityCode(null);
           o.setAreaName(null);
           o.setAreaCode(null);
           o.setAddress(null);
           o.setPassword(null);
           o.setStoreName(null);
       });
       deviceMapper.insert(dbDevice);
       // 测试 createTime 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCreateTime(null)));
       // 测试 name 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setName(null)));
       // 测试 code 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCode(null)));
       // 测试 contact 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setContact(null)));
       // 测试 phone 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setPhone(null)));
       // 测试 serialNo 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setSerialNo(null)));
       // 测试 provinceName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setProvinceName(null)));
       // 测试 provinceCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setProvinceCode(null)));
       // 测试 cityName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCityName(null)));
       // 测试 cityCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setCityCode(null)));
       // 测试 areaName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAreaName(null)));
       // 测试 areaCode 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAreaCode(null)));
       // 测试 address 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setAddress(null)));
       // 测试 password 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setPassword(null)));
       // 测试 storeName 不匹配
       deviceMapper.insert(cloneIgnoreId(dbDevice, o -> o.setStoreName(null)));
       // 准备参数
       DeviceExportReqVO reqVO = new DeviceExportReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setName(null);
       reqVO.setCode(null);
       reqVO.setContact(null);
       reqVO.setPhone(null);
       reqVO.setSerialNo(null);
       reqVO.setProvinceName(null);
       reqVO.setProvinceCode(null);
       reqVO.setCityName(null);
       reqVO.setCityCode(null);
       reqVO.setAreaName(null);
       reqVO.setAreaCode(null);
       reqVO.setAddress(null);
       reqVO.setPassword(null);
       reqVO.setStoreName(null);

       // 调用
       List<DeviceDO> list = deviceService.getDeviceList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbDevice, list.get(0));
    }

}
