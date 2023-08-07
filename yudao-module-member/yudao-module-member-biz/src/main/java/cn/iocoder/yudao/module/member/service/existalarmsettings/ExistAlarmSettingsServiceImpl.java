package cn.iocoder.yudao.module.member.service.existalarmsettings;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.existalarmsettings.ExistAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.existalarmsettings.ExistAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.dal.mysql.existalarmsettings.ExistAlarmSettingsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 人员存在感知雷达设置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ExistAlarmSettingsServiceImpl implements ExistAlarmSettingsService {

    @Resource
    private ExistAlarmSettingsMapper existAlarmSettingsMapper;

    @Override
    public Long createExistAlarmSettings(AppExistAlarmSettingsCreateReqVO createReqVO) {
        Long id = null;
        try {
            id = validateExistAlarmSettingsExists(createReqVO.getDeviceId());
        } catch (Exception e) {
        }

        // 插入
        ExistAlarmSettingsDO existAlarmSettings = ExistAlarmSettingsConvert.INSTANCE.convert(createReqVO);
        if(id == null){
            existAlarmSettingsMapper.insert(existAlarmSettings);
        }else{
            existAlarmSettings.setDeleted(false);
            existAlarmSettingsMapper.updateById(existAlarmSettings.setId(id));
        }
        // 返回
        return existAlarmSettings.getId();
    }

    @Override
    public void updateExistAlarmSettings(AppExistAlarmSettingsUpdateReqVO updateReqVO) {
        // 校验存在
        Long id = validateExistAlarmSettingsExists(updateReqVO.getDeviceId());
        // 更新
        ExistAlarmSettingsDO updateObj = ExistAlarmSettingsConvert.INSTANCE.convert(updateReqVO);
        updateObj.setId(id);
        existAlarmSettingsMapper.updateById(updateObj);
    }

    @Override
    public void deleteExistAlarmSettings(Long deviceId) {
        // 校验存在
        Long id = validateExistAlarmSettingsExists(deviceId);
        // 删除
        existAlarmSettingsMapper.deleteById(id);
    }

    private Long validateExistAlarmSettingsExists(Long id) {
        ExistAlarmSettingsDO settingsDO = existAlarmSettingsMapper.selectOne(ExistAlarmSettingsDO::getDeviceId, id);
        if (settingsDO == null) {
            throw exception(EXIST_ALARM_SETTINGS_NOT_EXISTS);
        }
        return  settingsDO.getId();
    }

    @Override
    public ExistAlarmSettingsDO getExistAlarmSettings(Long deviceId) {
        return existAlarmSettingsMapper.selectOne(ExistAlarmSettingsDO::getDeviceId, deviceId);
    }

    @Override
    public List<ExistAlarmSettingsDO> getExistAlarmSettingsList(Collection<Long> ids) {
        return existAlarmSettingsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ExistAlarmSettingsDO> getExistAlarmSettingsPage(AppExistAlarmSettingsPageReqVO pageReqVO) {
        return existAlarmSettingsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ExistAlarmSettingsDO> getExistAlarmSettingsList(AppExistAlarmSettingsExportReqVO exportReqVO) {
        return existAlarmSettingsMapper.selectList(exportReqVO);
    }

}
