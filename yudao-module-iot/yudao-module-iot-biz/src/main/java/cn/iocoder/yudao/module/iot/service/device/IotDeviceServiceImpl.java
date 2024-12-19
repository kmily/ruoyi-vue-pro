package cn.iocoder.yudao.module.iot.service.device;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.iot.controller.admin.device.vo.device.*;
import cn.iocoder.yudao.module.iot.dal.dataobject.device.IotDeviceDO;
import cn.iocoder.yudao.module.iot.dal.dataobject.device.IotDeviceGroupDO;
import cn.iocoder.yudao.module.iot.dal.dataobject.product.IotProductDO;
import cn.iocoder.yudao.module.iot.dal.mysql.device.IotDeviceMapper;
import cn.iocoder.yudao.module.iot.enums.device.IotDeviceStatusEnum;
import cn.iocoder.yudao.module.iot.enums.product.IotProductDeviceTypeEnum;
import cn.iocoder.yudao.module.iot.service.product.IotProductService;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.module.iot.enums.ErrorCodeConstants.*;

/**
 * IoT 设备 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class IotDeviceServiceImpl implements IotDeviceService {

    @Resource
    private IotDeviceMapper deviceMapper;

    @Resource
    private IotProductService productService;
    @Resource
    @Lazy // 延迟加载，解决循环依赖
    private IotDeviceGroupService deviceGroupService;

    @Override
    public Long createDevice(IotDeviceSaveReqVO createReqVO) {
        // 1.1 校验产品是否存在
        IotProductDO product = productService.getProduct(createReqVO.getProductId());
        if (product == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        // 1.2 校验设备标识是否唯一
        if (deviceMapper.selectByDeviceKey(createReqVO.getDeviceKey()) != null) {
            throw exception(DEVICE_KEY_EXISTS);
        }
        // 1.3 校验设备名称在同一产品下是否唯一
        if (deviceMapper.selectByProductKeyAndDeviceName(product.getProductKey(), createReqVO.getDeviceKey()) != null) {
            throw exception(DEVICE_NAME_EXISTS);
        }
        // 1.4 校验父设备是否为合法网关
        if (IotProductDeviceTypeEnum.isGateway(product.getDeviceType())
                && createReqVO.getGatewayId() != null) {
            validateGatewayDeviceExists(createReqVO.getGatewayId());
        }
        // 1.5 校验分组存在
        deviceGroupService.validateDeviceGroupExists(createReqVO.getGroupIds());

        // 2.1 转换 VO 为 DO
        IotDeviceDO device = BeanUtils.toBean(createReqVO, IotDeviceDO.class, o -> {
            o.setProductKey(product.getProductKey()).setDeviceType(product.getDeviceType());
            // 生成并设置必要的字段
            o.setDeviceSecret(generateDeviceSecret())
                    .setMqttClientId(generateMqttClientId())
                    .setMqttUsername(generateMqttUsername(o.getDeviceName(), o.getProductKey()))
                    .setMqttPassword(generateMqttPassword());
            // 设置设备状态为未激活
            o.setStatus(IotDeviceStatusEnum.INACTIVE.getStatus()).setStatusLastUpdateTime(LocalDateTime.now());
        });
        // 2.2 插入到数据库
        deviceMapper.insert(device);
        return device.getId();
    }

    @Override
    public void updateDevice(IotDeviceSaveReqVO updateReqVO) {
        updateReqVO.setDeviceKey(null).setDeviceName(null).setProductId(null); // 不允许更新
        // 1.1 校验存在
        IotDeviceDO device = validateDeviceExists(updateReqVO.getId());
        // 1.2 校验父设备是否为合法网关
        if (IotProductDeviceTypeEnum.isGateway(device.getDeviceType())
                && updateReqVO.getGatewayId() != null) {
            validateGatewayDeviceExists(updateReqVO.getGatewayId());
        }
        // 1.3 校验分组存在
        deviceGroupService.validateDeviceGroupExists(updateReqVO.getGroupIds());

        // 2. 更新到数据库
        IotDeviceDO updateObj = BeanUtils.toBean(updateReqVO, IotDeviceDO.class);
        deviceMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceGroup(IotDeviceUpdateGroupReqVO updateReqVO) {
        // 1.1 校验设备存在
        List<IotDeviceDO> devices = deviceMapper.selectBatchIds(updateReqVO.getIds());
        if (CollUtil.isEmpty(devices)) {
            return;
        }
        // 1.2 校验分组存在
        deviceGroupService.validateDeviceGroupExists(updateReqVO.getGroupIds());

        // 3. 更新设备分组
        deviceMapper.updateBatch(convertList(devices, device -> new IotDeviceDO()
                .setId(device.getId()).setGroupIds(updateReqVO.getGroupIds())));
    }

    @Override
    public void deleteDevice(Long id) {
        // 1.1 校验存在
        IotDeviceDO device = validateDeviceExists(id);
        // 1.2 如果是网关设备，检查是否有子设备
        if (device.getGatewayId() != null && deviceMapper.selectCountByGatewayId(id) > 0) {
            throw exception(DEVICE_HAS_CHILDREN);
        }

        // 2. 删除设备
        deviceMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeviceList(Collection<Long> ids) {
        // 1.1 校验存在
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<IotDeviceDO> devices = deviceMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(devices)) {
            return;
        }
        // 1.2 校验网关设备是否存在
        for (IotDeviceDO device : devices) {
            if (device.getGatewayId() != null && deviceMapper.selectCountByGatewayId(device.getId()) > 0) {
                throw exception(DEVICE_HAS_CHILDREN);
            }
        }

        // 2. 删除设备
        deviceMapper.deleteByIds(ids);
    }

    /**
     * 校验设备是否存在
     *
     * @param id 设备 ID
     * @return 设备对象
     */
    private IotDeviceDO validateDeviceExists(Long id) {
        IotDeviceDO device = deviceMapper.selectById(id);
        if (device == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
        return device;
    }

    /**
     * 校验网关设备是否存在
     *
     * @param id 设备 ID
     */
    private void validateGatewayDeviceExists(Long id) {
        IotDeviceDO device = deviceMapper.selectById(id);
        if (device == null) {
            throw exception(DEVICE_GATEWAY_NOT_EXISTS);
        }
        if (!IotProductDeviceTypeEnum.isGateway(device.getDeviceType())) {
            throw exception(DEVICE_NOT_GATEWAY);
        }
    }

    @Override
    public IotDeviceDO getDevice(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public PageResult<IotDeviceDO> getDevicePage(IotDevicePageReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<IotDeviceDO> getDeviceList(@Nullable Integer deviceType) {
        return deviceMapper.selectList(deviceType);
    }

    @Override
    public void updateDeviceStatus(IotDeviceStatusUpdateReqVO updateReqVO) {
        // 1. 校验存在
        IotDeviceDO device = validateDeviceExists(updateReqVO.getId());

        // 2.1 更新状态和更新时间
        IotDeviceDO updateDevice = BeanUtils.toBean(updateReqVO, IotDeviceDO.class)
                .setStatusLastUpdateTime(LocalDateTime.now());
        // 2.2 更新状态相关时间
        if (Objects.equals(device.getStatus(), IotDeviceStatusEnum.INACTIVE.getStatus())
                && Objects.equals(updateDevice.getStatus(), IotDeviceStatusEnum.ONLINE.getStatus())) {
            // 从未激活到在线，设置激活时间和最后上线时间
            updateDevice.setActiveTime(LocalDateTime.now()).setLastOnlineTime(LocalDateTime.now());
        } else if (Objects.equals(updateDevice.getStatus(), IotDeviceStatusEnum.ONLINE.getStatus())) {
            // 如果是上线，设置最后上线时间
            updateDevice.setLastOnlineTime(LocalDateTime.now());
        } else if (Objects.equals(updateDevice.getStatus(), IotDeviceStatusEnum.OFFLINE.getStatus())) {
            // 如果是离线，设置最后离线时间
            updateDevice.setLastOfflineTime(LocalDateTime.now());
        }
        // 2.3 更新到数据库
        deviceMapper.updateById(updateDevice);
    }

    @Override
    public Long getDeviceCountByProductId(Long productId) {
        return deviceMapper.selectCountByProductId(productId);
    }

    @Override
    public Long getDeviceCountByGroupId(Long groupId) {
        return deviceMapper.selectCountByGroupId(groupId);
    }

    @Override
    @TenantIgnore
    public IotDeviceDO getDeviceByProductKeyAndDeviceName(String productKey, String deviceName) {
        return deviceMapper.selectByProductKeyAndDeviceName(productKey, deviceName);
    }

    /**
     * 生成 deviceKey
     *
     * @return 生成的 deviceKey
     */
    private String generateDeviceKey() {
        return RandomUtil.randomString(16);
    }

    /**
     * 生成 deviceSecret
     *
     * @return 生成的 deviceSecret
     */
    private String generateDeviceSecret() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 生成 MQTT Client ID
     *
     * @return 生成的 MQTT Client ID
     */
    private String generateMqttClientId() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 生成 MQTT Username
     *
     * @param deviceName 设备名称
     * @param productKey 产品 Key
     * @return 生成的 MQTT Username
     */
    private String generateMqttUsername(String deviceName, String productKey) {
        return deviceName + "&" + productKey;
    }

    /**
     * 生成 MQTT Password
     *
     * @return 生成的 MQTT Password
     */
    private String generateMqttPassword() {
        return RandomUtil.randomString(32);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public IotDeviceImportRespVO importDevice(List<IotDeviceImportExcelVO> importDevices, boolean updateSupport) {
        // 1. 参数校验
        if (CollUtil.isEmpty(importDevices)) {
            throw exception(DEVICE_IMPORT_LIST_IS_EMPTY);
        }

        // 2. 遍历，逐个创建 or 更新
        IotDeviceImportRespVO respVO = IotDeviceImportRespVO.builder().createDeviceNames(new ArrayList<>())
                .updateDeviceNames(new ArrayList<>()).failureDeviceNames(new LinkedHashMap<>()).build();
        importDevices.forEach(importDevice -> {
            try {
                // 2.1.1 校验字段是否符合要求
                try {
                    ValidationUtils.validate(importDevice);
                } catch (ConstraintViolationException ex){
                    respVO.getFailureDeviceNames().put(importDevice.getDeviceName(), ex.getMessage());
                    return;
                }
                // 2.1.2 校验产品是否存在
                IotProductDO product = productService.validateProductExists(importDevice.getProductKey());
                // 2.1.3 校验父设备是否存在
                Long gatewayId = null;
                if (StrUtil.isNotEmpty(importDevice.getParentDeviceName())) {
                    IotDeviceDO gatewayDevice = deviceMapper.selectByDeviceName(importDevice.getParentDeviceName());
                    if (gatewayDevice == null) {
                        throw exception(DEVICE_GATEWAY_NOT_EXISTS);
                    }
                    if (!IotProductDeviceTypeEnum.isGateway(gatewayDevice.getDeviceType())) {
                        throw exception(DEVICE_NOT_GATEWAY);
                    }
                    gatewayId = gatewayDevice.getId();
                }
                // 2.1.4 校验设备分组是否存在
                Set<Long> groupIds = new HashSet<>();
                if (StrUtil.isNotEmpty(importDevice.getGroupNames())) {
                    String[] groupNames = importDevice.getGroupNames().split(",");
                    for (String groupName : groupNames) {
                        IotDeviceGroupDO group = deviceGroupService.getDeviceGroupByName(groupName);
                        if (group == null) {
                            throw exception(DEVICE_GROUP_NOT_EXISTS);
                        }
                        groupIds.add(group.getId());
                    }
                }

                // 2.2.1 判断如果不存在，在进行插入
                IotDeviceDO existDevice = deviceMapper.selectByDeviceName(importDevice.getDeviceName());
                if (existDevice == null) {
                    createDevice(new IotDeviceSaveReqVO()
                            .setDeviceName(importDevice.getDeviceName()).setDeviceKey(generateDeviceKey())
                            .setProductId(product.getId()).setGatewayId(gatewayId).setGroupIds(groupIds));
                    respVO.getCreateDeviceNames().add(importDevice.getDeviceName());
                    return;
                }
                // 2.2.2 如果存在，判断是否允许更新
                if (updateSupport) {
                    throw exception(DEVICE_KEY_EXISTS);
                }
                updateDevice(new IotDeviceSaveReqVO().setId(existDevice.getId())
                        .setGatewayId(gatewayId).setGroupIds(groupIds));
                respVO.getUpdateDeviceNames().add(importDevice.getDeviceName());
            } catch (ServiceException ex) {
                respVO.getFailureDeviceNames().put(importDevice.getDeviceName(), ex.getMessage());
            }
        });
        return respVO;
    }

}