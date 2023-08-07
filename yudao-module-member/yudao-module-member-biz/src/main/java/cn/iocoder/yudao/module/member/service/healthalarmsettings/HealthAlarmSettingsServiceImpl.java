package cn.iocoder.yudao.module.member.service.healthalarmsettings;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.healthalarmsettings.HealthAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.dal.mysql.healthalarmsettings.HealthAlarmSettingsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 体征检测雷达设置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HealthAlarmSettingsServiceImpl implements HealthAlarmSettingsService {

    @Resource
    private HealthAlarmSettingsMapper healthAlarmSettingsMapper;

    @Override
    public Long createHealthAlarmSettings(AppHealthAlarmSettingsCreateReqVO createReqVO) {

        Long id = null;

        try {
            id = validateHealthAlarmSettingsExists(createReqVO.getDeviceId());
        } catch (Exception e) {
        }
        // 插入
        HealthAlarmSettingsDO healthAlarmSettings = HealthAlarmSettingsConvert.INSTANCE.convert(createReqVO);
        if(id == null){
            healthAlarmSettingsMapper.insert(healthAlarmSettings);
        }else {
            healthAlarmSettings.setDeleted(false);
            healthAlarmSettingsMapper.updateById(healthAlarmSettings.setId(id));
        }
        // 返回
        return healthAlarmSettings.getId();
    }

    @Override
    public void updateHealthAlarmSettings(AppHealthAlarmSettingsUpdateReqVO updateReqVO) {
        // 校验存在
        Long id = validateHealthAlarmSettingsExists(updateReqVO.getDeviceId());
        // 更新
        HealthAlarmSettingsDO updateObj = HealthAlarmSettingsConvert.INSTANCE.convert(updateReqVO);
        updateObj.setId(id);
        healthAlarmSettingsMapper.updateById(updateObj);
    }

    @Override
    public void deleteHealthAlarmSettings(Long deviceId) {
        // 校验存在
        Long id = validateHealthAlarmSettingsExists(deviceId);
        // 删除
        healthAlarmSettingsMapper.deleteById(id);
    }

    private Long validateHealthAlarmSettingsExists(Long id) {
        HealthAlarmSettingsDO settingsDO = healthAlarmSettingsMapper.selectOne(HealthAlarmSettingsDO::getDeviceId, id);
        if (settingsDO == null) {
            throw exception(HEALTH_ALARM_SETTINGS_NOT_EXISTS);
        }
        return settingsDO.getId();
    }

    @Override
    public HealthAlarmSettingsDO getHealthAlarmSettings(Long id) {
        return healthAlarmSettingsMapper.selectOne(HealthAlarmSettingsDO::getDeviceId, id);
    }

    @Override
    public List<HealthAlarmSettingsDO> getHealthAlarmSettingsList(Collection<Long> ids) {
        return healthAlarmSettingsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HealthAlarmSettingsDO> getHealthAlarmSettingsPage(AppHealthAlarmSettingsPageReqVO pageReqVO) {
        return healthAlarmSettingsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HealthAlarmSettingsDO> getHealthAlarmSettingsList(AppHealthAlarmSettingsExportReqVO exportReqVO) {
        return healthAlarmSettingsMapper.selectList(exportReqVO);
    }

}
