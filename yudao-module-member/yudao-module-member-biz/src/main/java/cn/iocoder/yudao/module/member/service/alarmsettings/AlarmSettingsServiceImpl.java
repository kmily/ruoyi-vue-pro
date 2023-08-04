package cn.iocoder.yudao.module.member.service.alarmsettings;

import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.AppExistAlarmSettingsBaseVO;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.AppExistAlarmSettingsCreateReqVO;
import cn.iocoder.yudao.module.member.service.existalarmsettings.ExistAlarmSettingsService;
import cn.iocoder.yudao.module.radar.enums.RadarType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: AlarmSettingsServiceImpl
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/415:25
 */

@Service
public class AlarmSettingsServiceImpl implements AlarmSettingsService{

    @Resource
    private ExistAlarmSettingsService existAlarmSettingsService;

    @Override
    public long createAlarmSettings(Long deviceId, int type) {
        if(type == RadarType.EXIST_AREA.type){
            AppExistAlarmSettingsCreateReqVO alarmSettingsBaseVO = new AppExistAlarmSettingsCreateReqVO();
            alarmSettingsBaseVO.setDeviceId(deviceId);
            existAlarmSettingsService.createExistAlarmSettings(alarmSettingsBaseVO);
        }
        return 0;
    }
}
