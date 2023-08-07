package cn.iocoder.yudao.module.member.convert.fallalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.fallalarmsettings.FallAlarmSettingsDO;

/**
 * 跌倒雷达设置 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface FallAlarmSettingsConvert {

    FallAlarmSettingsConvert INSTANCE = Mappers.getMapper(FallAlarmSettingsConvert.class);

    FallAlarmSettingsDO convert(AppFallAlarmSettingsCreateReqVO bean);

    FallAlarmSettingsDO convert(AppFallAlarmSettingsUpdateReqVO bean);

    AppFallAlarmSettingsRespVO convert(FallAlarmSettingsDO bean);

    List<AppFallAlarmSettingsRespVO> convertList(List<FallAlarmSettingsDO> list);

    PageResult<AppFallAlarmSettingsRespVO> convertPage(PageResult<FallAlarmSettingsDO> page);

    List<AppFallAlarmSettingsExcelVO> convertList02(List<FallAlarmSettingsDO> list);

}
