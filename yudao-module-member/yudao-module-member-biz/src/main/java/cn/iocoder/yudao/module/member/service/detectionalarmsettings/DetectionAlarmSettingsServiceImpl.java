package cn.iocoder.yudao.module.member.service.detectionalarmsettings;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.detectionalarmsettings.DetectionAlarmSettingsConvert;
import cn.iocoder.yudao.module.member.dal.mysql.detectionalarmsettings.DetectionAlarmSettingsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 人体检测雷达设置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DetectionAlarmSettingsServiceImpl implements DetectionAlarmSettingsService {

    @Resource
    private DetectionAlarmSettingsMapper detectionAlarmSettingsMapper;

    @Override
    public Long createDetectionAlarmSettings(AppDetectionAlarmSettingsCreateReqVO createReqVO) {
        // 插入
        Long id = null;
        try {
            id = validateDetectionAlarmSettingsExists(createReqVO.getDeviceId());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        DetectionAlarmSettingsDO detectionAlarmSettings = DetectionAlarmSettingsConvert.INSTANCE.convert(createReqVO);
        if(id == null){
            detectionAlarmSettingsMapper.insert(detectionAlarmSettings);
        }else{
            detectionAlarmSettings.setId(id);
            detectionAlarmSettings.setDeleted(false);
            detectionAlarmSettingsMapper.updateById(detectionAlarmSettings);
        }
        // 返回
        return detectionAlarmSettings.getId();
    }

    @Override
    public void updateDetectionAlarmSettings(AppDetectionAlarmSettingsUpdateReqVO updateReqVO) {
        // 校验存在
        Long id = validateDetectionAlarmSettingsExists(updateReqVO.getDeviceId());
        // 更新
        DetectionAlarmSettingsDO updateObj = DetectionAlarmSettingsConvert.INSTANCE.convert(updateReqVO);
        updateObj.setId(id);
        detectionAlarmSettingsMapper.updateById(updateObj);
    }

    @Override
    public void deleteDetectionAlarmSettings(Long deviceId) {
        // 校验存在
        Long id = validateDetectionAlarmSettingsExists(deviceId);

        // 删除
        detectionAlarmSettingsMapper.deleteById(id);
    }

    private Long validateDetectionAlarmSettingsExists(Long id) {
        DetectionAlarmSettingsDO settingsDO = detectionAlarmSettingsMapper.selectOne(DetectionAlarmSettingsDO::getDeviceId, id);
        if (settingsDO == null) {
            throw exception(DETECTION_ALARM_SETTINGS_NOT_EXISTS);
        }

        return settingsDO.getId();
    }


    @Override
    public DetectionAlarmSettingsDO getDetectionAlarmSettings(Long deviceId) {
        return detectionAlarmSettingsMapper.selectOne(DetectionAlarmSettingsDO::getDeviceId, deviceId);
    }

    @Override
    public List<DetectionAlarmSettingsDO> getDetectionAlarmSettingsList(Collection<Long> ids) {
        return detectionAlarmSettingsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DetectionAlarmSettingsDO> getDetectionAlarmSettingsPage(AppDetectionAlarmSettingsPageReqVO pageReqVO) {
        return detectionAlarmSettingsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DetectionAlarmSettingsDO> getDetectionAlarmSettingsList(AppDetectionAlarmSettingsExportReqVO exportReqVO) {
        return detectionAlarmSettingsMapper.selectList(exportReqVO);
    }

}
