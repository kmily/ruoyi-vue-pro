package cn.iocoder.yudao.module.member.service.alarmsettings;

import cn.iocoder.yudao.module.member.config.AlarmSettingsProperties;
import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.AppDetectionAlarmSettingsCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.AppExistAlarmSettingsBaseVO;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.AppExistAlarmSettingsCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.AppFallAlarmSettingsCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.AppHealthAlarmSettingsCreateReqVO;
import cn.iocoder.yudao.module.member.service.detectionalarmsettings.DetectionAlarmSettingsService;
import cn.iocoder.yudao.module.member.service.existalarmsettings.ExistAlarmSettingsService;
import cn.iocoder.yudao.module.member.service.fallalarmsettings.FallAlarmSettingsService;
import cn.iocoder.yudao.module.member.service.healthalarmsettings.HealthAlarmSettingsService;
import cn.iocoder.yudao.module.radar.enums.RadarType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: AlarmSettingsServiceImpl
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/415:25
 */
@EnableConfigurationProperties(AlarmSettingsProperties.class)
@Service
public class AlarmSettingsServiceImpl implements AlarmSettingsService{

    @Resource
    private AlarmSettingsProperties properties;

    @Resource
    private ExistAlarmSettingsService existAlarmSettingsService;

    @Resource
    private FallAlarmSettingsService fallAlarmSettingsService;

    @Resource
    private HealthAlarmSettingsService healthAlarmSettingsService;

    @Resource
    private DetectionAlarmSettingsService detectionAlarmSettingsService;

    @Override
    public long createAlarmSettings(Long deviceId, int type) {
        if(type == RadarType.EXIST_AREA.type){
            // 存在感知雷达设置
            AppExistAlarmSettingsCreateReqVO alarmSettingsBaseVO = new AppExistAlarmSettingsCreateReqVO();
            alarmSettingsBaseVO.setDeviceId(deviceId);
            existAlarmSettingsService.createExistAlarmSettings(alarmSettingsBaseVO);
        }else if(type == RadarType.HEALTH_AREA.type){
            // 人体特征雷达设置

            String breatheRange = properties.getBreatheRange();
            String heartRange = properties.getHeartRange();

            AppHealthAlarmSettingsCreateReqVO alarmSettingsCreateReqVO = new AppHealthAlarmSettingsCreateReqVO();
            alarmSettingsCreateReqVO.setDeviceId(deviceId);
            alarmSettingsCreateReqVO.setBreatheRange(breatheRange);
            alarmSettingsCreateReqVO.setHeartRange(heartRange);
            healthAlarmSettingsService.createHealthAlarmSettings(alarmSettingsCreateReqVO);
        }else if(type == RadarType.DETECTION_AREA.type){
           // 人体检测雷达告警设置
            AppDetectionAlarmSettingsCreateReqVO createReqVO = new AppDetectionAlarmSettingsCreateReqVO();
            createReqVO.setDeviceId(deviceId);
            detectionAlarmSettingsService.createDetectionAlarmSettings(createReqVO);
        }else if(type == RadarType.FAll_AREA.type){
            // 跌倒雷达设置
            AppFallAlarmSettingsCreateReqVO createReqVO = new AppFallAlarmSettingsCreateReqVO();
            createReqVO.setDeviceId(deviceId);
            fallAlarmSettingsService.createFallAlarmSettings(createReqVO);
        }
        return 0;
    }
}
