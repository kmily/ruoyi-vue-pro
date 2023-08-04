package cn.iocoder.yudao.module.member.convert.existalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.existalarmsettings.ExistAlarmSettingsDO;

/**
 * 人员存在感知雷达设置 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ExistAlarmSettingsConvert {

    ExistAlarmSettingsConvert INSTANCE = Mappers.getMapper(ExistAlarmSettingsConvert.class);

    ExistAlarmSettingsDO convert(AppExistAlarmSettingsCreateReqVO bean);

    ExistAlarmSettingsDO convert(AppExistAlarmSettingsUpdateReqVO bean);

    AppExistAlarmSettingsRespVO convert(ExistAlarmSettingsDO bean);

    List<AppExistAlarmSettingsRespVO> convertList(List<ExistAlarmSettingsDO> list);

    PageResult<AppExistAlarmSettingsRespVO> convertPage(PageResult<ExistAlarmSettingsDO> page);

    List<AppExistAlarmSettingsExcelVO> convertList02(List<ExistAlarmSettingsDO> list);

}
