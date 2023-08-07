package cn.iocoder.yudao.module.member.service.fallalarmsettings;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.fallalarmsettings.FallAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.fallalarmsettings.FallAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.dal.mysql.fallalarmsettings.FallAlarmSettingsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 跌倒雷达设置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FallAlarmSettingsServiceImpl implements FallAlarmSettingsService {

    @Resource
    private FallAlarmSettingsMapper fallAlarmSettingsMapper;

    @Override
    public Long createFallAlarmSettings(AppFallAlarmSettingsCreateReqVO createReqVO) {

        Long id = null;

        try {
            id = validateFallAlarmSettingsExists(createReqVO.getDeviceId());
        } catch (Exception e) {
        }

        // 插入
        FallAlarmSettingsDO fallAlarmSettings = FallAlarmSettingsConvert.INSTANCE.convert(createReqVO);
        if(id == null){
            fallAlarmSettingsMapper.insert(fallAlarmSettings);
        }else {
            fallAlarmSettings.setDeleted(false);
            fallAlarmSettingsMapper.updateById(fallAlarmSettings.setId(id));
        }

        // 返回
        return fallAlarmSettings.getId();
    }

    @Override
    public void updateFallAlarmSettings(AppFallAlarmSettingsUpdateReqVO updateReqVO) {
        // 校验存在
        Long id = validateFallAlarmSettingsExists(updateReqVO.getDeviceId());
        // 更新
        FallAlarmSettingsDO updateObj = FallAlarmSettingsConvert.INSTANCE.convert(updateReqVO);
        updateObj.setId(id);
        fallAlarmSettingsMapper.updateById(updateObj);
    }

    @Override
    public void deleteFallAlarmSettings(Long deviceId) {
        // 校验存在
        Long id = validateFallAlarmSettingsExists(deviceId);
        // 删除
        fallAlarmSettingsMapper.deleteById(id);
    }

    private Long validateFallAlarmSettingsExists(Long deviceId) {
        FallAlarmSettingsDO settingsDO = fallAlarmSettingsMapper.selectOne(FallAlarmSettingsDO::getDeviceId, deviceId);
        if (settingsDO == null) {
            throw exception(FALL_ALARM_SETTINGS_NOT_EXISTS);
        }
        return settingsDO.getId();
    }

    @Override
    public FallAlarmSettingsDO getFallAlarmSettings(Long deviceId) {
        return fallAlarmSettingsMapper.selectOne(FallAlarmSettingsDO::getDeviceId, deviceId);
    }

    @Override
    public List<FallAlarmSettingsDO> getFallAlarmSettingsList(Collection<Long> ids) {
        return fallAlarmSettingsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FallAlarmSettingsDO> getFallAlarmSettingsPage(AppFallAlarmSettingsPageReqVO pageReqVO) {
        return fallAlarmSettingsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FallAlarmSettingsDO> getFallAlarmSettingsList(AppFallAlarmSettingsExportReqVO exportReqVO) {
        return fallAlarmSettingsMapper.selectList(exportReqVO);
    }

}
