package cn.iocoder.yudao.module.member.convert.detectionalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;

/**
 * 人体检测雷达设置 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DetectionAlarmSettingsConvert {

    DetectionAlarmSettingsConvert INSTANCE = Mappers.getMapper(DetectionAlarmSettingsConvert.class);

    DetectionAlarmSettingsDO convert(AppDetectionAlarmSettingsCreateReqVO bean);

    DetectionAlarmSettingsDO convert(AppDetectionAlarmSettingsUpdateReqVO bean);

    AppDetectionAlarmSettingsRespVO convert(DetectionAlarmSettingsDO bean);

    List<AppDetectionAlarmSettingsRespVO> convertList(List<DetectionAlarmSettingsDO> list);

    PageResult<AppDetectionAlarmSettingsRespVO> convertPage(PageResult<DetectionAlarmSettingsDO> page);

    List<AppDetectionAlarmSettingsExcelVO> convertList02(List<DetectionAlarmSettingsDO> list);

}
