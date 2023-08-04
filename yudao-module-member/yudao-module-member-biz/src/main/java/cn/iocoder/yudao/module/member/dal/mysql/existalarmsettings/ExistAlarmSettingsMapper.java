package cn.iocoder.yudao.module.member.dal.mysql.existalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.module.member.dal.dataobject.existalarmsettings.ExistAlarmSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.*;

/**
 * 人员存在感知雷达设置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ExistAlarmSettingsMapper extends BaseMapperX<ExistAlarmSettingsDO> {

    default PageResult<ExistAlarmSettingsDO> selectPage(AppExistAlarmSettingsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExistAlarmSettingsDO>()
                .eqIfPresent(ExistAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(ExistAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(ExistAlarmSettingsDO::getLeave, reqVO.getLeave())
                .eqIfPresent(ExistAlarmSettingsDO::getStop, reqVO.getStop())
                .eqIfPresent(ExistAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(ExistAlarmSettingsDO::getNobody, reqVO.getNobody())
                .betweenIfPresent(ExistAlarmSettingsDO::getStopTime, reqVO.getStopTime())
                .betweenIfPresent(ExistAlarmSettingsDO::getNobodyTime, reqVO.getNobodyTime())
                .betweenIfPresent(ExistAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ExistAlarmSettingsDO::getId));
    }

    default List<ExistAlarmSettingsDO> selectList(AppExistAlarmSettingsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ExistAlarmSettingsDO>()
                .eqIfPresent(ExistAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(ExistAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(ExistAlarmSettingsDO::getLeave, reqVO.getLeave())
                .eqIfPresent(ExistAlarmSettingsDO::getStop, reqVO.getStop())
                .eqIfPresent(ExistAlarmSettingsDO::getNobody, reqVO.getNobody())
                .eqIfPresent(ExistAlarmSettingsDO::getNotice, reqVO.getNotice())
                .betweenIfPresent(ExistAlarmSettingsDO::getStopTime, reqVO.getStopTime())
                .betweenIfPresent(ExistAlarmSettingsDO::getNobodyTime, reqVO.getNobodyTime())
                .betweenIfPresent(ExistAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ExistAlarmSettingsDO::getId));
    }

}
