package cn.iocoder.yudao.module.member.dal.mysql.detectionalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.*;

/**
 * 人体检测雷达设置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DetectionAlarmSettingsMapper extends BaseMapperX<DetectionAlarmSettingsDO> {

    default PageResult<DetectionAlarmSettingsDO> selectPage(AppDetectionAlarmSettingsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DetectionAlarmSettingsDO>()
                .eqIfPresent(DetectionAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DetectionAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(DetectionAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(DetectionAlarmSettingsDO::getLeave, reqVO.getLeave())
                .betweenIfPresent(DetectionAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DetectionAlarmSettingsDO::getId));
    }

    default List<DetectionAlarmSettingsDO> selectList(AppDetectionAlarmSettingsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DetectionAlarmSettingsDO>()
                .eqIfPresent(DetectionAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DetectionAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(DetectionAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(DetectionAlarmSettingsDO::getLeave, reqVO.getLeave())
                .betweenIfPresent(DetectionAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DetectionAlarmSettingsDO::getId));
    }

}
