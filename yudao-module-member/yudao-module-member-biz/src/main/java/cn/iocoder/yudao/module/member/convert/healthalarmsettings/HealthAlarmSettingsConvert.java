package cn.iocoder.yudao.module.member.convert.healthalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;

/**
 * 体征检测雷达设置 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthAlarmSettingsConvert {

    HealthAlarmSettingsConvert INSTANCE = Mappers.getMapper(HealthAlarmSettingsConvert.class);

    HealthAlarmSettingsDO convert(AppHealthAlarmSettingsCreateReqVO bean);

    HealthAlarmSettingsDO convert(AppHealthAlarmSettingsUpdateReqVO bean);

    AppHealthAlarmSettingsRespVO convert(HealthAlarmSettingsDO bean);

    List<AppHealthAlarmSettingsRespVO> convertList(List<HealthAlarmSettingsDO> list);

    PageResult<AppHealthAlarmSettingsRespVO> convertPage(PageResult<HealthAlarmSettingsDO> page);

    List<AppHealthAlarmSettingsExcelVO> convertList02(List<HealthAlarmSettingsDO> list);

}
